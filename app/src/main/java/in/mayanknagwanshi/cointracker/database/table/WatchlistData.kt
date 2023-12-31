package `in`.mayanknagwanshi.cointracker.database.table

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "watchlist")
data class WatchlistData(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("current_price")
    val currentPrice: Double? = null,
    @SerializedName("market_cap")
    val marketCap: Double? = null,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("price_change_24h")
    val priceChange24h: Double? = null,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double? = null,
    @SerializedName("last_updated")
    val lastUpdated: String? = null,
    val lastSynced: Date? = null,
    @Embedded
    var currencyFiatData: CurrencyFiatData?=null
)

class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return if (dateLong == null) null else Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}