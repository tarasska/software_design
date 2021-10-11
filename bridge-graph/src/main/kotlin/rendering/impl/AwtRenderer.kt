package rendering.impl

import rendering.RenderingApi
import rendering.model.Point
import java.awt.Graphics2D

class AwtRenderer<T>(
    private val graphics2d: Graphics2D
) : RenderingApi<T> {
    override fun getDrawingAreaWidth(): Long {
        TODO("Not yet implemented")
    }

    override fun getDrawingAreaHeight(): Long {
        TODO("Not yet implemented")
    }

    override fun drawCircle(point: Point<T>, radius: T) {
        TODO("Not yet implemented")
    }

    override fun drawLine(from: Point<T>, to: Point<T>) {
        TODO("Not yet implemented")
    }
}