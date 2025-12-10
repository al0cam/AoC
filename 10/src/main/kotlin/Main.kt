package org.example

import java.nio.file.FileSystems
import kotlin.io.path.readText

data class ManualLine(var lights: Int, var buttons: ArrayList<Int>, var joltages: ArrayList<Int>)

fun readFile(fileName: String): ArrayList<ManualLine> {
    val path = FileSystems.getDefault().getPath(fileName)
    val text = path.readText()
    val machines = arrayListOf<ManualLine>()

    text.split("\n").forEach { row ->
        if (row.isBlank()) return@forEach


        val lightsMatch = Regex("""\[([.#]+)]""").find(row) ?: return@forEach
        val lightStr = lightsMatch.groupValues[1]
        var lights = 0
        lightStr.forEachIndexed { i, c ->
            if (c == '#') {
                lights = lights or (1 shl i)
            }
        }


        val buttons = arrayListOf<Int>()
        Regex("""\(([0-9,]+)\)""").findAll(row).forEach { match ->
            val indices = match.groupValues[1].split(",").map { it.toInt() }
            var buttonMask = 0
            indices.forEach { idx ->
                buttonMask = buttonMask or (1 shl idx)
            }
            buttons.add(buttonMask)
        }


        val joltagesMatch = Regex("""\{([0-9,]+)}""").find(row)
        val joltages = if (joltagesMatch != null) {
            arrayListOf(*joltagesMatch.groupValues[1].split(",").map { it.toInt() }.toTypedArray())
        } else {
            arrayListOf()
        }

        machines.add(ManualLine(lights, buttons, joltages))
    }

    return machines
}

fun solveLinearSystem(target: Int, buttons: List<Int>, numLights: Int): Int? {


    val n = buttons.size
    if (n > 20) {

        return gaussianEliminationGF2(target, buttons, numLights)
    }

    var minPresses: Int? = null


    for (mask in 0 until (1 shl n)) {
        var result = 0
        var presses = 0

        for (i in 0 until n) {
            if ((mask and (1 shl i)) != 0) {
                result = result xor buttons[i]
                presses++
            }
        }

        if (result == target) {
            if (minPresses == null || presses < minPresses) {
                minPresses = presses
            }
        }
    }

    return minPresses
}

fun gaussianEliminationGF2(target: Int, buttons: List<Int>, numLights: Int): Int? {

    val n = buttons.size
    val matrix = Array(n) { i -> buttons[i] }
    val targetVec = target


    val buttonSelection = BooleanArray(n) { false }


    val pivot = IntArray(numLights) { -1 }

    for (col in 0 until numLights) {

        var pivotRow = -1
        for (row in 0 until n) {
            if (pivot.contains(row)) continue
            if ((matrix[row] and (1 shl col)) != 0) {
                pivotRow = row
                break
            }
        }

        if (pivotRow == -1) continue
        pivot[col] = pivotRow


        for (row in 0 until n) {
            if (row == pivotRow) continue
            if (pivot.contains(row)) continue
            if ((matrix[row] and (1 shl col)) != 0) {
                matrix[row] = matrix[row] xor matrix[pivotRow]
            }
        }
    }


    var currentState = 0
    for (col in 0 until numLights) {
        val needBit = (targetVec and (1 shl col)) != 0
        val haveBit = (currentState and (1 shl col)) != 0

        if (needBit != haveBit) {
            val pivotRow = pivot[col]
            if (pivotRow != -1) {
                buttonSelection[pivotRow] = !buttonSelection[pivotRow]
                currentState = currentState xor buttons[pivotRow]
            }
        }
    }


    var check = 0
    for (i in 0 until n) {
        if (buttonSelection[i]) {
            check = check xor buttons[i]
        }
    }

    if (check != targetVec) return null

    return buttonSelection.count { it }
}

fun getFewestPresses(machines: ArrayList<ManualLine>): Int {
    var totalPresses = 0

    machines.forEachIndexed { idx, machine ->
        val numLights = Integer.SIZE - Integer.numberOfLeadingZeros(
            maxOf(machine.lights, machine.buttons.maxOrNull() ?: 0)
        )

        val minPresses = solveLinearSystem(machine.lights, machine.buttons, numLights)

        if (minPresses != null) {
            println("Machine ${idx + 1}: $minPresses presses")
            totalPresses += minPresses
        } else {
            println("Machine ${idx + 1}: No solution found")
        }
    }

    return totalPresses
}


fun isButtonAvailable(buttonIndex: Int, mask: Int): Boolean {
    return (mask and (1 shl buttonIndex)) > 0
}

/**
 * Generates the next combination of presses [x0, x1, ..., xm-1] such that sum(xi) = n.
 * The logic iterates through all non-negative integer partitions of 'n' into 'm' parts.
 *
 * @param combinations Mutable list of integers representing [x0, x1, ..., xm-1].
 * @return True if a new combination was successfully generated, false if finished.
 */
fun nextCombination(combinations: MutableList<Int>): Boolean {

    val i = combinations.indexOfLast { it != 0 }


    if (i <= 0) {
        return false
    }

    val v = combinations[i]
    combinations[i - 1] += 1
    combinations[i] = 0
    combinations[combinations.size - 1] += v - 1

    return true
}

/**
 * Solves the joltage system using an optimized recursive search (DFS with pruning).
 * * @param joltage The current remaining target joltage vector (j).
 * @param availableButtonsMask Bitmask indicating which buttons are still free to use.
 * @param buttons The list of button masks (b_i).
 * @return The minimum number of presses required from this state, or Int.MAX_VALUE if impossible.
 */
fun dfsPart2(joltage: List<Int>, availableButtonsMask: Int, buttons: List<Int>): Int {

    if (joltage.all { it == 0 }) {
        return 0
    }

    val numCounters = joltage.size


    var minMatchingButtonsCount = Int.MAX_VALUE
    var pivotCounterIndex = -1
    var pivotJoltage = 0

    for (i in 0 until numCounters) {
        val target = joltage[i]
        if (target > 0) {

            val matchingCount = buttons.indices.count { j ->
                isButtonAvailable(j, availableButtonsMask) &&
                        (buttons[j] and (1 shl i)) != 0
            }

            if (matchingCount < minMatchingButtonsCount) {
                minMatchingButtonsCount = matchingCount
                pivotCounterIndex = i
                pivotJoltage = target
            }
        }
    }



    if (pivotCounterIndex == -1 || minMatchingButtonsCount == 0) return Int.MAX_VALUE


    val matchingButtons = buttons.indices
        .filter { j ->
            isButtonAvailable(j, availableButtonsMask) &&
                    (buttons[j] and (1 shl pivotCounterIndex)) != 0
        }
        .map { j -> j to buttons[j] }
        .toList()


    var newMask = availableButtonsMask
    matchingButtons.forEach { (index, _) ->
        newMask = newMask and (1 shl index).inv()
    }

    var minResult = Int.MAX_VALUE
    val numMatching = matchingButtons.size


    val counts = MutableList(numMatching) { 0 }
    counts[numMatching - 1] = pivotJoltage



    combinationsLoop@ do {

        val newJoltage = joltage.toMutableList()
        var totalPresses = 0
        var good = true

        for (i in 0 until numMatching) {
            val count = counts[i]
            if (count == 0) continue

            val (_, buttonMask) = matchingButtons[i]
            totalPresses += count


            for (k in 0 until numCounters) {

                if ((buttonMask and (1 shl k)) != 0) {
                    if (newJoltage[k] >= count) {
                        newJoltage[k] -= count
                    } else {

                        good = false


                        break
                    }
                }
            }
            if (!good) break
        }

        if (good) {

            val r = dfsPart2(newJoltage, newMask, buttons)

            if (r != Int.MAX_VALUE) {
                minResult = minOf(minResult, totalPresses + r)
            }
        }

    } while (nextCombination(counts))

    return minResult
}


fun getFewestPressesPart2DFS(machines: ArrayList<ManualLine>): Int {
    var totalPresses = 0

    machines.forEachIndexed { idx, machine ->
        val numButtons = machine.buttons.size

        val initialMask = (1 shl numButtons) - 1


        val minPresses = dfsPart2(machine.joltages, initialMask, machine.buttons)

        if (minPresses != Int.MAX_VALUE) {

            totalPresses += minPresses
        } else {

        }
    }

    return totalPresses
}


fun main() {
    val machines = readFile("./src/main/resources/file.txt")

    val totalPressesPart2 = getFewestPressesPart2DFS(machines)
    println("Total minimum joltage button presses (Part 2): $totalPressesPart2")
}


