package com.ahmedr.movies_app.model.response.upcoming
import com.ahmedr.movies_app.model.response.Result

data class UpComingResponse(
    val dates: Dates,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)