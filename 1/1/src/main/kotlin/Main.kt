package org.example

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.readText


fun readFile(fileName: String): ArrayList<String> {
   val strings: ArrayList<String> = ArrayList<String>()
   val path: Path = FileSystems.getDefault().getPath(fileName);
    val text = path.readText()

    text.split("\n").forEach { line -> strings.add(line) }
    return strings;
}

fun turnKnob(instruction: String): Int?{
    var result: Int? = null
    if (instruction.contains("L"))
        result = Regex("""\d+""").find(instruction)?.value?.toInt()?.unaryMinus() ?: 0
    else if (instruction.contains("R"))
        result = Regex("""\d+""").find(instruction)?.value?.toInt() ?: 0

    return result
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val lines = readFile("./src/main/resources/file.txt")
    lines.forEach { line -> println(line)}

    var result = 50
    var zeroes = 0
    for (line in lines){
        result = (result + turnKnob(line)!!)%100
        zeroes += if (result == 0) 1 else 0
    }

    println(zeroes)
}