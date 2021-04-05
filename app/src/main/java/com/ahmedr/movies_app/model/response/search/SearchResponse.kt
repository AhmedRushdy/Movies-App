package com.ahmedr.movies_app.model.response

data class SearchResponse(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)