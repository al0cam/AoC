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

fun main() {
    val machines = readFile("./src/main/resources/file.txt")
    val totalPresses = getFewestPresses(machines)
    println("Total minimum button presses: $totalPresses")
}


