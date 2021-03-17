package com.ahmedr.movies_app.ui.movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.ahmedr.movies_app.R
import com.ahmedr.movies_app.databinding.ActivityMovieDetailBinding
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: MovieDetailsViewModel
    private val args : MovieDetailActivityArgs by navArgs()
    lateinit var binding: ActivityMovieDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(MovieDetailsViewModel::class.java)


        val movie = args.movieInfo
        Glide.with(this).load("https://image.tmdb.org/t/p/w500"+movie.backdrop_path).
        placeholder(R.drawable.defult)
            .error(R.drawable.defult)
            .into(binding.detailImgPoster)
        binding.detailTitle.text = movie.title
        binding.overview.text = movie.overview

        binding.backImg.setOnClickListener {
            onBackPressed()
        }


    }

}