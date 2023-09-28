package `in`.mayanknagwanshi.cointracker.database.table

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.mayanknagwanshi.cointracker.data.CalculatorData
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist order by marketCapRank asc")
    fun getAll(): Flow<List<WatchlistData>>

    @Query("SELECT id FROM watchlist")
    fun getAllIds(): Flow<List<String>>

    @Query("SELECT id FROM watchlist")
    fun getAllIdsAsList(): List<String>

    @Query("SELECT * FROM watchlist order by marketCapRank asc")
    fun getAllForCalculator(): Flow<List<CalculatorData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(watchlistData: WatchlistData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(watchlistData: List<WatchlistData>)

    @Query("Delete from watchlist where id=:id")
    fun delete(id: String): Int
}