package com.ahmedr.movies_app.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmedr.movies_app.model.response.popular.PopularMoviesResponse
import com.ahmedr.movies_app.model.response.upcoming.UpComingResponse
import com.ahmedr.movies_app.repositories.MoviesRepository
import com.ahmedr.movies_app.utils.MoviesApplication
import com.ahmedr.movies_app.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MoviesViewModel(
    app: Application,
    private val moviesRepository: MoviesRepository
) : AndroidViewModel(app) {
    init {
        getPopularMovies()
        getUpComingMovies()
    }

    val upComingMovies: MutableLiveData<Resource<UpComingResponse>> = MutableLiveData()
    var UpComingResponse: UpComingResponse? = null

    val popularMovies: MutableLiveData<Resource<PopularMoviesResponse>> = MutableLiveData()
    var popularMoviesResponse : PopularMoviesResponse? = null

    private fun getPopularMovies() = viewModelScope.launch {
        val response = moviesRepository.getPopularMovies()
        popularMovies.postValue(handlePopularMoviesResponse(response))
    }



    private fun handlePopularMoviesResponse(response: Response<PopularMoviesResponse>): Resource<PopularMoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun getUpComingMovies() = viewModelScope.launch {
        val response = moviesRepository.getUpComingMovies()
        upComingMovies.postValue(handleUpComingMoviesResponse(response))
    }


    private suspend fun safeUpComingMoviesCall(response: Response<UpComingResponse>): Resource<UpComingResponse> {

        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())

    }


    private fun handleUpComingMoviesResponse(response: Response<UpComingResponse>): Resource<UpComingResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager: ConnectivityManager = getApplication<MoviesApplication>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}