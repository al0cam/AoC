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

fun getConnectionResult(junctionBoxes: List<Box>): Long {
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
    var lastConnectionIdx = -1
    for (i in connections.indices) {
        val connection = connections[i]
        val wasUnited = uf.union(connection.fromIdx, connection.toIdx)

        if (wasUnited) {

            val numCircuits = uf.getCircuitSizes(n).size

            if (numCircuits == 1) {
                lastConnectionIdx = i
                println("All boxes connected after processing ${i + 1} connections")
                println("Last connection: Box ${connection.fromIdx} <-> Box ${connection.toIdx}")
                println("Distance: ${String.format("%.2f", connection.distance)}")
                break
            }

            if ((i + 1) % 100 == 0) {
                println("Processed ${i + 1} connections, $numCircuits circuits remaining...")
            }
        }
    }

    val lastConnection = connections[lastConnectionIdx]
    val x1 = junctionBoxes[lastConnection.fromIdx].x.toLong()
    val x2 = junctionBoxes[lastConnection.toIdx].x.toLong()
    val result = x1 * x2

    println("\nLast connection X coordinates: $x1 and $x2")

    return result
}

fun main() {
    val junctionBoxes = readFile("./src/main/resources/file.txt")
    println("Loaded ${junctionBoxes.size} junction boxes\n")

    val result = getConnectionResult(junctionBoxes)
    println(" RESULT: $result")
}

// 50760
// 3206508875