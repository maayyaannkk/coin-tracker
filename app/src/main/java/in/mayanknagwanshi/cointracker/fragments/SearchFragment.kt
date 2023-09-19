package `in`.mayanknagwanshi.cointracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.adapter.SearchListAdapter
import `in`.mayanknagwanshi.cointracker.adapter.TrendingListAdapter
import `in`.mayanknagwanshi.cointracker.databinding.FragmentSearchBinding

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)

        binding.recyclerViewCoins.addItemDecoration(divider)
        binding.recyclerViewCoins.adapter = SearchListAdapter()

        binding.recyclerViewTrending.addItemDecoration(divider)
        binding.recyclerViewTrending.adapter = TrendingListAdapter()

        super.onViewCreated(view, savedInstanceState)
    }
}