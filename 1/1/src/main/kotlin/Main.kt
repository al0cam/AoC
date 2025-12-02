package org.example

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.readText
import kotlin.math.abs

class Knob {
    public var knobPosition = 50;

    public fun goLeft(step: Int){

    }
}

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
//    lines.forEach { line -> println(line)}

    var knobPosition = 50
    var zeroes = 0
    println()
    for (line in lines){
        println("Old knob position: "+knobPosition)
        println("Old zeroes: "+zeroes)

        println("Line: "+line)

        var oldKnobPosition = knobPosition

        var turnKnob = turnKnob(line)!!

        knobPosition += turnKnob

        var wholeSteps = abs(knobPosition/100)
        knobPosition = if (knobPosition < 0) {
            if (oldKnobPosition != 0)
                zeroes+=1
             100+(knobPosition%100)
        } else knobPosition%100
        knobPosition = knobPosition%100

        zeroes += if (knobPosition == 0 && wholeSteps == 0) 1 else wholeSteps

        println("New Knob position: "+knobPosition)
        println("New zeroes: "+zeroes)
        println()
    }

    println(zeroes)
}


//The dial starts by pointing at 50.
//The dial is rotated L68 to point at 82; during this rotation, it points at 0 once.
    //The dial is rotated L30 to point at 52.
//The dial is rotated R48 to point at 0.
//The dial is rotated L5 to point at 95. problem when going from 0 to something
//The dial is rotated R60 to point at 55; during this rotation, it points at 0 once.
//The dial is rotated L55 to point at 0.
//The dial is rotated L1 to point at 99.
//The dial is rotated L99 to point at 0.
//The dial is rotated R14 to point at 14.
//The dial is rotated L82 to point at 32; during this rotation, it points at 0 once.

// 7325 not
//6999 not
// 8819 not
// 6401 not
// 6749 nope