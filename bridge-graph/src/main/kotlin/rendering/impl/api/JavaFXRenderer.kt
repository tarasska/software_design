package rendering.impl.api

import javafx.scene.canvas.Canvas
import rendering.RenderingApi
import rendering.model.Point

class JavaFXRenderer(
    private val canvas: Canvas,
    private val height: Long,
    private val width: Long
) : RenderingApi<Double> {
    override fun getDrawingAreaWidth(): Long = height

    override fun getDrawingAreaHeight(): Long = width

    override fun drawCircle(point: Point<Double>, radius: Double) {
        canvas.graphicsContext2D.fillOval(
            point.getX() - radius, point.getY() - radius,
            2 * radius, 2 * radius
        )
    }

    override fun drawLine(from: Point<Double>, to: Point<Double>) {
        canvas.graphicsContext2D.strokeLine(from.getX(), from.getY(), to.getX(), to.getY())
    }
}