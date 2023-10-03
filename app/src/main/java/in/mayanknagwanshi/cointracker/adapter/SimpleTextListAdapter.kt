package `in`.mayanknagwanshi.cointracker.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.data.MarketData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemSimpleTextBinding

class SimpleTextListAdapter : RecyclerView.Adapter<SimpleTextListAdapter.ItemViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    var selectedAbbr: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemBinding =
            ListItemSimpleTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ItemViewHolder(private val itemBinding: ListItemSimpleTextBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(text: String) {
            val backgroundColor = MaterialColors.getColor(
                itemBinding.textView,
                com.google.android.material.R.attr.colorSurfaceVariant
            )
            itemBinding.textView.text = text
            if (selectedAbbr != null && text.contains(selectedAbbr!!))
                itemBinding.textView.setBackgroundColor(backgroundColor)
            else itemBinding.textView.setBackgroundColor(Color.TRANSPARENT)

            itemBinding.textView.setOnClickListener {
                onItemClick?.invoke(text)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
}