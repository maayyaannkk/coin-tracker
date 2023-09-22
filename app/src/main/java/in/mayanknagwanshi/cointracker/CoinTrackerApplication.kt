package `in`.mayanknagwanshi.cointracker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import `in`.mayanknagwanshi.cointracker.util.SharedPrefUtil

@HiltAndroidApp
class CoinTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (SharedPrefUtil.isDarkMode(this))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}