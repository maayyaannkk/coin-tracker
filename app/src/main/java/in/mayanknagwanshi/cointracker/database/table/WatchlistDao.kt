package `in`.mayanknagwanshi.cointracker.database.table

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist")
    fun getAll(): Flow<List<WatchlistData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(watchlistData: List<WatchlistData>)

    @Delete
    fun delete(watchlistData: WatchlistData)
}