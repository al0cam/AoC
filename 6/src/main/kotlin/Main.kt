package org.example

import java.nio.file.FileSystems
import kotlin.io.path.readText

data class HomeWorkTask(var numbers: MutableList<Long>, var opSign: String)

fun readFile(fileName: String): ArrayList<HomeWorkTask> {
    val path = FileSystems.getDefault().getPath(fileName);
    val text = path.readText()
    var homeWorkTasks = ArrayList<HomeWorkTask>()

    text.split("\n").forEach {
        run {
            var splitNumbers = it.trim().split("""\s+""".toRegex())
//            println("SplitNumber: $splitNumbers")
            if (it.contains("""\d+""".toRegex())) {
                // create homework tasks for each item
                splitNumbers.forEachIndexed { i, number ->
                    if (homeWorkTasks.getOrNull(i) == null)
                        homeWorkTasks.add(
                            HomeWorkTask(
                                arrayListOf(number.toLong()),
                                ""
                            )
                        )
                    else
                        homeWorkTasks[i].numbers.add(number.toLong())
                }
            } else if (it.contains("""[+*]""".toRegex())) {
                var splitSigns = it.trim().split("\\s+".toRegex())
//                println("\nSplitSigns: $splitSigns")
                splitSigns.forEachIndexed { index, sign -> homeWorkTasks[index].opSign = sign }
            }
        }
    }
    return homeWorkTasks
}

fun doMath(homeWorkTasks: MutableList<HomeWorkTask>): Long {
    var result = 0L

    homeWorkTasks.forEach {
        if (it.opSign == "*")
            result+= it.numbers.reduce { acc, num -> acc*num }
        else if (it.opSign == "+")
            result+= it.numbers.reduce { acc, num -> acc+num }
    }
    return result
}

fun main() {
    val homeWorkTasks = readFile("./src/main/resources/file.txt")

    homeWorkTasks.forEach { println("Numbers: ${it.numbers} | Sign: ${it.opSign}") }

    val result = doMath(homeWorkTasks)
    println("Result is: $result")
}


// 8108520669952
