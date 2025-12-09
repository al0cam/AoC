package org.example

import java.nio.file.FileSystems
import kotlin.io.path.readText
import kotlin.math.abs

data class Point(var x: Long, var y: Long)

fun readFile(fileName: String): ArrayList<Point> {
    val path = FileSystems.getDefault().getPath(fileName)
    val text = path.readText()
    var points = arrayListOf<Point>()
    text.split("\n").forEach { row ->
        run {
            var split = row.split(",")
            if (split.size == 2) {
                points.add(Point(split[0].toLong(), split[1].toLong()))
            }
        }
    }
    return points
}

fun isPointInPolygon(point: Point, polygon: ArrayList<Point>): Boolean {
    var inside = false
    var j = polygon.size - 1

    for (i in polygon.indices) {
        val pi = polygon[i]
        val pj = polygon[j]

        if ((pi.y > point.y) != (pj.y > point.y) &&
            point.x < (pj.x - pi.x) * (point.y - pi.y) / (pj.y - pi.y) + pi.x
        ) {
            inside = !inside
        }
        j = i
    }

    return inside
}

fun isRectangleValid(p1: Point, p2: Point, polygon: ArrayList<Point>): Boolean {
    val corners = listOf(
        Point(p1.x, p1.y),
        Point(p1.x, p2.y),
        Point(p2.x, p1.y),
        Point(p2.x, p2.y)
    )

    for (corner in corners) {
        if (!isPointInOrOnPolygon(corner, polygon)) {
            return false
        }
    }

    val minX = minOf(p1.x, p2.x)
    val maxX = maxOf(p1.x, p2.x)
    val minY = minOf(p1.y, p2.y)
    val maxY = maxOf(p1.y, p2.y)

    for (i in polygon.indices) {
        val edgeStart = polygon[i]
        val edgeEnd = polygon[(i + 1) % polygon.size]

        if (edgeCrossesRectangle(edgeStart, edgeEnd, minX, maxX, minY, maxY)) {
            return false
        }
    }
    return true
}

fun edgeCrossesRectangle(p1: Point, p2: Point, minX: Long, maxX: Long, minY: Long, maxY: Long): Boolean {
    val p1Inside = p1.x in minX..maxX && p1.y in minY..maxY
    val p2Inside = p2.x in minX..maxX && p2.y in minY..maxY

    if (p1Inside && p2Inside) return false

    val rectEdges = listOf(
        Pair(Point(minX, minY), Point(maxX, minY)),
        Pair(Point(maxX, minY), Point(maxX, maxY)),
        Pair(Point(maxX, maxY), Point(minX, maxY)),
        Pair(Point(minX, maxY), Point(minX, minY))
    )

    for ((r1, r2) in rectEdges) {
        if (segmentsIntersect(p1, p2, r1, r2)) {
            return true
        }
    }

    return false
}

fun segmentsIntersect(p1: Point, p2: Point, p3: Point, p4: Point): Boolean {
    val d1 = direction(p3, p4, p1)
    val d2 = direction(p3, p4, p2)
    val d3 = direction(p1, p2, p3)
    val d4 = direction(p1, p2, p4)

    if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
        ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))
    ) return true
    return false
}

fun direction(p1: Point, p2: Point, p3: Point): Long {
    return (p3.x - p1.x) * (p2.y - p1.y) - (p2.x - p1.x) * (p3.y - p1.y)
}

fun isPointInOrOnPolygon(point: Point, polygon: ArrayList<Point>): Boolean {
    if (point in polygon) return true

    for (i in polygon.indices) {
        val p1 = polygon[i]
        val p2 = polygon[(i + 1) % polygon.size]
        if (isPointOnSegment(point, p1, p2)) return true
    }

    return isPointInPolygon(point, polygon)
}

fun isPointOnSegment(point: Point, p1: Point, p2: Point): Boolean {
    val minX = minOf(p1.x, p2.x)
    val maxX = maxOf(p1.x, p2.x)
    val minY = minOf(p1.y, p2.y)
    val maxY = maxOf(p1.y, p2.y)

    if (point.x !in minX..maxX || point.y !in minY..maxY) return false

    val cross = (point.y - p1.y) * (p2.x - p1.x) - (point.x - p1.x) * (p2.y - p1.y)
    return cross == 0L
}

fun getArea(p1: Point, p2: Point): Long {
    val a = abs(p2.x - p1.x) + 1L
    val b = abs(p2.y - p1.y) + 1L
    return a * b
}

fun getBiggestSquare(points: ArrayList<Point>): Long {
    var biggestSquare = 0L
    var checked = 0

    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            checked++

            val p1 = points[i]
            val p2 = points[j]

            if (p1.x == p2.x || p1.y == p2.y) continue

            if (isRectangleValid(p1, p2, points)) {
                val area = getArea(p1, p2)
                if (area > biggestSquare) {
                    biggestSquare = area
                    println("BigSquare updated: $biggestSquare")
                    println("With points: $p1 and $p2")
                }
            }
        }
    }

    return biggestSquare
}

fun main() {
    val points = readFile("./src/main/resources/file.txt")

    val bigSquare = getBiggestSquare(points)
    println("BigBig: $bigSquare")
}


