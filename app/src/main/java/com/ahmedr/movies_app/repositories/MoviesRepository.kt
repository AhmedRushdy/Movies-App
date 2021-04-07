package com.ahmedr.movies_app.repositories

import com.ahmedr.movies_app.api.RetrofitInctance
import com.ahmedr.movies_app.model.response.Result
import com.ahmedr.movies_app.room.MoviesDatabase

class MoviesRepository (
    private val db: MoviesDatabase
        ){
    suspend fun getUpComingMovies(pageNumber:Int) = RetrofitInctance.api.getUpComing(page = pageNumber)
    suspend fun getPopularMovies(pageNumber:Int) = RetrofitInctance.api.getPopularMovies(page = pageNumber)
    suspend fun searchForMovies(pageNumber:Int , query:String) = RetrofitInctance.api.searchForMovies(pageNumber, query )

    //local database
    suspend fun insertMovie(movie:Result) = db.getMoviesDao().insert(movie)
    fun getAllMovies() = db.getMoviesDao().getAllMovies()
    suspend fun deleteMovie(movie:Result) = db.getMoviesDao().deleteMovie(movie)
    suspend fun deleteAllMovies() = db.getMoviesDao().deleteAllMovies()
}