package graph.impl

import graph.Graph
import rendering.RenderingApi

class AdjacencyMatrixGraph(
    renderer: RenderingApi<Double>,
    private val matrix: Array<Array<Boolean>>
): Graph(renderer, matrix.size) {
    override fun renderGraph() {
        (0..matrix.size).forEach{ super.renderNode(it) }
        matrix.forEachIndexed { fromId, neighbours ->
            neighbours.filter { it }.forEachIndexed { toId, _ ->  super.renderEdge(fromId, toId)}
        }
    }
}