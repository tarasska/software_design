package rendering.model

interface Point<T> {
    fun getX(): T
    fun getY(): T
    operator fun plus(other: Point<T>): Point<T>
    operator fun times(mul: T): Point<T>
}