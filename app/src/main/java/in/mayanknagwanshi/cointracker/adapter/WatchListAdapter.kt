package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.database.table.WatchlistData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemMarketBinding
import java.text.DecimalFormat

class WatchListAdapter : RecyclerView.Adapter<WatchListAdapter.WatchlistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val itemBinding =
            ListItemMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchlistViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    class WatchlistViewHolder(private val itemBinding: ListItemMarketBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(watchlistData: WatchlistData) {
            itemBinding.textViewRank.text = "${watchlistData.marketCapRank}"
            itemBinding.textViewCoin.text = watchlistData.symbol.uppercase()
            itemBinding.textViewPrice.text =
                "$${DecimalFormat("#,###.000").format(watchlistData.currentPrice)}"
            itemBinding.textViewChange.text =
                "${DecimalFormat("##.00").format(watchlistData.priceChangePercentage24h)}%"
            itemBinding.textViewMarketCap.text =
                "$${DecimalFormat("#,###").format(watchlistData.marketCap)}"
            itemBinding.imageViewCoin.load(watchlistData.image) {
                crossfade(true)
                placeholder(R.drawable.logo)
                transformations(CircleCropTransformation())
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