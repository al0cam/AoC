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


fun getFreshItemCount(rangeList: ArrayList<Range>, idList: ArrayList<Long>): Long {
    var freshItems = 0L

    val maxRangeEnding: Long = rangeList.maxOf { it.end }
    println("Max ending: $maxRangeEnding")

    for (id in idList) {
        if (id >= maxRangeEnding)
            continue
        for (range in rangeList) {
            if (range.start <= id && range.end >= id  ) {
                freshItems+=1
                break
            }
        }
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