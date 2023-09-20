package `in`.mayanknagwanshi.cointracker.data


import com.google.gson.annotations.SerializedName

data class SearchData(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("large")
    val image: String
)