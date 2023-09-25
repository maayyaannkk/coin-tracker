package `in`.mayanknagwanshi.cointracker.util

import kotlin.math.pow

fun Double.formatLargeAmount(currencySymbol: String = "$"): String {
    if (this > (1 * 10.0.pow(12)))
        return String.format("%s%.2fT", currencySymbol, this / 10.0.pow(12))
    if (this > (1 * 10.0.pow(9)))
        return String.format("%s%.2fB", currencySymbol, this / 10.0.pow(9))
    else if (this > (1 * 10.0.pow(6)))
        return String.format("%s%.2fM", currencySymbol, this / 10.0.pow(6))
    else if (this > (1 * 10.0.pow(3)))
        return String.format("%s%.2fK", currencySymbol, this / 10.0.pow(3))
    else if (this < 1)
        return String.format("%s%.4f", currencySymbol, this)
    return String.format("%s%.2f", currencySymbol, this)
}

fun Double.formatPercentage(): String {
    return String.format("%.2f%%", if (this > 0) this else this * -1)
}