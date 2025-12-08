import java.nio.file.FileSystems
import kotlin.io.path.readText
import kotlin.math.sqrt

data class Box(val x: Double, val y: Double, val z: Double)


data class Connection(val fromIdx: Int, val toIdx: Int, val distance: Double)

fun readFile(fileName: String): ArrayList<Box> {
    val path = FileSystems.getDefault().getPath(fileName)
    val text = path.readText()
    val junctionBoxes = arrayListOf<Box>()
    text.split("\n").forEach { string ->
        if (string.isNotBlank()) {
            val (x, y, z) = string.split(",")
            junctionBoxes.add(Box(x.toDouble(), y.toDouble(), z.toDouble()))
        }
    }
    return junctionBoxes
}

fun findDistanceInSpace(from: Box, to: Box): Double {
    val dx = from.x - to.x
    val dy = from.y - to.y
    val dz = from.z - to.z
    return sqrt(dx * dx + dy * dy + dz * dz)
}

class UnionFind(size: Int) {
    private val parent = IntArray(size) { it }
    private val rank = IntArray(size) { 0 }

    fun find(x: Int): Int {
        if (parent[x] != x) {
            parent[x] = find(parent[x])
        }
        return parent[x]
    }

    fun union(x: Int, y: Int): Boolean {
        val rootX = find(x)
        val rootY = find(y)

        if (rootX == rootY) return false


        when {
            rank[rootX] < rank[rootY] -> parent[rootX] = rootY
            rank[rootX] > rank[rootY] -> parent[rootY] = rootX
            else -> {
                parent[rootY] = rootX
                rank[rootX]++
            }
        }
        return true
    }

    fun getCircuitSizes(size: Int): List<Int> {
        val circuits = mutableMapOf<Int, Int>()
        for (i in 0 until size) {
            val root = find(i)
            circuits[root] = circuits.getOrDefault(root, 0) + 1
        }
        return circuits.values.toList()
    }
}

fun getConnectionResult(junctionBoxes: List<Box>): Int {
    val n = junctionBoxes.size
    println("Processing $n boxes...")

    val connections = ArrayList<Connection>(n * (n - 1) / 2)

    for (i in 0 until n) {
        for (j in i + 1 until n) {
            connections.add(Connection(i, j, findDistanceInSpace(junctionBoxes[i], junctionBoxes[j])))
        }
        if (i % 100 == 0) println("Generated connections for box $i/$n")
    }

    println("Sorting ${connections.size} connections...")
    connections.sortBy { it.distance }

    println("Top 10 shortest distances:")
    connections.take(10).forEach {
        println("  ${String.format("%.2f", it.distance)}")
    }

    val uf = UnionFind(n)

    val K = 1000
    val connectionsToProcess = minOf(connections.size, K)

    println("\nProcessing first $connectionsToProcess connections...")

    for (i in 0 until connectionsToProcess) {
        val connection = connections[i]
        uf.union(connection.fromIdx, connection.toIdx)

        if ((i + 1) % 100 == 0) {
            println("Processed ${i + 1} connections...")
        }
    }


    val circuitSizes = uf.getCircuitSizes(n).sortedDescending()

    println("\nAll circuit sizes (top 20): ${circuitSizes.take(20)}")
    println("Number of circuits: ${circuitSizes.size}")
    println("Top 3 circuits: ${circuitSizes.take(3)}")


    val top3 = circuitSizes.take(3)
    val result = top3[0] * top3[1] * top3[2]

    println("Result: ${top3[0]} × ${top3[1]} × ${top3[2]} = $result")

    return result
}

fun main() {
    val junctionBoxes = readFile("./src/main/resources/file.txt")
    println("Loaded ${junctionBoxes.size} junction boxes")

    val result = getConnectionResult(junctionBoxes)
    println("\nFinal result: $result")
}


