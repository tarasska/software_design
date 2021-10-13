package rendering.impl

import rendering.RenderingApi
import rendering.model.Point
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D

class AwtRenderer(
    private val graphics2d: Graphics2D,
    private val height: Long,
    private val width: Long
) : RenderingApi<Double> {
    override fun getDrawingAreaWidth(): Long = width

    override fun getDrawingAreaHeight(): Long = height

    override fun drawCircle(point: Point<Double>, radius: Double) {
        graphics2d.fill(
            Ellipse2D.Double(
                point.getX() - radius, point.getY() - radius,
                2 * radius, 2 * radius
            )
        )
    }

    override fun drawLine(from: Point<Double>, to: Point<Double>) {
        graphics2d.draw(
            Line2D.Double(from.getX(), from.getY(), to.getX(), to.getY())
        )
    }
}