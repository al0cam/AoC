import java.nio.file.FileSystems
import kotlin.io.path.readText


var diagram: MutableList<String> = mutableListOf()
var ROWS = 0
var COLS = 0

fun readFile(fileName: String) {
    val path = FileSystems.getDefault().getPath(fileName);
    val text = path.readText()
    text.split("\n").forEach { diagram.add(it) }

    ROWS = diagram.size - 1 // for 0 indexing
    COLS = diagram[0].length - 1 // for 0 indexing
}


var splits = 0
fun drawLine(row: Int, col: Int) {
    if (row > ROWS || col > COLS) return
    if (diagram[row][col] == '^') {
        splits += 1
        drawLine(row, col - 1)
        drawLine(row, col + 1)
        drawLine(row + 1, col - 1)
        drawLine(row + 1, col + 1)
    } else if (diagram[row][col] == '.') {
        var string = diagram[row].replaceRange(col, col + 1, "|")
        diagram[row] = string
        drawLine(row + 1, col) // draw until the end if possible
    }
}

val memo = mutableMapOf<Pair<Int, Int>, Long>()

fun countTimelines(row: Int, col: Int): Long {
    if (row >= ROWS || col < 0 || col >= COLS) return 1L

    val pos = Pair(row, col)
    if (pos in memo) return memo[pos]!!
    val result = when (diagram[row][col]) {
        '^' -> {
            countTimelines(row + 1, col - 1) + countTimelines(row + 1, col + 1)
        }
        '|' -> {
            countTimelines(row + 1, col)
        }
        else -> 0L
    }

    memo[pos] = result
    return result
}

fun main() {
    readFile("./src/main/resources/file.txt")

//    println("ROWS: $ROWS | COLS: $COLS")

    drawLine(1, diagram[0].indexOf("S"))

    diagram.forEach { println(it) }

    var res = countTimelines(1, diagram[0].indexOf("S"))

    println("Result is: $splits")
    println("Res is: $res")
}


// 1550
// 9897897326778