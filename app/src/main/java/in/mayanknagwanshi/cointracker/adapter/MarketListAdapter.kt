package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.mayanknagwanshi.cointracker.databinding.ListItemMarketBinding

class MarketListAdapter : RecyclerView.Adapter<MarketListAdapter.PaymentHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHolder {
        val itemBinding =
            ListItemMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PaymentHolder, position: Int) {
        //val paymentBean: PaymentBean = paymentList[position]
        //holder.bind(paymentBean)
    }

    override fun getItemCount(): Int = 25

    class PaymentHolder(private val itemBinding: ListItemMarketBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        /*fun bind(coinData: CoinData) {
            itemBinding.tvPaymentInvoiceNumber.text = paymentBean.invoiceNumber
            itemBinding.tvPaymentAmount.text = paymentBean.totalAmount
        }*/
    }
}