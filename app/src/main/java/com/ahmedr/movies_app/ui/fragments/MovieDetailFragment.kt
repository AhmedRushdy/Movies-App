package com.ahmedr.movies_app.ui.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.ahmedr.movies_app.R
import com.ahmedr.movies_app.databinding.FragmentMovieDetailBinding
import com.ahmedr.movies_app.ui.MainActivity
import com.ahmedr.movies_app.ui.MoviesViewModel
import com.bumptech.glide.Glide

class MovieDetailFragment : Fragment() {
    private val args : MovieDetailFragmentArgs by navArgs()
    lateinit var binding: FragmentMovieDetailBinding
    lateinit var viewModel: MoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentMovieDetailBinding = FragmentMovieDetailBinding.inflate(inflater,container,false)
        binding = fragmentMovieDetailBinding
        viewModel = ViewModelProviders.of(requireActivity()).get(MoviesViewModel::class.java)

       return fragmentMovieDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        val movie = args.movieInfo
        Glide.with(this).load("https://image.tmdb.org/t/p/w500"+movie.backdrop_path).
        placeholder(R.drawable.defult)
            .error(R.drawable.defult)
            .into(binding.detailImgPoster)
        val res = resources
        binding.detailTitle.text = movie.title
        binding.overview.text = movie.overview
        binding.voteAverage.text = res.getString(R.string.vote_average,movie.vote_average.toString())
        binding.releaseDate.text = res.getString(R.string.formatted_date, movie.release_date)
        binding.saveMovieToFav.setOnClickListener {
            viewModel.saveMovie(movie)
        }
        binding.backImg.setOnClickListener {
            findNavController().navigateUp()
        }


    }


}




