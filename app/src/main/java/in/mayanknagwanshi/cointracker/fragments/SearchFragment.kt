package `in`.mayanknagwanshi.cointracker.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.adapter.SearchListAdapter
import `in`.mayanknagwanshi.cointracker.adapter.TrendingListAdapter
import `in`.mayanknagwanshi.cointracker.databinding.FragmentSearchBinding
import `in`.mayanknagwanshi.cointracker.network.NetworkResult
import `in`.mayanknagwanshi.cointracker.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)

        binding.progressRecyclerViewCoins.recyclerView.addItemDecoration(divider)
        binding.progressRecyclerViewTrending.recyclerView.addItemDecoration(divider)

        setupTrending()
        setupSearch()
    }

    private fun setupTrending() {
        viewModel.requestTrending()

        val trendingListAdapter = TrendingListAdapter()
        binding.progressRecyclerViewTrending.recyclerView.adapter = trendingListAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.trendingData.collect { event ->
                    when (event) {
                        is NetworkResult.Error -> {
                            binding.progressRecyclerViewTrending.showError()
                        }

                        is NetworkResult.Success -> {
                            binding.progressRecyclerViewTrending.showRecyclerView()
                            trendingListAdapter.differ.submitList(event.data)
                        }

                        is NetworkResult.Loading -> {
                            if (event.isLoading) binding.progressRecyclerViewTrending.showProgress()
                            else binding.progressRecyclerViewTrending.hideProgress()
                        }
                    }
                }
            }
        }
    }

    private fun setupSearch() {
        val searchListAdapter = SearchListAdapter()
        binding.progressRecyclerViewCoins.recyclerView.adapter = searchListAdapter

        var job: Job? = null
        binding.textFieldSearch.editText?.doAfterTextChanged {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(750L)
                if (binding.textFieldSearch.editText!!.text.length > 2) {
                    val searchString = binding.textFieldSearch.editText!!.text.toString()
                    viewModel.searchCoins(searchString)
                }
                if (binding.textFieldSearch.editText!!.text.isNotEmpty()) {
                    binding.groupSearch.visibility = View.VISIBLE
                    binding.groupTrending.visibility = View.GONE
                } else {
                    binding.groupSearch.visibility = View.GONE
                    binding.groupTrending.visibility = View.VISIBLE
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchData.collect { event ->
                    when (event) {
                        is NetworkResult.Error -> {
                            binding.progressRecyclerViewCoins.showError()
                        }

                        is NetworkResult.Success -> {
                            binding.progressRecyclerViewCoins.showRecyclerView()
                            searchListAdapter.differ.submitList(event.data)
                        }

                        is NetworkResult.Loading -> {
                            if (event.isLoading) binding.progressRecyclerViewCoins.showProgress()
                            else binding.progressRecyclerViewCoins.hideProgress()
                        }
                    }
                }
            }
        }
    }
}