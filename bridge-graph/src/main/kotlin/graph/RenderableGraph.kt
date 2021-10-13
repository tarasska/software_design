package graph

interface RenderableGraph {
    fun renderNode(id: Int)
    fun renderEdge(fromId: Int, toId: Int)
    fun renderGraph()
}