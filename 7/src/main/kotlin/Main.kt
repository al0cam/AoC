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
        drawLine(row + 1, col - 1)
        drawLine(row + 1, col + 1)
    } else if (diagram[row][col] == '.') {
        var string = diagram[row].replaceRange(col, col + 1, "|")
        diagram[row] = string
        drawLine(row + 1, col) // draw until the end if possible
    }
}

fun main() {
    readFile("./src/main/resources/file.txt")

    println("ROWS: $ROWS | COLS: $COLS")

    drawLine(1, diagram[0].indexOf("S"))

    diagram.forEach { println(it) }

    println("Result is: $splits")
}


// 1550