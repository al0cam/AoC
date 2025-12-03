
package org.example

import java.nio.file.FileSystems
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.math.max
import kotlin.math.pow

fun readFile(fileName: String): ArrayList<String> {
    val strings: ArrayList<String> = ArrayList<String>()
    val path: Path = FileSystems.getDefault().getPath(fileName);
    val text = path.readText()

    text.split("\n").forEach { line -> strings.add(line.trim()) }
    return strings;
}


//In 987654321111111, you can make the largest joltage possible, 98, by turning on the first two batteries.
//In 811111111111119, you can make the largest joltage possible by turning on the batteries labeled 8 and 9, producing 89 jolts.
//In 234234234234278, you can make 78 by turning on the last two batteries (marked 7 and 8).
//In 818181911112111, the largest joltage you can produce is 92.
//
//The total output joltage is the sum of the maximum joltage from each bank, so in this example, the total output joltage is 98 + 89 + 78 + 92 = 357.

data class Battery(var value: Double, var index: Int){
    companion object{
        val EMPTY = Battery(value = 0.00, index = 0 )
    }
}

fun getMaxJolt(battery: String): Double{
    var maxJolt: Double = 0.00
    println("bat: "+ battery)
    var batterySizeSequence = battery.map { it.digitToInt().toDouble() }
    val batteries = Array<Battery>(12) {Battery.EMPTY.copy()}

    for (batIndex in 0..(batteries.size-1)){
        val startingIndex = if (batIndex == 0) batIndex else batteries[batIndex-1].index
        val endSpacer = 11 -batIndex
        val endIndex = batterySizeSequence.size - endSpacer -1

//        println("Bat index: $batIndex | startIndex: $startingIndex | endIndex: $endIndex")
//        println("   InnerLoop")
        for (i in startingIndex..endIndex) {
//            print("      Index: $i | value: ${batterySizeSequence[i]} # ")
            if (batterySizeSequence[i] > batteries[batIndex].value)
            {
//                println("      Setting value: ${batterySizeSequence[i]}")
                batteries[batIndex].value = batterySizeSequence[i]
                batteries[batIndex].index = i+1
            }
        }
    }
    // find first biggest value which cant be at the end of an array
    // then start searching from that for second value

//    batteries.forEach { println("value: ${it.value} | index: ${it.index}")}

    batteries.forEachIndexed { index, battery -> battery.value = battery.value*10.00.pow((batteries.size-index-1).toDouble()) }
    maxJolt = batteries.sumOf { it.value }
    println()
    println("MaxJolt: "+ maxJolt.toLong())
    println()

    return maxJolt
}



fun main() {
    val batteries = readFile("./src/main/resources/file.txt")
    var joltSum: Double = 0.00

    batteries.forEach { joltSum+=getMaxJolt(it) }

    println("JoltSum: "+joltSum.toLong())
}

//17346 done
// 172981362045136 done