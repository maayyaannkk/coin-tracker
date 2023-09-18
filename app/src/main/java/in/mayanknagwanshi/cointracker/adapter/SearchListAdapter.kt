package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.mayanknagwanshi.cointracker.databinding.ListItemMarketBinding
import `in`.mayanknagwanshi.cointracker.databinding.ListItemSearchBinding
import `in`.mayanknagwanshi.cointracker.databinding.ListItemTrendingBinding

class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.PaymentHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHolder {
        val itemBinding =
            ListItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PaymentHolder, position: Int) {
        //val paymentBean: PaymentBean = paymentList[position]
        holder.bind(position)
    }

    override fun getItemCount(): Int = 2

    class PaymentHolder(private val itemBinding: ListItemSearchBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int) {
            itemBinding.textViewCoinRank.text = "#${position * 10}"
            itemBinding.textViewCoin.text = "BTC"
            itemBinding.textViewCoinName.text = "Bitcoin"
        }
    }
}