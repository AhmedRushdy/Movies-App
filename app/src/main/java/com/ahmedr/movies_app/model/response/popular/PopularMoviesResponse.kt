package com.ahmedr.movies_app.model.response.popular

import com.ahmedr.movies_app.model.response.Result

data class PopularMoviesResponse(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)