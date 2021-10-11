package rendering

import rendering.model.Point

interface RenderingApi<T> {
    fun getDrawingAreaWidth(): Long
    fun getDrawingAreaHeight(): Long
    fun drawCircle(point: Point<T>, radius: T)
    fun drawLine(from: Point<T>, to: Point<T>)
}