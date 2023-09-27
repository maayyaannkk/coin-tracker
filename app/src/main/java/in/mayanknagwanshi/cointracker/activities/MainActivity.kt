package `in`.mayanknagwanshi.cointracker.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import dagger.hilt.android.AndroidEntryPoint
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.databinding.ActivityMainBinding
import `in`.mayanknagwanshi.cointracker.fragments.MarketFragment
import `in`.mayanknagwanshi.cointracker.fragments.CalculatorFragment
import `in`.mayanknagwanshi.cointracker.fragments.SearchFragment
import `in`.mayanknagwanshi.cointracker.fragments.SettingsFragment
import `in`.mayanknagwanshi.cointracker.fragments.WatchlistFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.isEnabled = false

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuItemMarket -> {
                    binding.viewPager.currentItem = 0
                    true
                }

                R.id.menuItemWatchlist -> {
                    binding.viewPager.currentItem = 1
                    true
                }

                R.id.menuItemSearch -> {
                    binding.viewPager.currentItem = 2
                    true
                }

                R.id.menuItemCalculator -> {
                    binding.viewPager.currentItem = 3
                    true
                }

                R.id.menuItemSettings -> {
                    binding.viewPager.currentItem = 4
                    true
                }

                else -> false
            }
        }
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.bottomNavigationView.selectedItemId = R.id.menuItemMarket
                    1 -> binding.bottomNavigationView.selectedItemId = R.id.menuItemWatchlist
                    2 -> binding.bottomNavigationView.selectedItemId = R.id.menuItemSearch
                    3 -> binding.bottomNavigationView.selectedItemId = R.id.menuItemCalculator
                    4 -> binding.bottomNavigationView.selectedItemId = R.id.menuItemSettings
                }
            }

        })
    }
}

class ViewPagerAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MarketFragment()
            1 -> WatchlistFragment()
            2 -> SearchFragment()
            3 -> CalculatorFragment()
            else -> SettingsFragment()
        }
    }

}