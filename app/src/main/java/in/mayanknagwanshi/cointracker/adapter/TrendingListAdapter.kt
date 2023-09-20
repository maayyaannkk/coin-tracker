package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.data.TrendingData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemTrendingBinding

class TrendingListAdapter : RecyclerView.Adapter<TrendingListAdapter.TrendingViewHolder>() {
    var trendingDataList: List<TrendingData> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val itemBinding =
            ListItemTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrendingViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val trendingData: TrendingData = trendingDataList[position]
        holder.bind(trendingData)
    }

    override fun getItemCount(): Int = trendingDataList.size

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
}