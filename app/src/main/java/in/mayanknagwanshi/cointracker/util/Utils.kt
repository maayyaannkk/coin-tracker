package `in`.mayanknagwanshi.cointracker.util

import android.content.Context
import android.content.res.AssetManager
import android.text.format.DateUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.mayanknagwanshi.cointracker.database.table.CurrencyFiatData
import java.util.Date
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

fun Int.formatShortForm(precision: Int = 1): String {
    if (this >= (1 * 10.0.pow(12)))
        return String.format("%.${precision}fT", this / 10.0.pow(12))
    if (this >= (1 * 10.0.pow(9)))
        return String.format("%.${precision}fB", this / 10.0.pow(9))
    else if (this >= (1 * 10.0.pow(6)))
        return String.format("%.${precision}fM", this / 10.0.pow(6))
    else if (this >= (1 * 10.0.pow(3)))
        return String.format("%.${precision}fK", this / 10.0.pow(3))
    return String.format("%d", this)
}

fun Date.getTimeAgo(): String? {
    var time = this.time
    if (time < 1000000000000L) {
        time *= 1000
    }
    val now = System.currentTimeMillis()
    if (time > now || time <= 0) {
        return null
    }
    val diff = now - time
    return if (diff < DateUtils.MINUTE_IN_MILLIS) {
        "just now"
    } else if (diff < 2 * DateUtils.MINUTE_IN_MILLIS) {
        "a minute ago"
    } else if (diff < 50 * DateUtils.MINUTE_IN_MILLIS) {
        (diff / DateUtils.MINUTE_IN_MILLIS).toString() + " minutes ago"
    } else if (diff < 90 * DateUtils.MINUTE_IN_MILLIS) {
        "an hour ago"
    } else if (diff < 24 * DateUtils.HOUR_IN_MILLIS) {
        (diff / DateUtils.HOUR_IN_MILLIS).toString() + " hours ago"
    } else if (diff < 48 * DateUtils.HOUR_IN_MILLIS) {
        "yesterday"
    } else {
        (diff / DateUtils.DAY_IN_MILLIS).toString() + " days ago"
    }
}

fun AssetManager.readAssetsFile(fileName: String): String =
    open(fileName).bufferedReader().use { it.readText() }

val supportedCurrenciesFiat = fun(context: Context): List<CurrencyFiatData> {
    return Gson().fromJson(
        context.assets.readAssetsFile("currency.json"),
        object : TypeToken<List<CurrencyFiatData>>() {}.type
    )
}

/**
 * 1. integrate watchlist api and run when app open
 * 3. display last updated time on watchlist (times ago)
 * 8. add to watchlist from search and trending
 * 4. create and display calculator ui (use only top 10 coins for now - later can do with watchlist)
 * 5. integrate calculator api with calculator ui
 * 9. remove currency util and add to room
 *
 * App TODO list
 * 6. add supported currency in settings
 * 7. integrate supported currency in market and watchlist
 * 2. integrate watchlist with update frequency using work manager
 */