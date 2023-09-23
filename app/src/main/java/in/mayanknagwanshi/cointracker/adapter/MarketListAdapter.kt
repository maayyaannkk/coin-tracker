package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.data.MarketData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemMarketBinding
import java.text.DecimalFormat

class MarketListAdapter : RecyclerView.Adapter<MarketListAdapter.MarketViewHolder>() {

    var onFavoriteClick: ((MarketData) -> Unit)? = null

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
            itemBinding.textViewPrice.text =
                "$${DecimalFormat("#,###.000").format(marketData.currentPrice)}"
            itemBinding.textViewChange.text =
                "${DecimalFormat("##.00").format(marketData.priceChangePercentage24h)}%"
            itemBinding.textViewMarketCap.text =
                "$${DecimalFormat("#,###").format(marketData.marketCap)}"
            itemBinding.imageViewCoin.load(marketData.image) {
                crossfade(true)
                placeholder(R.drawable.logo)
                transformations(CircleCropTransformation())
            }
            itemBinding.imageViewFavorite.setOnClickListener {
                if (onFavoriteClick != null) onFavoriteClick?.invoke(marketData)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<MarketData>() {
        override fun areItemsTheSame(oldItem: MarketData, newItem: MarketData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MarketData, newItem: MarketData): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
}