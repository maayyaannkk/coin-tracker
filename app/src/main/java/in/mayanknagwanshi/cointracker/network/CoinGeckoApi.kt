package `in`.mayanknagwanshi.cointracker.network

import `in`.mayanknagwanshi.cointracker.data.MarketData
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("coins/markets?order=market_cap_desc&per_page=100&page=1&sparkline=false&locale=en")
    suspend fun getMarket(@Query("vs_currency") currency: String = "usd"): Response<List<MarketData>>

    @GET("coins/markets?order=market_cap_desc&per_page=100&page=1&sparkline=false&locale=en")
    suspend fun getWatchlist(
        @Query("vs_currency") currency: String = "usd",
        @Query("ids") ids: String
    ): Response<List<MarketData>>

    @GET("simple/price")
    suspend fun getSimplePrice(
        @Query("vs_currencies") currency: String = "usd",
        @Query("ids") ids: String
    ): Response<ResponseBody>

    @GET("search/trending")
    suspend fun getTrending(): Response<ResponseBody>

    @GET("search")
    suspend fun search(@Query("query") search: String): Response<ResponseBody>
}