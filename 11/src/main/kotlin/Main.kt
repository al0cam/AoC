package org.example

import java.nio.file.FileSystems
import kotlin.io.path.readText

data class Node(var name: String, var outs: List<String>, var outNodes: ArrayList<Node>)

lateinit var SVR: Node
lateinit var OUTNODES: ArrayList<Node>


fun readFile(fileName: String): ArrayList<Node> {
    val path = FileSystems.getDefault().getPath(fileName)
    val text = path.readText()
    val nodes = arrayListOf<Node>()
    val pattern = """(?<source>\w+): (?<outputs>[A-Za-z ]+)""".toRegex()
    OUTNODES = arrayListOf()

    text.split("\n").forEach { row ->
        run {
            var match = pattern.matchEntire(row)
            var source = match!!.groups["source"]!!.value
            var outs = match!!.groups["outputs"]!!.value.split(" ")

            var node = Node(source, outs, arrayListOf())

            if (source.lowercase() == "svr") {
                SVR = node
                println("SVR node: $SVR")
                nodes.add(node)
            } else if (node.outs.contains("out")) {
                OUTNODES.add(node)
                nodes.add(node)
            } else
                nodes.add(node)
        }
    }

    return nodes
}

fun connectNodes(nodes: ArrayList<Node>) {
    for (n1 in nodes) {
        for (n2 in nodes) {
            if (n1 == n2) continue
            else if (n1.outs.contains(n2.name)) {
                n1.outNodes.add(n2)
            }
        }
    }
}

fun countPathsEfficient(
    node: Node,
    state: Int,
    pathNodes: Set<String>,
    memo: MutableMap<Pair<String, Int>, Long>
): Long {
    if (pathNodes.contains(node.name)) {
        return 0
    }

    var newState = state
    if (node.name == "fft" && state == 0) newState = 1
    if (node.name == "fft" && state == 2) newState = 3
    if (node.name == "dac" && state == 0) newState = 2
    if (node.name == "dac" && state == 1) newState = 3

    val key = Pair(node.name, newState)
    if (key in memo) {
        return memo[key]!!
    }

    if (node.outs.contains("out")) {
        val result = if (newState == 3) 1L else 0L
        memo[key] = result
        return result
    }

    val newPathNodes = pathNodes + node.name
    var totalPaths = 0L

    for (nextNode in node.outNodes) {
        totalPaths += countPathsEfficient(nextNode, newState, newPathNodes, memo)
    }

    memo[key] = totalPaths
    return totalPaths
}

fun main() {
    val nodes = readFile("./src/main/resources/file.txt")


//    println("Source: $YOU")
//    println("End: $OUTNODES")
//    nodes.forEach { println(it) }
    println("Connecting nodes")
    connectNodes(nodes)

//    println("Source: $YOU")
//    println("End: $OUTNODES")
//    nodes.forEach { println(it) }

    println("Getting path")
//    var result = getPaths(SVR, mutableListOf(), mutableSetOf())
//    println("Paths: $result")
    val memo = mutableMapOf<Pair<String, Int>, Long>()
    val result = countPathsEfficient(SVR, 0, setOf(), memo)

    println("\nMemoization cache size: ${memo.size}")
    println("paths visiting both dac and fft: $result")
}

// 599