package `in`.mayanknagwanshi.cointracker.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.data.MarketData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemMarketBinding
import `in`.mayanknagwanshi.cointracker.util.formatLargeAmount
import `in`.mayanknagwanshi.cointracker.util.formatPercentage
import java.text.DecimalFormat

class MarketListAdapter : RecyclerView.Adapter<MarketListAdapter.MarketViewHolder>() {

    var onFavoriteClick: ((MarketData) -> Unit)? = null
    var selectedList = mutableListOf<String>()
        set(value) {
            field = value
            formatAndNotify()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val itemBinding =
            ListItemMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarketViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val marketData: MarketData = differ.currentList[position]
        holder.bind(marketData)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class MarketViewHolder(private val itemBinding: ListItemMarketBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(marketData: MarketData) {
            itemBinding.textViewRank.text = "${marketData.marketCapRank}"
            itemBinding.textViewCoin.text = marketData.symbol.uppercase()
            itemBinding.textViewPrice.text = marketData.currentPrice.formatLargeAmount()
            itemBinding.textViewChange.text = marketData.priceChangePercentage24h.formatPercentage()
            itemBinding.textViewChange.setTextColor(if (marketData.priceChangePercentage24h > 0) Color.GREEN else Color.RED)
            itemBinding.textViewMarketCap.text = marketData.marketCap.formatLargeAmount()
            itemBinding.imageViewCoin.load(marketData.image) {
                crossfade(true)
                placeholder(R.drawable.logo)
                transformations(CircleCropTransformation())
            }
            itemBinding.buttonFavorite.setOnClickListener {
                if (onFavoriteClick != null) onFavoriteClick?.invoke(marketData)
            }
            itemBinding.buttonFavorite.icon =
                if (marketData.isFavorite) ContextCompat.getDrawable(
                    itemBinding.buttonFavorite.context,
                    R.drawable.ic_favorite_selected
                )
                else ContextCompat.getDrawable(
                    itemBinding.buttonFavorite.context,
                    R.drawable.ic_favorite
                )
        }
    }

    fun formatAndNotify(marketListRaw: List<MarketData> = emptyList()) {
        val marketList = marketListRaw.ifEmpty { differ.currentList.map { it.copy() } }
        for (marketData in marketList)
            marketData.isFavorite = selectedList.contains(marketData.id)
        differ.submitList(marketList)
    }

    private val differCallback = object : DiffUtil.ItemCallback<MarketData>() {
        override fun areItemsTheSame(oldItem: MarketData, newItem: MarketData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MarketData, newItem: MarketData): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)
}