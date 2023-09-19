package `in`.mayanknagwanshi.cointracker.network

import `in`.mayanknagwanshi.cointracker.data.MarketData
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET

interface CoinGeckoApi {

    @GET("coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false&locale=en")
    suspend fun getMarket(): Response<List<MarketData>>

}