package com.ahmedr.movies_app.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ahmedr.movies_app.model.response.Result

@Dao
interface MoviesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie:Result):Long

    @Query("select * from movie_data")
    fun getAllMovies(): LiveData<List<Result>>

    @Delete
    suspend fun deleteMovie(result: Result)
    @Query("delete from movie_data")
    suspend fun deleteAllMovies()
}