package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.data.MarketData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemMarketBinding
import java.text.DecimalFormat

class MarketListAdapter :
    RecyclerView.Adapter<MarketListAdapter.PaymentHolder>() {

    var marketDataList: List<MarketData> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHolder {
        val itemBinding =
            ListItemMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PaymentHolder, position: Int) {
        val marketData: MarketData = marketDataList[position]
        holder.bind(marketData)
    }

    override fun getItemCount(): Int = marketDataList.size

    class PaymentHolder(private val itemBinding: ListItemMarketBinding) :
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
        }
    }
}