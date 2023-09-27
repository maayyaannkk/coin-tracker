package `in`.mayanknagwanshi.cointracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.data.MarketData
import `in`.mayanknagwanshi.cointracker.data.SearchData
import `in`.mayanknagwanshi.cointracker.databinding.ListItemSearchBinding

class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>() {

    var onFavoriteClick: ((SearchData) -> Unit)? = null
    var selectedList = mutableListOf<String>()
        set(value) {
            field = value
            formatAndNotify()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemBinding =
            ListItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchData: SearchData = differ.currentList[position]
        holder.bind(searchData)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class SearchViewHolder(private val itemBinding: ListItemSearchBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(searchData: SearchData) {
            itemBinding.textViewCoinRank.text = "#${searchData.marketCapRank}"
            itemBinding.textViewCoin.text = "${searchData.symbol}"
            itemBinding.textViewCoinName.text = "${searchData.name}"
            itemBinding.buttonFavorite.setOnClickListener {
                if (onFavoriteClick != null) onFavoriteClick?.invoke(searchData)
            }
            itemBinding.imageViewCoin.load(searchData.image) {
                crossfade(true)
                placeholder(R.drawable.logo)
                transformations(CircleCropTransformation())
            }
            itemBinding.buttonFavorite.icon =
                if (searchData.isFavorite) ContextCompat.getDrawable(
                    itemBinding.buttonFavorite.context,
                    R.drawable.ic_favorite_selected
                )
                else ContextCompat.getDrawable(
                    itemBinding.buttonFavorite.context,
                    R.drawable.ic_favorite
                )
        }
    }

    fun formatAndNotify(searchListRaw: List<SearchData> = emptyList()) {
        val searchDataList = searchListRaw.ifEmpty { differ.currentList.map { it.copy() } }
        for (searchData in searchDataList)
            searchData.isFavorite = selectedList.contains(searchData.id)
        differ.submitList(searchDataList)
    }

    private val differCallback = object : DiffUtil.ItemCallback<SearchData>() {
        override fun areItemsTheSame(oldItem: SearchData, newItem: SearchData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchData, newItem: SearchData): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)
}