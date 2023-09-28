package `in`.mayanknagwanshi.cointracker.data

data class CalculatorData(
    val id: String,
    val name: String,
    val symbol: String,
    val marketCapRank: Int,
    val image: String,
    var currency: String?,
    var amount: Double?,
    var amountToDisplay: Double?
)