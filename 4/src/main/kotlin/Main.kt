package org.example

import java.nio.file.FileSystems
import java.nio.file.Path
import kotlin.io.path.readText

fun readFile(fileName: String): ArrayList<String> {
    val strings: ArrayList<String> = ArrayList<String>()
    val path: Path = FileSystems.getDefault().getPath(fileName);
    val text = path.readText()

    text.split("\n").forEach { line -> strings.add(".${line.trim()}.") }
    return strings;
}

fun getRemovableRolls(matrixRows: ArrayList<String>): Int{
    val rollSign = '@'
    var movableRolls = 0

    val rowStart = 1 // first row is dots
    val rowEnd = matrixRows.size-2 // last row is dots


    var rollRemoved = false
    var pass = 0
    do {
        pass+=1
        println("Pass: $pass | Movable Rolls: $movableRolls")
        rollRemoved = false
        for (row in rowStart..rowEnd) {
            val colStart = 1 // first is dots
            val colEnd = matrixRows[row].length-2 // last col is dots

            for (col in colStart..colEnd) {
                if (matrixRows[row][col].toChar() != rollSign)
                    continue
                //println("row: $row | col: $col")
                var sequence = ""
                // from current el get n-1 row, n row and n+1 row
                //println("     Sequence before: $sequence")
                sequence += matrixRows[row-1].substring(col-1, col+2) // second index is exclusive < instead of <= ????
                //println("     Sequence n-1: $sequence")
                sequence += matrixRows[row].substring(col-1, col+2)
                //println("     Sequence n: $sequence")
                sequence += matrixRows[row+1].substring(col-1, col+2)
                //println("     Sequence n+1: $sequence")


                if (sequence.count { it == rollSign } <= 4) {
                    var newString = matrixRows[row].replaceRange(col, col+1,"X")
                    matrixRows[row] = newString
                    rollRemoved = true
                    movableRolls+=1
//                    println("Moving rolls increased: $movableRolls")
                }
            }
        }
    } while (rollRemoved != false)


    return movableRolls
}


fun main() {

    val matrixRows = readFile("./src/main/resources/file.txt")
    val firstAndLastRow = ".".repeat(matrixRows[0].length)
    matrixRows.addFirst(firstAndLastRow)
    matrixRows.addLast(firstAndLastRow)

    matrixRows.forEach { println("$it") }

    println()
    println("Movable rolls: ${getRemovableRolls(matrixRows)}")
}


// 1523
// 9290