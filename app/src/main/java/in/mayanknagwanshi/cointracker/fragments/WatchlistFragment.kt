package `in`.mayanknagwanshi.cointracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.adapter.MarketListAdapter
import `in`.mayanknagwanshi.cointracker.adapter.WatchListAdapter
import `in`.mayanknagwanshi.cointracker.databinding.FragmentMarketBinding
import `in`.mayanknagwanshi.cointracker.databinding.FragmentWatchlistBinding

class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {
    private lateinit var binding: FragmentWatchlistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(divider)

        binding.recyclerView.adapter = WatchListAdapter()

        super.onViewCreated(view, savedInstanceState)
    }
}