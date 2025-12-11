package org.example

import java.nio.file.FileSystems
import kotlin.io.path.readText

data class Node(var name: String, var outs: List<String>, var outNodes: ArrayList<Node>)

lateinit var YOU: Node
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

            if (source.lowercase() == "you") {
                YOU = node
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

fun getPaths(node: Node): Int {
    if (node.outNodes.size == 0) return 1
    else {
        println("Node: $node")
        return node.outNodes.sumOf { getPaths(it) }
    }
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
    var result = getPaths(YOU)
    println("Paths: $result")
}

// 599