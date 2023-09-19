package `in`.mayanknagwanshi.cointracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import `in`.mayanknagwanshi.cointracker.data.MarketData
import `in`.mayanknagwanshi.cointracker.network.NetworkResult
import `in`.mayanknagwanshi.cointracker.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _marketData =
        MutableStateFlow<NetworkResult<List<MarketData>>>(NetworkResult.Success(listOf()))
    val marketData: StateFlow<NetworkResult<List<MarketData>>> = _marketData

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
}