package `in`.mayanknagwanshi.cointracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.adapter.WatchListAdapter
import `in`.mayanknagwanshi.cointracker.databinding.FragmentWatchlistBinding
import `in`.mayanknagwanshi.cointracker.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {
    private lateinit var binding: FragmentWatchlistBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.progressRecyclerView.recyclerView.addItemDecoration(divider)

        val watchListAdapter = WatchListAdapter()
        watchListAdapter.onFavoriteClick = { watchlistData ->
            viewModel.toggleWatchlist(watchlistData)
        }
        binding.progressRecyclerView.recyclerView.adapter = watchListAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.watchlistData.collect { event ->
                    watchListAdapter.differ.submitList(event)
                    if (event.isEmpty()) binding.progressRecyclerView.showError()
                    else binding.progressRecyclerView.showRecyclerView()
                }
            }
        }
    }
}