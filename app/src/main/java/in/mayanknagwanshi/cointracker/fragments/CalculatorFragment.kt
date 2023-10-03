package `in`.mayanknagwanshi.cointracker.fragments

import android.os.Bundle
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.adapter.CalculatorListAdapter
import `in`.mayanknagwanshi.cointracker.adapter.SimpleTextListAdapter
import `in`.mayanknagwanshi.cointracker.databinding.DialogSearchListBinding
import `in`.mayanknagwanshi.cointracker.databinding.FragmentCalculatorBinding
import `in`.mayanknagwanshi.cointracker.network.NetworkResult
import `in`.mayanknagwanshi.cointracker.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CalculatorFragment : Fragment(R.layout.fragment_calculator) {
    private lateinit var binding: FragmentCalculatorBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.progressRecyclerView.recyclerView.addItemDecoration(divider)

        val calculatorListAdapter = CalculatorListAdapter()
        binding.progressRecyclerView.recyclerView.adapter = calculatorListAdapter

        binding.progressRecyclerView.showError()
        val materialAutoCompleteTextView =
            binding.textInputCurrency.editText as MaterialAutoCompleteTextView

        var job: Job? = null
        binding.textInputAmount.editText?.doAfterTextChanged { text ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(750L)
                if (text.toString().toDoubleOrNull() != null)
                    calculatorListAdapter.setConversionData(text.toString().toDouble())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currencyList.collect { event ->
                    materialAutoCompleteTextView.tag = event
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.calculatorList.collect { event ->
                    if (event.isNotEmpty()) {
                        binding.progressRecyclerView.showRecyclerView()
                        calculatorListAdapter.differ.submitList(event)
                    } else
                        binding.progressRecyclerView.showError()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.calculatorData.collect { event ->
                    when (event) {
                        is NetworkResult.Error -> {
                            calculatorListAdapter.purgeConversion()
                        }

                        is NetworkResult.Success -> {
                            binding.progressRecyclerView.showRecyclerView()
                            calculatorListAdapter.setConversionData(
                                binding.textInputAmount.editText?.text.toString()
                                    .toDoubleOrNull(), event.data
                            )
                        }

                        is NetworkResult.Loading -> {
                            if (event.isLoading) binding.progressRecyclerView.showProgress()
                            else binding.progressRecyclerView.hideProgress()
                        }
                    }
                }
            }
        }

        fun showDialog() {
            val dialogBinding = DialogSearchListBinding.inflate(layoutInflater)
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(dialogBinding.root)
                .create()

            val textInputSearch = dialogBinding.textFieldSearch
            val recyclerView = dialogBinding.recyclerView
            val textViewError = dialogBinding.textViewError

            fun toggleError(showError: Boolean) {
                recyclerView.visibility = if (showError) View.GONE else View.VISIBLE
                textViewError.visibility = if (showError) View.VISIBLE else View.GONE

            }
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            val adapter = SimpleTextListAdapter()
            adapter.onItemClick = { currency ->
                val abbr = currency.substring(0, 3)
                viewModel.requestSimpleTokenPrice(abbr)
                materialAutoCompleteTextView.setText(abbr)
                binding.textInputCurrency.tag = abbr
                if (dialog.isShowing) dialog.dismiss()
            }
            recyclerView.adapter = adapter

            val currencyList =
                if (materialAutoCompleteTextView != null)
                    materialAutoCompleteTextView.tag as MutableList<String>?
                else mutableListOf()
            val selectedCurrency =
                if (binding.textInputCurrency.tag != null)
                    binding.textInputCurrency.tag as String?
                else null
            adapter.selectedAbbr = selectedCurrency
            adapter.differ.submitList(currencyList)

            var job: Job? = null
            textInputSearch.editText?.doAfterTextChanged {
                job?.cancel()
                job = lifecycleScope.launch {
                    delay(750L)
                    if (textInputSearch.editText!!.text.isNotEmpty()) {
                        val searchString = textInputSearch.editText!!.text.toString().uppercase()
                        val filteredList = currencyList?.filter { it.contains(searchString) }

                        if (filteredList!!.isNotEmpty()) {
                            toggleError(showError = false)
                            adapter.differ.submitList(filteredList)
                        } else {
                            toggleError(showError = true)
                        }
                    } else {
                        toggleError(showError = false)
                        adapter.differ.submitList(currencyList)
                    }

                }
            }
            dialog.show()
        }

        binding.textInputCurrency.setEndIconOnClickListener { showDialog() }
        materialAutoCompleteTextView.setOnClickListener { showDialog() }
    }


}