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

/**
 * App TODO list
 * 1. integrate watchlist api and run when app open
 * 2. integrate watchlist with update frequency using work manager
 * 3. display last updated time on watchlist (times ago)
 * 4. create and display calculator ui (use only top 10 coins for now - later can do with watchlist)
 * 5. integrate calculator api with calculator ui
 * 6. add supported currency in settings
 * 7. integrate supported currency in market and watchlist
 */