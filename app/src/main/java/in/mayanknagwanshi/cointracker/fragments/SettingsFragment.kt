package `in`.mayanknagwanshi.cointracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.adapter.SimpleTextListAdapter
import `in`.mayanknagwanshi.cointracker.databinding.DialogSearchListBinding
import `in`.mayanknagwanshi.cointracker.databinding.FragmentSettingsBinding
import `in`.mayanknagwanshi.cointracker.util.SharedPrefUtil
import `in`.mayanknagwanshi.cointracker.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchDarkMode.isChecked = SharedPrefUtil.isDarkMode(requireContext())
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                SharedPrefUtil.saveDarkMode(requireActivity(), true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                SharedPrefUtil.saveDarkMode(requireActivity(), false)
            }
        }

        val screenList = listOf(
            getString(R.string.menu_item_market),
            getString(R.string.menu_item_watchlist),
            getString(R.string.menu_item_search),
            getString(R.string.menu_item_calculator)
        )
        binding.textViewDefaultScreen.text =
            screenList[SharedPrefUtil.getStartScreenIndex(requireContext()) ]

        binding.linearLayoutCurrency.setOnClickListener {
            showCurrencyDialog()
        }

        binding.linearLayoutDefaultScreen.setOnClickListener {
            showDefaultScreenDialog(screenList)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currencyList.collect { event ->
                    binding.linearLayoutCurrency.tag = event
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedCurrency.collect { currency ->
                    binding.textViewCurrency.text = currency
                    binding.textViewCurrency.tag = currency
                }
            }
        }

    }

    private fun showCurrencyDialog() {
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
            viewModel.updateCurrency(abbr)
            if (dialog.isShowing) dialog.dismiss()
        }
        recyclerView.adapter = adapter

        val currencyList =
            if (binding.linearLayoutCurrency.tag != null)
                binding.linearLayoutCurrency.tag as MutableList<String>?
            else mutableListOf()
        val selectedCurrency =
            if (binding.textViewCurrency.tag != null)
                binding.textViewCurrency.tag as String?
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

    private fun showDefaultScreenDialog(screenList: List<String>) {
        val currentIndex = SharedPrefUtil.getStartScreenIndex(requireContext())
        MaterialAlertDialogBuilder(requireContext())
            .setSingleChoiceItems(
                screenList.toTypedArray(),
                currentIndex
            ) { dialog, checkedItemPosition ->
                if (checkedItemPosition != AdapterView.INVALID_POSITION && checkedItemPosition != currentIndex) {
                    SharedPrefUtil.saveStartScreenIndex(requireContext(), checkedItemPosition)
                    binding.textViewDefaultScreen.text = screenList[checkedItemPosition ]
                    (dialog as AlertDialog?)?.dismiss()
                }
            }
            .show()
    }
}