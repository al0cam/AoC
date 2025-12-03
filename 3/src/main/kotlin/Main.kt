package org.example

import java.nio.file.FileSystems
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.math.max

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

fun getMaxJolt(battery: String): Long{
    var maxJolt: Long = 0
    println("bat: "+ battery)
    var batterySizeSequence = battery.map { it.digitToInt().toLong() }
    var highestNumber = 0L
    var highestNumberIndex = 0
    var secondHighestNumber = 0L

    for (i in 0..(batterySizeSequence.size-2)) {
        if (batterySizeSequence[i] > highestNumber)
        {
            highestNumber = batterySizeSequence[i]
            highestNumberIndex = i
        }
    }

    for (i in highestNumberIndex+1..batterySizeSequence.size-1){
        if (batterySizeSequence[i] > secondHighestNumber)
            secondHighestNumber = batterySizeSequence[i]
    }

    // find first biggest value which cant be at the end of an array
    // then start searching from that for second value

    println("Max: "+highestNumber)
    println("2nd Max: "+secondHighestNumber)

    maxJolt = highestNumber*10+secondHighestNumber
    println("MaxJolt: "+ maxJolt)
    println()

    return maxJolt
}



fun main() {
    val batteries = readFile("./src/main/resources/file.txt")
    var joltSum: Long = 0

    batteries.forEach { joltSum+=getMaxJolt(it) }

    println("JoltSum: "+joltSum)
}

//17346 done