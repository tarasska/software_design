package graph

import rendering.RenderingApi
import rendering.model.DoublePoint
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

abstract class Graph(
    private val renderer: RenderingApi<Double>,
    private val size: Int
): RenderableGraph {

    protected val canvasCenter: DoublePoint = DoublePoint(
        renderer.getDrawingAreaWidth().toDouble() / 2,
        renderer.getDrawingAreaHeight().toDouble() / 2
    )
    protected val maxCircleRadius: Double = min(
        renderer.getDrawingAreaWidth(), renderer.getDrawingAreaHeight()
    ) * WORK_AREA_PROPORTION / 8
    protected val nodeRadius = min(
        renderer.getDrawingAreaWidth(),
        renderer.getDrawingAreaHeight()
    ) * (1 - WORK_AREA_PROPORTION) / 4

    /**
     *  Places node in a circle.
     */
    protected fun positionById(id: Int): DoublePoint {
        val turn = 2 * PI / size * id
        return canvasCenter + DoublePoint(cos(turn), sin(turn)) * maxCircleRadius
    }

    override fun renderNode(id: Int) {
        val pos = positionById(id)
        renderer.drawCircle(pos, nodeRadius)
        renderer.drawLabel(pos, "$id")
    }

    override fun renderEdge(fromId: Int, toId: Int) {
        renderer.drawLine(positionById(fromId), positionById(toId))
    }

    companion object {
        private const val WORK_AREA_PROPORTION: Double = 0.9
    }
}