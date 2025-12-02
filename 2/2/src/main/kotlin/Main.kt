package org.example

import java.nio.file.FileSystems
import java.nio.file.Path
import kotlin.io.path.readText

fun readFile(fileName: String): ArrayList<String> {
    val strings: ArrayList<String> = ArrayList<String>()
    val path: Path = FileSystems.getDefault().getPath(fileName);
    val text = path.readText()

    text.split(",").forEach { line -> strings.add(line.trim().removeSurrounding("\n")) }
    return strings;
}

fun getInvalidIdList(range: String): ArrayList<Long>{
   val invalidIdList: ArrayList<Long> = ArrayList<Long>()
    val (listStart, listEnd) = range.split("-").map { it.toLong() }

    for(i in listStart..listEnd) {
        val iString = i.toString()
        val iLength = iString.length

        if (iLength%2 != 0)
            continue

        if (iString.endsWith(iString.substring(0, iLength/2)))
            invalidIdList.add(i)
    }


    return invalidIdList
}

fun main() {
    val ranges = readFile("./src/main/resources/file.txt")

    var sum: Long = 0
    ranges.forEach { sum+=getInvalidIdList(it).sum()}


    println("Sum: "+sum)
}