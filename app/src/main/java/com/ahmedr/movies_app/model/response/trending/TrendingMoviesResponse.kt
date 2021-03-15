package com.ahmedr.movies_app.model.response.trending


data class TrendingMoviesResponse(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)