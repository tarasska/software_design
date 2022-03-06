package model

enum class Currency(val officialName: String, val referencePrice: Double) {
    RUB("Russian ruble", 1.0),
    EUR("Euro", 118.7),
    USD("US Dollar", 105.0);

    companion object {
        fun convert(from: Currency, to: Currency, amount: Double): Double {
            return amount * from.referencePrice / to.referencePrice
        }
    }
}