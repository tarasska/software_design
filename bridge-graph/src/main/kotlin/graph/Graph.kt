package graph

import rendering.RenderingApi

abstract class Graph<T>(
    private val renderer: RenderingApi<Double>,
    private val size: Int
): RenderableGraph {
    class Node<T>() {
        private val children: MutableList<T> = ArrayList()

        constructor(src: Collection<T>): this() {
            this.children.addAll(src)
        }
    }


}