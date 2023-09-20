package `in`.mayanknagwanshi.cointracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import `in`.mayanknagwanshi.cointracker.data.MarketData
import `in`.mayanknagwanshi.cointracker.data.SearchData
import `in`.mayanknagwanshi.cointracker.data.TrendingData
import `in`.mayanknagwanshi.cointracker.network.NetworkResult
import `in`.mayanknagwanshi.cointracker.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _marketData =
        MutableStateFlow<NetworkResult<List<MarketData>>>(NetworkResult.Success(listOf()))
    val marketData: StateFlow<NetworkResult<List<MarketData>>> = _marketData

    private val _trendingData =
        MutableStateFlow<NetworkResult<List<TrendingData>>>(NetworkResult.Success(listOf()))
    val trendingData: StateFlow<NetworkResult<List<TrendingData>>> = _trendingData

    private val _searchData =
        MutableStateFlow<NetworkResult<List<SearchData>>>(NetworkResult.Success(listOf()))
    val searchData: StateFlow<NetworkResult<List<SearchData>>> = _searchData

    fun requestMarket() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.api.getMarket()
            val result = response.body()
            if (response.isSuccessful && !result.isNullOrEmpty()) {
                _marketData.value = NetworkResult.Success(result)
            } else {
                _marketData.value =
                    NetworkResult.Error(response.code(), response.errorBody().toString())
            }
        }
    }

    fun requestTrending() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.api.getTrending()
            val result = response.body()?.string()
            if (response.isSuccessful && result != null) {
                val coinsArray = JSONObject(result).getJSONArray("coins")
                val trendingDataList = mutableListOf<TrendingData>()
                for (i in 0..<coinsArray.length()) {
                    val coinObject = coinsArray.getJSONObject(i).getJSONObject("item")
                    trendingDataList.add(
                        Gson().fromJson(
                            coinObject.toString(),
                            TrendingData::class.java
                        )
                    )
                }
                _trendingData.value = NetworkResult.Success(trendingDataList)
            } else {
                _trendingData.value =
                    NetworkResult.Error(response.code(), response.errorBody().toString())
            }
        }
    }

    fun searchCoins(searchString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.api.search(searchString)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                val coinsArray = JSONObject(result.string()).getJSONArray("coins")
                val searchDataList = mutableListOf<SearchData>()

                for (i in 0..<if (coinsArray.length() <= 10) coinsArray.length() else 10) {
                    val coinObject = coinsArray.getJSONObject(i)
                    searchDataList.add(
                        Gson().fromJson(
                            coinObject.toString(),
                            SearchData::class.java
                        )
                    )
                }
                _searchData.value = NetworkResult.Success(searchDataList)
            } else {
                _searchData.value =
                    NetworkResult.Error(response.code(), response.errorBody().toString())
            }
        }
    }
}