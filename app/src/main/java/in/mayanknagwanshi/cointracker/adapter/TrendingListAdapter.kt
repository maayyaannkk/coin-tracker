package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.data.SearchData
import `in`.mayanknagwanshi.cointracker.data.TrendingData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemTrendingBinding

class TrendingListAdapter : RecyclerView.Adapter<TrendingListAdapter.TrendingViewHolder>() {

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

    class TrendingViewHolder(private val itemBinding: ListItemTrendingBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(trendingData: TrendingData) {
            itemBinding.textViewCoinRank.text = "#${trendingData.marketCapRank}"
            itemBinding.textViewCoin.text = "${trendingData.symbol}"
            itemBinding.textViewCoinName.text = "${trendingData.name}"
            itemBinding.imageViewCoin.load(trendingData.image) {
                crossfade(true)
                placeholder(R.drawable.logo)
                transformations(CircleCropTransformation())
            }
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
    val differ = AsyncListDiffer(this, differCallback)
}