package com.ahmedr.movies_app.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ahmedr.movies_app.model.response.Result

@Dao
interface MoviesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie:Result):Long

    @Query("select * from movie_data")
    fun getAllArticles(): LiveData<List<Result>>

    @Delete
    suspend fun deleteArticle(result: Result)
}