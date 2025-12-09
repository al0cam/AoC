package org.example

import java.nio.file.FileSystems
import kotlin.io.path.readText
import kotlin.math.abs

data class Point(var x: Double, var y: Double)

fun readFile(fileName: String): ArrayList<Point> {
    val path = FileSystems.getDefault().getPath(fileName)
    val text = path.readText()

    var points = arrayListOf<Point>()

    text.split("\n").forEach { row ->
        run {
            var spit = row.split(",")
            points.add(Point(spit[0].toDouble(), spit[1].toDouble()))
        }
    }

    return points
}

fun getArea(p1: Point, p2: Point): Double {
    var a = abs(p2.x - p1.x) + 1
    var b = abs(p2.y - p1.y) + 1


    println("a: $a | b: $b")

    return a * b
}

fun getBiggestSquare(points: ArrayList<Point>): Double {
    var biggestSquare = 0.00

    for (p1 in points) {
        for (p2 in points) {
            if (p1 == p2) continue
            else if (p1.x == p2.x || p1.y == p2.y) continue
            var square = getArea(p1, p2)
            if (square > biggestSquare)
                biggestSquare = square
        }
    }


    return biggestSquare
}

fun main() {
    var points = readFile("./src/main/resources/file.txt")
    var bigSquare = getBiggestSquare(points)

    println("Area of 2,5 and 11,1: ${getArea(Point(2.00, 5.00), Point(11.00, 1.00))}")

    println("Big Dog: ${bigSquare.toLong()}")
}

// 4748769124
