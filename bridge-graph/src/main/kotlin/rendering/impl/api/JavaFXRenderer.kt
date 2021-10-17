package rendering.impl.api

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
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

    override fun drawLabel(point: Point<Double>, label: String) {
        canvas.graphicsContext2D.fill = Color.GREEN
        canvas.graphicsContext2D.fillText(label, point.getX(), point.getY())
        canvas.graphicsContext2D.fill = Color.BLACK
    }
}