package com.ahmedr.movies_app.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity( tableName = "movie_data")
data class Result(
    @PrimaryKey(autoGenerate = true)
    var m_id: Int? = null,
    val adult: Boolean?,
    val backdrop_path: String?,
    val genre_ids: List<Int>?,
    val id: Int?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?
):Serializable