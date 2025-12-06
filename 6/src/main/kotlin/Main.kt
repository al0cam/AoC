package org.example

import java.nio.file.FileSystems
import kotlin.io.path.readText

data class HomeWorkTask(
    var numbers: MutableList<Long> = mutableListOf(),
    var opSign: String = ""
) {
    companion object {
        val EMPTY = HomeWorkTask()
    }
}

fun readFile(fileName: String): ArrayList<HomeWorkTask> {
    val path = FileSystems.getDefault().getPath(fileName);
    val text = path.readText()
    var homeWorkTasks = ArrayList<HomeWorkTask>()

    var columns = ArrayList<String>()

    text.split("\n").forEach {
        run {
            // split into chars and then treat every char as important
            // after consolidating data into object
            // delete empty data
            it.split("").forEachIndexed { index, char ->
                if (columns.getOrNull(index) == null)
                    columns.add(char)
                else columns[index] += char
            }
        }
    }


    var i = -1
    columns.forEach { column ->
        println("Column: $column | index: $i")
        run {
            if (column.isBlank()) {
                println("adding task")
                homeWorkTasks.add(HomeWorkTask(mutableListOf(), ""))
                i += 1
            } else if (column.contains("[+*]".toRegex())) {
                var (number, sign) = "(\\d+)\\s*([*+])".toRegex().find(column)!!.destructured
                homeWorkTasks[i].numbers.add(number.toLong())
                homeWorkTasks[i].opSign = sign
            } else
                homeWorkTasks[i].numbers.add(column.trim().toLong())
        }
    }

    return homeWorkTasks
}

fun doMath(homeWorkTasks: MutableList<HomeWorkTask>): Long {
    var result = 0L

    homeWorkTasks.forEach {
        if (it.opSign == "*")
            result += it.numbers.reduce { acc, num -> acc * num }
        else if (it.opSign == "+")
            result += it.numbers.reduce { acc, num -> acc + num.toLong() }
    }
    return result
}

fun main() {
    val homeWorkTasks = readFile("./src/main/resources/file.txt")

    homeWorkTasks.forEach {
        run {
            it.numbers.forEach { num -> print(" $num ") }
            println("| Sign: ${it.opSign}")
        }
    }

    val result = doMath(homeWorkTasks)
    println("Result is: $result")
}


// 8108520669952
// 11708563470209