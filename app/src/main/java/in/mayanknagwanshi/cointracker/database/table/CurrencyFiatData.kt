package `in`.mayanknagwanshi.cointracker.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_fiat")
data class CurrencyFiatData(
    @PrimaryKey val abbr: String,
    val name: String,
    val symbol: String,
    var isPreference: Boolean = false
)