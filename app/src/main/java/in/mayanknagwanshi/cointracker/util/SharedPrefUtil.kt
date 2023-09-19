package `in`.mayanknagwanshi.cointracker.util

import android.content.Context

object SharedPrefUtil {
    private const val prefString = "SHARED_PREFERENCE_SETTINGS"
    private const val prefStringDarkMode = "SHARED_PREFERENCE_SETTINGS_DARK_MODE"

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
}