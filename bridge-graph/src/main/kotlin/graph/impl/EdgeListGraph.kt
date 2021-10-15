package graph.impl

import graph.Graph
import rendering.RenderingApi

class EdgeListGraph(
    renderer: RenderingApi<Double>,
    private val edgeList: List<List<Int>>
): Graph(renderer, edgeList.size) {
    override fun renderGraph() {
        (0..edgeList.size).forEach{ super.renderNode(it) }
        edgeList.forEachIndexed { fromId, neighbours ->
            neighbours.forEach { toId ->  super.renderEdge(fromId, toId)}
        }
    }
}