import graph.Graph
import graph.impl.AdjacencyMatrixGraph
import graph.impl.EdgeListGraph
import rendering.RenderingApi
import rendering.impl.app.AwtFrame
import rendering.impl.app.JavaFXApplicationRunner
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    if (args[0] == "--help") {
        println("Usage: main <render_type> renderer-height renderer-width <graph_type> <graph_file>")
    }
    val height = args[1].toInt()
    val width = args[2].toInt()
    val app: Runnable = when (args[0]) {
        "-awt" -> AwtFrame(height, width, graphMapper(args[3], File(args[4])))
        "-javafx" -> JavaFXApplicationRunner(height, width, graphMapper(args[3], File(args[4])))
        else -> error("Unsupported renderer type")
    }
    app.run()
}

private fun graphMapper(graphType: String, graphFile: File): (RenderingApi<Double>) -> Graph {
    return { api ->
        when (graphType) {
            "-elist" -> EdgeListGraph(api, readEList(graphFile))
            "-amatrix" -> AdjacencyMatrixGraph(api, readMatrix(graphFile))
            else -> error("Incorrect graph type")
        }
    }
}

private fun readEList(graphFile: File): List<List<Int>> {
    with(Scanner(graphFile.inputStream())) {
        val size = nextInt()
        val graph: MutableList<MutableList<Int>> = ArrayList()
        for (i in 0 until size) {
            val curSize = nextInt()
            graph.add(ArrayList())
            for (j in 0 until curSize) {
                graph[i].add(nextInt())
            }
        }
        return graph
    }
}

private fun readMatrix(graphFile: File): Array<Array<Boolean>> {
    with(Scanner(graphFile.inputStream())) {
        val size = nextInt()
        val res = Array(size) { Array(size) { false } }
        for (i in 0 until size) {
            for (j in 0 until size) {
                res[i][j] = nextInt() == 1
            }
        }
        return res
    }
}