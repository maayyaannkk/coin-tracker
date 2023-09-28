package `in`.mayanknagwanshi.cointracker.util

import android.content.Context
import com.google.gson.Gson

object SharedPrefUtil {
    private const val prefString = "SHARED_PREFERENCE_SETTINGS"
    private const val prefStringDarkMode = "SHARED_PREFERENCE_SETTINGS_DARK_MODE"
    private const val prefStringCurrency = "SHARED_PREFERENCE_SETTINGS_CURRENCY"

    fun saveDarkMode(context: Context, isDarkMode: Boolean) {
        val sharedPref = context.getSharedPreferences(prefString, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(prefStringDarkMode, isDarkMode)
            apply()
        }
    }

    fun isDarkMode(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(prefString, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(prefStringDarkMode, false)
    }

    fun saveUserCurrency(context: Context, currencyData: FiatCurrencyData) {
        val sharedPref = context.getSharedPreferences(prefString, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(prefStringCurrency, Gson().toJson(currencyData))
            apply()
        }
    }

    fun getUserCurrency(context: Context): FiatCurrencyData {
        val sharedPref = context.getSharedPreferences(prefString, Context.MODE_PRIVATE)
        return Gson().fromJson(
            sharedPref.getString(
                prefStringCurrency,
                Gson().toJson(FiatCurrencyData("USD", "US Dollar", "$"))
            ),
            FiatCurrencyData::class.java
        )
    }

}