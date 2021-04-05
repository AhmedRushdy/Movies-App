package com.ahmedr.movies_app.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmedr.movies_app.model.response.Result
import com.ahmedr.movies_app.model.response.SearchResponse
import com.ahmedr.movies_app.model.response.popular.PopularMoviesResponse
import com.ahmedr.movies_app.model.response.upcoming.UpComingResponse
import com.ahmedr.movies_app.repositories.MoviesRepository
import com.ahmedr.movies_app.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MoviesViewModel(
    app: Application,
    private val moviesRepository: MoviesRepository
) : AndroidViewModel(app) {


    val upComingMovies: MutableLiveData<Resource<UpComingResponse>> = MutableLiveData()
    var upComingResponse: UpComingResponse? = null
    var upComingPageNumber = 1

    val popularMovies: MutableLiveData<Resource<PopularMoviesResponse>> = MutableLiveData()
    var popularMoviesResponse: PopularMoviesResponse? = null
    var popularMoviesPageNumber = 1

    val searchResultMovies: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()
    var searchResponse: SearchResponse? = null
    var searchResultMoviesPageNumber = 1
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null


    fun searchForMovie(searchQuery: String) = viewModelScope.launch {
//        searchResultMovies.postValue(Resource.Loading())
//        val response = moviesRepository.searchForMovies( searchResultMoviesPageNumber,searchQuery)
//        Log.i("zzz",response.toString())
//
//        searchResultMovies.postValue(handleSearchNewsResponse(response))
        safeSearchCall(searchQuery)
    }

    private suspend fun safeSearchCall(searchQuery: String) {
        this.newSearchQuery = searchQuery
        searchResultMovies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response =
                    moviesRepository.searchForMovies(searchResultMoviesPageNumber, searchQuery)
                    searchResultMovies.postValue(handleSearchResponse(response))
            } else {
                searchResultMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchResultMovies.postValue(Resource.Error("Network Failure"))
                else -> searchResultMovies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
//    private fun handleSearchNewsResponse(response: Response<SearchResponse>): Resource<SearchResponse>? {
//        if(response.isSuccessful) {
//            response.body()?.let { resultResponse ->
//                return Resource.Success(resultResponse)
//            }
//        }
//        return Resource.Error(response.message())
//    }
    private fun handleSearchResponse(response: Response<SearchResponse>): Resource<SearchResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchResponse== null&&newSearchQuery != oldSearchQuery) {
                    searchResultMoviesPageNumber = 1
                    oldSearchQuery = newSearchQuery
                    searchResponse = resultResponse
                } else {
                    searchResultMoviesPageNumber++
                    val oldArticles = searchResponse?.results
                    val newArticles = resultResponse.results
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    fun getPopularMovies() = viewModelScope.launch {
        safePopularMoviesCall()
    }

    fun getUpComingMovies() = viewModelScope.launch {
        safeUpComingMoviesCall()
    }

    init {
        getPopularMovies()
        getUpComingMovies()
    }

    private fun handlePopularMoviesResponse(response: Response<PopularMoviesResponse>): Resource<PopularMoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                ++popularMoviesPageNumber
                if (popularMoviesResponse == null) {
                    popularMoviesResponse = resultResponse
                } else {
                    val oldMovies = popularMoviesResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)

                }
                return Resource.Success(popularMoviesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safePopularMoviesCall() {
        popularMovies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response =
                    moviesRepository.getPopularMovies(pageNumber = popularMoviesPageNumber)
                popularMovies.postValue(handlePopularMoviesResponse(response))
            } else {
                popularMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> popularMovies.postValue(Resource.Error("Network Failure"))
                else -> popularMovies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private suspend fun safeUpComingMoviesCall() {
        upComingMovies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = moviesRepository.getUpComingMovies(pageNumber = upComingPageNumber)
                upComingMovies.postValue(handleUpComingMoviesResponse(response))
            } else {
                upComingMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> upComingMovies.postValue(Resource.Error("Network Failure"))
                else -> upComingMovies.postValue(Resource.Error("Conversion Error" + t.message))
            }
        }
    }


    private fun handleUpComingMoviesResponse(response: Response<UpComingResponse>): Resource<UpComingResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                upComingPageNumber++
                if (upComingResponse == null) {
                    upComingResponse = resultResponse
                } else {
                    val oldMovies = upComingResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(upComingResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager: ConnectivityManager = getApplication<Application>()
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

    fun saveMovie(movie: Result) = viewModelScope.launch {
        moviesRepository.insertMovie(movie)
    }

    fun deleteMovie(movie: Result) = viewModelScope.launch {
        moviesRepository.deleteMovie(movie)
    }

    fun deleteAllMovies() = viewModelScope.launch {
        moviesRepository.deleteAllMovies()
    }

    fun getAllMovies() = moviesRepository.getAllMovies()
}