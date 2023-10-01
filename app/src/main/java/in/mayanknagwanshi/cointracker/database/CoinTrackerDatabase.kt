package `in`.mayanknagwanshi.cointracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import `in`.mayanknagwanshi.cointracker.database.table.CurrencyFiatDao
import `in`.mayanknagwanshi.cointracker.database.table.CurrencyFiatData
import `in`.mayanknagwanshi.cointracker.database.table.DateConverter
import `in`.mayanknagwanshi.cointracker.database.table.WatchlistDao
import `in`.mayanknagwanshi.cointracker.database.table.WatchlistData

//TODO add currency
@Database(entities = [WatchlistData::class, CurrencyFiatData::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class CoinTrackerDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
    abstract fun currencyFiatDao(): CurrencyFiatDao
}