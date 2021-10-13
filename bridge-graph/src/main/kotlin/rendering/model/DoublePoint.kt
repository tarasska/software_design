package rendering.model

class DoublePoint(
    private val x: Double,
    private val y: Double
) : Point<Double> {

    override fun getX(): Double = x

    override fun getY(): Double = y

    override fun plus(other: Point<Double>): DoublePoint {
        return DoublePoint(x + other.getX(), y + other.getY())
    }

    override fun times(mul: Double): DoublePoint {
        return DoublePoint(x * mul, y * mul)
    }
}