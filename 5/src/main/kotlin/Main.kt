package org.example

import java.nio.file.FileSystems
import java.nio.file.Path
import java.util.HashMap
import java.util.TreeSet
import kotlin.io.path.readText

data class Range (var start: Long, var end: Long)

fun readFile(fileName: String): Pair<ArrayList<Range>, ArrayList<Long>> {
    val ranges = ArrayList<Range>()
    val ids = ArrayList<Long>()
    val path = FileSystems.getDefault().getPath(fileName);
    val text = path.readText()

    text.split("\n").forEach {
        run {
            if (it.contains("-"))
            {
                var splitRange = it.split("-")
              ranges.add(Range(splitRange[0].toLong(), splitRange[1].toLong()))
            }
            else if (it.contains("""\d+""".toRegex())) {
                ids.add(it.toLong())
            }
        }
    }
    return Pair(ranges, ids)
}

fun consolidateRangesSorted(rangeList: MutableList<Range>) {
    if (rangeList.size <= 1) return
    rangeList.sortBy { it.start }
    val consolidated = mutableListOf<Range>()
    consolidated.add(rangeList[0])

    for (i in 1 until rangeList.size) {
        val currentRange = rangeList[i]
        val lastConsolidated = consolidated.last()
        if (currentRange.start <= lastConsolidated.end) {
            lastConsolidated.end = maxOf(lastConsolidated.end, currentRange.end)
        }
        else {
            consolidated.add(currentRange)
        }
    }

    rangeList.clear()
    rangeList.addAll(consolidated)
}

fun getFreshItemCount(rangeList: ArrayList<Range>, idList: ArrayList<Long>): Long {
    var freshItems = 0L

    val maxRangeEnding: Long = rangeList.maxOf { it.end }
    println("Max ending: $maxRangeEnding")
    consolidateRangesSorted(rangeList)

    for (range in rangeList) {
        freshItems+= range.end - range.start + 1
    }
    return freshItems
}

fun main() {
    val (rangeList, idList) = readFile("./src/main/resources/file.txt")

//    rangeList.forEach { println(it)}
//    idList.forEach { println(it)}

   var freshItemCount =  getFreshItemCount(rangeList, idList)

    println("Fresh item count: $freshItemCount")
}

// 558
// 344813017450467