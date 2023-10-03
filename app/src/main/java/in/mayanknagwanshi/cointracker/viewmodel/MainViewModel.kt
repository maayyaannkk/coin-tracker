package `in`.mayanknagwanshi.cointracker.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.mayanknagwanshi.cointracker.data.MarketData
import `in`.mayanknagwanshi.cointracker.data.SearchData
import `in`.mayanknagwanshi.cointracker.data.TrendingData
import `in`.mayanknagwanshi.cointracker.database.table.CurrencyFiatDao
import `in`.mayanknagwanshi.cointracker.database.table.WatchlistDao
import `in`.mayanknagwanshi.cointracker.database.table.WatchlistData
import `in`.mayanknagwanshi.cointracker.network.CoinGeckoApi
import `in`.mayanknagwanshi.cointracker.network.NetworkResult
import `in`.mayanknagwanshi.cointracker.util.supportedCurrenciesFiat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONException
import org.json.JSONObject
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coinGeckoApi: CoinGeckoApi,
    private val watchlistDao: WatchlistDao,
    private val currencyFiatDao: CurrencyFiatDao,
    application: Application
) : AndroidViewModel(application) {

    private val _marketData =
        MutableStateFlow<NetworkResult<List<MarketData>>>(NetworkResult.Success(listOf()))
    val marketData: StateFlow<NetworkResult<List<MarketData>>> = _marketData

    private val _trendingData =
        MutableStateFlow<NetworkResult<List<TrendingData>>>(NetworkResult.Success(listOf()))
    val trendingData: StateFlow<NetworkResult<List<TrendingData>>> = _trendingData

    private val _searchData =
        MutableStateFlow<NetworkResult<List<SearchData>>>(NetworkResult.Success(listOf()))
    val searchData: StateFlow<NetworkResult<List<SearchData>>> = _searchData

    private val _calculatorData =
        MutableStateFlow<NetworkResult<JSONObject>>(NetworkResult.Success(JSONObject()))
    val calculatorData: StateFlow<NetworkResult<JSONObject>> = _calculatorData

    val watchlistData = watchlistDao.getAll()
    val favoriteList = watchlistDao.getAllIds()
    val calculatorList = watchlistDao.getAllForCalculator()
    var currencyList = currencyFiatDao.getAllAsStringList()
    var selectedCurrency = currencyFiatDao.getSelectedAbbr()

    fun requestMarket() {
        viewModelScope.launch(Dispatchers.IO) {
            _marketData.value = NetworkResult.Loading(true)
            try {
                val response = coinGeckoApi.getMarket()
                _marketData.value = NetworkResult.Loading(false)
                val result = response.body()
                if (response.isSuccessful && !result.isNullOrEmpty()) {
                    _marketData.value = NetworkResult.Success(result)
                } else {
                    _marketData.value =
                        NetworkResult.Error(response.code(), response.errorBody().toString())
                }
            } catch (e: IOException) {
                _marketData.value = NetworkResult.Loading(false)
                _marketData.value = NetworkResult.Error(0, e.message)
            }

        }
    }

    fun requestTrending() {
        viewModelScope.launch(Dispatchers.IO) {
            _trendingData.value = NetworkResult.Loading(true)
            try {
                val response = coinGeckoApi.getTrending()
                _trendingData.value = NetworkResult.Loading(false)
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
            } catch (e: IOException) {
                _trendingData.value = NetworkResult.Loading(false)
                _trendingData.value = NetworkResult.Error(0, e.message)
            }
        }
    }

    fun searchCoins(searchString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchData.value = NetworkResult.Loading(true)
            try {
                val response = coinGeckoApi.search(searchString)
                _searchData.value = NetworkResult.Loading(false)
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
                    _searchData.value =
                        if (searchDataList.isEmpty()) NetworkResult.Error(0, "No results")
                        else NetworkResult.Success(searchDataList)
                } else {
                    _searchData.value =
                        NetworkResult.Error(response.code(), response.errorBody().toString())
                }
            } catch (e: IOException) {
                _searchData.value = NetworkResult.Loading(false)
                _searchData.value = NetworkResult.Error(0, e.message)
            }
        }
    }

    fun requestWatchlist() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ids = watchlistDao.getAllIdsAsList()
                if (ids.isNotEmpty()) {
                    val response = coinGeckoApi.getWatchlist(ids = TextUtils.join(",", ids))
                    val result = response.body()
                    if (response.isSuccessful && !result.isNullOrEmpty()) {
                        for (marketData in result) {
                            addToWatchlist(marketData)
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun requestSimpleTokenPrice(currency: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ids = watchlistDao.getAllIdsAsList()
                if (ids.isNotEmpty()) {
                    _calculatorData.value = NetworkResult.Loading(true)
                    val response = coinGeckoApi.getSimplePrice(
                        ids = TextUtils.join(",", ids),
                        currency = currency
                    )
                    val result = response.body()?.string()
                    _calculatorData.value = NetworkResult.Loading(false)

                    if (response.isSuccessful && !TextUtils.isEmpty(result)) {
                        try {
                            val responseJson = result?.let { JSONObject(it) }
                            responseJson?.put("currency", currency)
                            _calculatorData.value = NetworkResult.Success(responseJson!!)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            _calculatorData.value = NetworkResult.Error(1, e.message)
                        }
                    }
                }
            } catch (e: IOException) {
                _calculatorData.value = NetworkResult.Loading(false)
                _calculatorData.value = NetworkResult.Error(1, e.message)
                e.printStackTrace()
            }

        }
    }

    private fun addToWatchlist(marketData: MarketData) {
        val watchlistData = WatchlistData(
            id = marketData.id,
            symbol = marketData.symbol,
            name = marketData.name,
            image = marketData.image,
            currentPrice = marketData.currentPrice,
            marketCap = marketData.marketCap,
            marketCapRank = marketData.marketCapRank,
            priceChange24h = marketData.priceChange24h,
            priceChangePercentage24h = marketData.priceChangePercentage24h,
            lastUpdated = marketData.lastUpdated,
            lastSynced = Date()
        )
        viewModelScope.launch(Dispatchers.IO) {
            watchlistDao.insert(watchlistData)
        }
    }

    fun toggleWatchlist(id: String, symbol: String, name: String, image: String, rank: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (watchlistDao.delete(id) == 0) {
                val watchlistData =
                    WatchlistData(
                        id = id,
                        symbol = symbol,
                        name = name,
                        image = image,
                        marketCapRank = rank
                    )
                watchlistDao.insert(watchlistData)
                requestWatchlist()
            }
        }
    }

    fun insertCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            if (currencyFiatDao.getCount() == 0)
                currencyFiatDao.insertAll(supportedCurrenciesFiat.invoke(getApplication()))
        }
    }

    fun updateCurrency(abbr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            currencyFiatDao.updatePreference(abbr)
        }
    }
}