package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.data.SearchData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemSearchBinding

class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>() {
    var searchList: List<SearchData> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemBinding =
            ListItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchData: SearchData = searchList[position]
        holder.bind(searchData)
    }

    override fun getItemCount(): Int = searchList.size

    class SearchViewHolder(private val itemBinding: ListItemSearchBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(searchData: SearchData) {
            itemBinding.textViewCoinRank.text = "#${searchData.marketCapRank}"
            itemBinding.textViewCoin.text = "${searchData.symbol}"
            itemBinding.textViewCoinName.text = "${searchData.name}"
            itemBinding.imageViewCoin.load(searchData.image) {
                crossfade(true)
                placeholder(R.drawable.logo)
                transformations(CircleCropTransformation())
            }
        }
    }
}