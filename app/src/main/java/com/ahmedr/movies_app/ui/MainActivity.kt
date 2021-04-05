package com.ahmedr.movies_app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ahmedr.movies_app.R
import com.ahmedr.movies_app.databinding.ActivityMainBinding
import com.ahmedr.movies_app.repositories.MoviesRepository
import com.ahmedr.movies_app.room.MoviesDatabase
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val moviesRepository = MoviesRepository(MoviesDatabase(this))
        val viewModelProvider = MoviesViewModelProvider(moviesRepository, application)
        viewModel = ViewModelProvider(this, viewModelProvider).get(MoviesViewModel::class.java)

        //setting navBottom
        val navFragmentHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navFragmentHost.navController

        val radius = resources.getDimension(R.dimen.radius_small)
        val bottomNavigationViewBackground = binding.navBottom.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()
        navController = navFragmentHost.navController
        binding.navBottom.setupWithNavController(navController)
    }
}