package `in`.mayanknagwanshi.cointracker.database.table

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyFiatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(watchlistData: List<CurrencyFiatData>)

    @Query("SELECT abbr || ' (' || currencySymbol || ')' FROM currency_fiat order by isPreference desc")
    fun getAllAsStringList(): Flow<List<String>>

    @Query("SELECT abbr FROM currency_fiat where isPreference=1")
    fun getSelectedAbbr(): Flow<String>

    @Query("UPDATE currency_fiat set isPreference=1 where abbr=:abbr")
    fun setPreference(abbr: String)

    @Query("UPDATE currency_fiat set isPreference=0 where isPreference=1")
    fun removePreference()

    @Query("SELECT count(*) FROM currency_fiat")
    fun getCount(): Int

    @Query("SELECT * FROM currency_fiat where isPreference=1")
    fun getSelected(): CurrencyFiatData

    @Transaction
    fun updatePreference(abbr: String) {
        removePreference()
        setPreference(abbr)
    }
}