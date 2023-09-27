package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.data.TrendingData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemTrendingBinding

class TrendingListAdapter : RecyclerView.Adapter<TrendingListAdapter.TrendingViewHolder>() {

    var onFavoriteClick: ((TrendingData) -> Unit)? = null
    var selectedList = mutableListOf<String>()
        set(value) {
            field = value
            formatAndNotify()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val itemBinding =
            ListItemTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrendingViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val trendingData: TrendingData = differ.currentList[position]
        holder.bind(trendingData)
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun formatAndNotify(trendingListRaw: List<TrendingData> = emptyList()) {
        val trendingDataList = trendingListRaw.ifEmpty { differ.currentList.map { it.copy() } }
        for (trendingData in trendingDataList)
            trendingData.isFavorite = selectedList.contains(trendingData.id)
        differ.submitList(trendingDataList)
    }

    inner class TrendingViewHolder(private val itemBinding: ListItemTrendingBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(trendingData: TrendingData) {
            itemBinding.textViewCoinRank.text = "#${trendingData.marketCapRank}"
            itemBinding.textViewCoin.text = "${trendingData.symbol}"
            itemBinding.textViewCoinName.text = "${trendingData.name}"
            itemBinding.buttonFavorite.setOnClickListener {
                if (onFavoriteClick != null) onFavoriteClick?.invoke(trendingData)
            }
            itemBinding.imageViewCoin.load(trendingData.image) {
                crossfade(true)
                placeholder(R.drawable.logo)
                transformations(CircleCropTransformation())
            }
            itemBinding.buttonFavorite.icon =
                if (trendingData.isFavorite) ContextCompat.getDrawable(
                    itemBinding.buttonFavorite.context,
                    R.drawable.ic_favorite_selected
                )
                else ContextCompat.getDrawable(
                    itemBinding.buttonFavorite.context,
                    R.drawable.ic_favorite
                )
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<TrendingData>() {
        override fun areItemsTheSame(oldItem: TrendingData, newItem: TrendingData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TrendingData, newItem: TrendingData): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)
}