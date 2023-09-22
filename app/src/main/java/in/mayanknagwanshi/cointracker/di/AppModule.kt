package `in`.mayanknagwanshi.cointracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import `in`.mayanknagwanshi.cointracker.database.CoinTrackerDatabase
import `in`.mayanknagwanshi.cointracker.database.table.WatchlistDao
import `in`.mayanknagwanshi.cointracker.network.CoinGeckoApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideCoinGeckoApi(): CoinGeckoApi {
        return Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .build().create(CoinGeckoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinTrackerDatabase(@ApplicationContext context: Context): CoinTrackerDatabase {
        return Room.databaseBuilder(
            context,
            CoinTrackerDatabase::class.java, "coin-tracker-database"
        ).build()
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideWatchlistDao(database: CoinTrackerDatabase): WatchlistDao {
        return database.watchlistDao()
    }
}