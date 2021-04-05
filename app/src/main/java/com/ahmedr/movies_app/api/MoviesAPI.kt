package com.ahmedr.movies_app.api

import com.ahmedr.movies_app.model.response.SearchResponse
import com.ahmedr.movies_app.model.response.popular.PopularMoviesResponse
import com.ahmedr.movies_app.model.response.upcoming.UpComingResponse
import com.ahmedr.movies_app.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPI {
    @GET("3/movie/upcoming")
    suspend fun getUpComing(
        @Query("page")
        page: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY

         )  : Response<UpComingResponse>

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("page")
        page:Int = 1,
        @Query("api_key")
        apiKey: String= API_KEY

    ) : Response<PopularMoviesResponse>
    @GET("3/search/movie")
    suspend fun searchForMovies(
        @Query("page")
        page:Int = 1,
        @Query("query")
        query: String,
        @Query("api_key")
        apiKey: String= API_KEY

    ) : Response<SearchResponse>
}