package `in`.mayanknagwanshi.cointracker.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.databinding.ActivityMainBinding
import `in`.mayanknagwanshi.cointracker.fragments.MarketFragment
import `in`.mayanknagwanshi.cointracker.fragments.NewsFragment
import `in`.mayanknagwanshi.cointracker.fragments.SearchFragment
import `in`.mayanknagwanshi.cointracker.fragments.SettingsFragment
import `in`.mayanknagwanshi.cointracker.fragments.WatchlistFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuItemMarket -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, MarketFragment())
                        .commit()
                    true
                }

                R.id.menuItemWatchlist -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, WatchlistFragment())
                        .commit()
                    true
                }

                R.id.menuItemSearch -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, SearchFragment())
                        .commit()
                    true
                }

                R.id.menuItemNews -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, NewsFragment())
                        .commit()
                    true
                }

                R.id.menuItemSettings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, SettingsFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}