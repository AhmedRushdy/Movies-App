package com.ahmedr.movies_app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedr.ma_task1.adapers.MoviesVerticalAdapter
import com.ahmedr.movies_app.R
import com.ahmedr.movies_app.adapers.MoviesHorezentalAdapter
import com.ahmedr.movies_app.databinding.FragmentMoviesListBinding
import com.ahmedr.movies_app.ui.MainActivity
import com.ahmedr.movies_app.ui.MoviesViewModel
import com.ahmedr.movies_app.utils.Resource

class MoviesListFragment : Fragment() {
    lateinit var viewModel: MoviesViewModel
    lateinit var moviesHorezentalAdapter: MoviesHorezentalAdapter
    lateinit var moviesVerticalAdapter: MoviesVerticalAdapter

    var binding: FragmentMoviesListBinding? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentMoviesListBinding = FragmentMoviesListBinding.inflate(inflater,container,false)
        binding = fragmentMoviesListBinding

        return fragmentMoviesListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        initRecyclerView()


        viewModel.popularMovies.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success ->{
                    response.data?.let{ moviesResponse->
                        moviesVerticalAdapter.differ.submitList(moviesResponse.results)
                    }
                }
                is Resource.Error ->{
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> TODO()
            }
        })

        viewModel.upComingMovies.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success ->{
                    response.data?.let { moviesResponse->
                        Toast.makeText(activity,"done",Toast.LENGTH_LONG).show()
                        moviesHorezentalAdapter.differ.submitList(moviesResponse.results)
                    }
                }
                is Resource.Error ->{
                    response.message?.let {message->
                        Toast.makeText(requireContext(), message,Toast.LENGTH_LONG).show()
                    }

                }
                is Resource.Loading -> TODO()
            }

        })


}
    private fun initRecyclerView() {
        moviesHorezentalAdapter = MoviesHorezentalAdapter()
        moviesVerticalAdapter = MoviesVerticalAdapter()
        binding?.horizontalRv?.apply {
            adapter = moviesHorezentalAdapter
        }
        binding?.verticalRv?.apply {
            adapter = moviesVerticalAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }}