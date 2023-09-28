package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.data.CalculatorData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemCalculatorBinding
import org.json.JSONObject

class CalculatorListAdapter : RecyclerView.Adapter<CalculatorListAdapter.CalculatorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculatorViewHolder {
        val itemBinding =
            ListItemCalculatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalculatorViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CalculatorViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class CalculatorViewHolder(private val itemBinding: ListItemCalculatorBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(calculatorData: CalculatorData) {
            itemBinding.textViewCoin.text = calculatorData.symbol.uppercase()
            itemBinding.textViewCoinAmount.text =
                if (calculatorData.amountToDisplay != null && calculatorData.currency != null)
                    String.format("%.5f", calculatorData.amountToDisplay)
                else ""
            itemBinding.imageViewCoin.load(calculatorData.image) {
                crossfade(true)
                placeholder(R.drawable.logo)
                transformations(CircleCropTransformation())
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CalculatorData>() {
        override fun areItemsTheSame(oldItem: CalculatorData, newItem: CalculatorData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CalculatorData, newItem: CalculatorData): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    fun setConversionData(value: Double) {
        if (differ.currentList.isEmpty()) return

        val currentList = differ.currentList.map { it.copy() }
        for (calculatorData in currentList) {
            if (calculatorData.amount != null) {
                calculatorData.amountToDisplay = value / calculatorData.amount!!
            }
        }
        differ.submitList(currentList)
    }

    fun setConversionData(value: Double?, conversionObject: JSONObject) {
        if (differ.currentList.isEmpty() || conversionObject.toString() == "{}") return

        val currency = conversionObject.getString("currency").lowercase()
        val currentList = differ.currentList.map { it.copy() }
        for (calculatorData in currentList) {
            if (conversionObject.has(calculatorData.id)
                && conversionObject.getJSONObject(calculatorData.id).has(currency)
            ) {
                calculatorData.amount =
                    conversionObject.getJSONObject(calculatorData.id).getDouble(currency)
                if (value != null) calculatorData.amountToDisplay = value /
                        conversionObject.getJSONObject(calculatorData.id).getDouble(currency)
                calculatorData.currency = currency
            }
        }
        differ.submitList(currentList)
    }

    fun purgeConversion() {
        if (differ.currentList.isEmpty()) return

        val currentList = differ.currentList.map { it.copy() }
        for (calculatorData in currentList) {
            calculatorData.amount = null
            calculatorData.amountToDisplay = null
            calculatorData.currency = null
        }
        differ.submitList(currentList)
    }
}