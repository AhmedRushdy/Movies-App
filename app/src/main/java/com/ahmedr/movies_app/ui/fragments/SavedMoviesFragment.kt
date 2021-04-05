package com.ahmedr.movies_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmedr.movies_app.R
import com.ahmedr.movies_app.adapers.FavoriteMoviesAdapter
import com.ahmedr.movies_app.databinding.FragmentFavoritMoviesBinding
import com.ahmedr.movies_app.ui.MainActivity
import com.ahmedr.movies_app.ui.MoviesViewModel
import com.google.android.material.snackbar.Snackbar


class SavedMoviesFragment : Fragment() {
    lateinit var viewModel: MoviesViewModel
    lateinit var favoriteMoviesAdapter: FavoriteMoviesAdapter
    var binding: FragmentFavoritMoviesBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentFavoriteMoviesBinding = FragmentFavoritMoviesBinding.inflate(inflater,container,false)
        binding = fragmentFavoriteMoviesBinding

        return fragmentFavoriteMoviesBinding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        initRecyclerView()
        viewModel.getAllMovies().observe(viewLifecycleOwner, Observer {
            favoriteMoviesAdapter.differ.submitList(it)
        })
        favoriteMoviesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie_info", it)

            }

            findNavController().navigate(
                R.id.action_savedMoviesFragment_to_movieDetailFragment,
                bundle
            )
        }
    val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val movie = favoriteMoviesAdapter.differ.currentList[position]
            viewModel.deleteMovie(movie)

            Snackbar.make(view,"Deleted Movie successfully", Snackbar.LENGTH_LONG)
                .setAction("Undo"){
                    viewModel.saveMovie(movie)
                }.show()
        }
    }
        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding?.rvFavMovie)
        }
    }
    private fun initRecyclerView() {
        favoriteMoviesAdapter = FavoriteMoviesAdapter()
        binding?.rvFavMovie?.apply {
            adapter = favoriteMoviesAdapter
            layoutManager = LinearLayoutManager(activity)
        }


    }
}