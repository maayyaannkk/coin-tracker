package `in`.mayanknagwanshi.cointracker.util

import android.content.Context
import com.google.gson.Gson

object SharedPrefUtil {
    private const val prefString = "SHARED_PREFERENCE_SETTINGS"
    private const val prefStringDarkMode = "SHARED_PREFERENCE_SETTINGS_DARK_MODE"
    private const val prefStringStartScreenIndex = "SHARED_PREFERENCE_START_SCREEN_INDEX"

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

    fun saveStartScreenIndex(context: Context, index: Int) {
        val sharedPref = context.getSharedPreferences(prefString, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(prefStringStartScreenIndex, index)
            apply()
        }
    }

    fun getStartScreenIndex(context: Context): Int {
        val sharedPref = context.getSharedPreferences(prefString, Context.MODE_PRIVATE)
        return sharedPref.getInt(prefStringStartScreenIndex, 1)
    }
}