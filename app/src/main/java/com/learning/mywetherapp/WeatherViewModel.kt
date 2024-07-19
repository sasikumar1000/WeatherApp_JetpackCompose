package com.learning.mywetherapp

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.mywetherapp.api.Constant
import com.learning.mywetherapp.api.Constant.isNetworkAvailable
import com.learning.mywetherapp.api.NetworkResponse
import com.learning.mywetherapp.api.RetrofitInstance
import com.learning.mywetherapp.api.WeatherModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi

    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult


    fun getData(city: String,context: Context) {

        if (!isNetworkAvailable(context)) {
            Log.e("NetworkError", "No internet connection")
            return
        }
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.apiKey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Sucess(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to Load data")
                }
            }catch (e: UnknownHostException) {
                _weatherResult.value = NetworkResponse.Error("Failed to Load data${e.message}")
            } catch (e: IOException) {
                _weatherResult.value = NetworkResponse.Error("Failed to Load data${e.message}")
            } catch (e: HttpException) {
                _weatherResult.value = NetworkResponse.Error("Failed to Load data${e.message}")
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Failed to Load data${e.message}")
            }

        }
    }
}


