package `in`.mayanknagwanshi.cointracker.fragments

import android.opengl.Visibility
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
import `in`.mayanknagwanshi.cointracker.adapter.MarketListAdapter
import `in`.mayanknagwanshi.cointracker.databinding.FragmentMarketBinding
import `in`.mayanknagwanshi.cointracker.network.NetworkResult
import `in`.mayanknagwanshi.cointracker.util.formatLargeAmount
import `in`.mayanknagwanshi.cointracker.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MarketFragment : Fragment(R.layout.fragment_market) {
    private lateinit var binding: FragmentMarketBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.progressRecyclerView.recyclerView.addItemDecoration(divider)

        val marketListAdapter = MarketListAdapter()
        marketListAdapter.onFavoriteClick = { marketData ->
            viewModel.toggleWatchlist(
                marketData.id,
                marketData.symbol,
                marketData.name,
                marketData.image,
                marketData.marketCapRank
            )
        }
        binding.progressRecyclerView.recyclerView.adapter = marketListAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedCurrency.collect {
                    viewModel.requestMarket()
                    viewModel.requestGlobal()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.globalData.collect { event ->
                    when (event) {
                        is NetworkResult.Error -> {
                            binding.linearLayoutMarketCap.visibility = View.GONE
                        }

                        is NetworkResult.Success -> {
                            binding.linearLayoutMarketCap.visibility = View.VISIBLE
                            val globalInfoData = event.data
                            binding.textViewGlobalMarketCap.text =
                                globalInfoData.globalMarketCap.formatLargeAmount(globalInfoData.currencyFiatData.currencySymbol)
                            binding.textViewGlobalVolume.text =
                                globalInfoData.globalVolume.formatLargeAmount(globalInfoData.currencyFiatData.currencySymbol)
                        }

                        is NetworkResult.Loading -> {
                            if (event.isLoading) binding.progressRecyclerView.showProgress()
                            else binding.progressRecyclerView.hideProgress()
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.marketData.collect { event ->
                    when (event) {
                        is NetworkResult.Error -> {
                            binding.linearLayoutTitle.visibility = View.GONE
                            binding.progressRecyclerView.showError()
                        }

                        is NetworkResult.Success -> {
                            binding.linearLayoutTitle.visibility = View.VISIBLE
                            binding.progressRecyclerView.showRecyclerView()
                            marketListAdapter.formatAndNotify(event.data)
                        }

                        is NetworkResult.Loading -> {
                            if (event.isLoading) binding.progressRecyclerView.showProgress()
                            else binding.progressRecyclerView.hideProgress()
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteList.collect { event ->
                    marketListAdapter.selectedList = event.toMutableList()
                }
            }
        }
    }
}