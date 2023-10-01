package `in`.mayanknagwanshi.cointracker.database.table

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyFiatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(watchlistData: List<CurrencyFiatData>)

    @Query("SELECT * FROM currency_fiat")
    fun getAll(): Flow<List<CurrencyFiatData>>

    @Query("SELECT abbr || '(' || symbol || ')' AS formatted FROM currency_fiat")
    fun getAllAsStringList(): Flow<List<String>>
}