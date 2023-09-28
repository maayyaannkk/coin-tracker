package `in`.mayanknagwanshi.cointracker.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.database.table.WatchlistData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemWatchlistBinding
import `in`.mayanknagwanshi.cointracker.util.formatLargeAmount
import `in`.mayanknagwanshi.cointracker.util.formatPercentage
import `in`.mayanknagwanshi.cointracker.util.formatShortForm
import `in`.mayanknagwanshi.cointracker.util.getTimeAgo
import java.text.DecimalFormat

class WatchListAdapter : RecyclerView.Adapter<WatchListAdapter.WatchlistViewHolder>() {
    var onFavoriteClick: ((WatchlistData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val itemBinding =
            ListItemWatchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchlistViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class WatchlistViewHolder(private val itemBinding: ListItemWatchlistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(watchlistData: WatchlistData) {
            itemBinding.textViewRank.text = watchlistData.marketCapRank.formatShortForm()
            itemBinding.textViewCoin.text = watchlistData.symbol.uppercase()
            itemBinding.textViewPrice.text = watchlistData.currentPrice?.formatLargeAmount() ?: ""
            itemBinding.textViewChange.text =
                watchlistData.priceChangePercentage24h?.formatPercentage() ?: ""
            if (watchlistData.priceChangePercentage24h != null) itemBinding.textViewChange.setTextColor(
                if (watchlistData.priceChangePercentage24h > 0) Color.GREEN else Color.RED
            )
            itemBinding.textViewMarketCap.text = watchlistData.marketCap?.formatLargeAmount() ?: ""
            itemBinding.textViewLastUpdated.text =
                if (watchlistData.lastSynced != null)
                    itemBinding.textViewLastUpdated.context.getString(
                        R.string.label_last_updated,
                        watchlistData.lastSynced.getTimeAgo()
                    )
                else ""
            itemBinding.imageViewCoin.load(watchlistData.image) {
                crossfade(true)
                placeholder(R.drawable.logo)
                transformations(CircleCropTransformation())
            }
            itemBinding.buttonFavorite.setOnClickListener {
                if (onFavoriteClick != null) onFavoriteClick?.invoke(watchlistData)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<WatchlistData>() {
        override fun areItemsTheSame(oldItem: WatchlistData, newItem: WatchlistData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WatchlistData, newItem: WatchlistData): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
}