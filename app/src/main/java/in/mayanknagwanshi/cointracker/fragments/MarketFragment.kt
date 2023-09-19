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
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.adapter.MarketListAdapter
import `in`.mayanknagwanshi.cointracker.databinding.FragmentMarketBinding
import `in`.mayanknagwanshi.cointracker.network.NetworkResult
import `in`.mayanknagwanshi.cointracker.viewmodel.MainViewModel
import kotlinx.coroutines.launch

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

        viewModel.requestMarket()

        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(divider)

        val marketListAdapter = MarketListAdapter()
        binding.recyclerView.adapter = marketListAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.marketData.collect { event ->
                    when (event) {
                        is NetworkResult.Error -> TODO()
                        is NetworkResult.Success -> marketListAdapter.marketDataList = event.data
                    }
                }
            }
        }

    }
}