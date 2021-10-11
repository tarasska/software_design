package rendering.model

class DoublePoint(
    private val x: Double,
    private val y: Double
) : Point<Double> {

    override fun getX(): Double = x

    override fun getY(): Double = y
}