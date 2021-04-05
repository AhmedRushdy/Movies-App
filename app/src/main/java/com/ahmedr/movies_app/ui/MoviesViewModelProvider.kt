package com.ahmedr.movies_app.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedr.movies_app.repositories.MoviesRepository

class MoviesViewModelProvider(
    private val moviesRepository: MoviesRepository
    , val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(app,moviesRepository) as T
    }
}