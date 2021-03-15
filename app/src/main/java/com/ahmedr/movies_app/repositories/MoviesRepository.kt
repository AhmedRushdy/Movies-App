package com.ahmedr.movies_app.repositories

import com.ahmedr.movies_app.api.RetrofitInctance

class MoviesRepository {
    suspend fun getUpComingMovies() = RetrofitInctance.api.getUpComing()
    suspend fun getPopularMovies() = RetrofitInctance.api.getPopularMovies(page = 1)
}