package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.mayanknagwanshi.cointracker.databinding.ListItemMarketBinding

class WatchListAdapter : RecyclerView.Adapter<WatchListAdapter.PaymentHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHolder {
        val itemBinding =
            ListItemMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PaymentHolder, position: Int) {
        //val paymentBean: PaymentBean = paymentList[position]
        holder.bind(position)
    }

    override fun getItemCount(): Int = 5

    class PaymentHolder(private val itemBinding: ListItemMarketBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int) {
            itemBinding.textViewRank.text = "$position"
            itemBinding.textViewCoin.text = "BTCBD"
            itemBinding.textViewPrice.text = "$25,010"
            itemBinding.textViewChange.text = "1.02%"
            itemBinding.textViewMarketCap.text = "$225,010,123,112"
        }
    }
}