package com.ahmedr.movies_app.api

import com.ahmedr.movies_app.model.response.popular.PopularMoviesResponse
import com.ahmedr.movies_app.model.response.upcoming.UpComingResponse
import com.ahmedr.movies_app.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPI {
    @GET("3/movie/upcoming")
    suspend fun getUpComing(
        @Query("api_key")
        apiKey: String = API_KEY
         )  : Response<UpComingResponse>

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key")
        apiKey: String= API_KEY,
        @Query("page")
        page:Int = 1
    ) : Response<PopularMoviesResponse>
}