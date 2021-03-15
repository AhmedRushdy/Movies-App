package com.ahmedr.movies_app.model.response.popular

data class PopularMoviesResponse(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)