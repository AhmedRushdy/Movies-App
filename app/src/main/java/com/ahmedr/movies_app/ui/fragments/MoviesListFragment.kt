package com.ahmedr.movies_app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmedr.ma_task1.adapers.MoviesVerticalAdapter
import com.ahmedr.movies_app.R
import com.ahmedr.movies_app.adapers.MoviesHorezentalAdapter
import com.ahmedr.movies_app.databinding.FragmentMoviesListBinding
import com.ahmedr.movies_app.ui.MainActivity
import com.ahmedr.movies_app.ui.MoviesViewModel
import com.ahmedr.movies_app.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.ahmedr.movies_app.utils.Resource


class MoviesListFragment : Fragment() {
    lateinit var viewModel: MoviesViewModel
    lateinit var moviesHorizontalAdapter: MoviesHorezentalAdapter
    lateinit var moviesVerticalAdapter: MoviesVerticalAdapter
    var isLastPageVer = false
    var isLastPageHor = false
    var binding: FragmentMoviesListBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentMoviesListBinding = FragmentMoviesListBinding.inflate(
            inflater,
            container,
            false
        )

        binding = fragmentMoviesListBinding

        return fragmentMoviesListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        initRecyclerView()
        moviesVerticalAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie_info", it)
            }

            findNavController().navigate(
                R.id.action_moviesListFragment_to_movieDetailFragment,
                bundle
            )
        }
        moviesHorizontalAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie_info", it)

            }

            findNavController().navigate(
                R.id.action_moviesListFragment_to_movieDetailFragment,
                bundle
            )
        }
        getPopularMovies()
        getUpComingMovies()
    }

    private fun getUpComingMovies() {

        viewModel.upComingMovies.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        moviesHorizontalAdapter.differ.submitList(moviesResponse.results.toList())
                        val totalPages = moviesResponse.total_pages / QUERY_PAGE_SIZE + 2
                        isLastPageHor = viewModel.upComingPageNumber == totalPages
                        if (isLastPageHor) {
                            binding?.verticalRv?.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }

                }
                is Resource.Loading -> showProgressBar()
            }

        })
    }


    private fun getPopularMovies() {
        viewModel.popularMovies.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { moviesResponse ->
                        moviesVerticalAdapter.differ.submitList(moviesResponse.results.toList())
                        val totalPages = moviesResponse.total_pages / QUERY_PAGE_SIZE + 2
                        isLastPageVer = viewModel.popularMoviesPageNumber == totalPages
                        if (isLastPageVer) {
                            binding?.horizontalRv?.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> showProgressBar()
            }
        })
    }

    var isErrorVer = true
    var isLoadingVer = false
    var isScrollingVer = false

    private val scrollListenerVer = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isErrorVer
            val isNotLoadingAndNotLastPage = !isLoadingVer && !isLastPageVer
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount + 3 >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
//                val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
//                        isTotalMoreThanVisible && isScrollingVer
            if (isAtLastItem && isScrollingVer) {
                viewModel.getPopularMovies()
                getPopularMovies()
                isScrollingVer = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrollingVer = true
            }
        }
    }
    var isErrorHor = true
    var isLoadingHor = false
    var isScrollingHor = false

    private val scrollListenerHor = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as GridLayoutManager

            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isErrorHor
            val isNotLoadingAndNotLastPage = !isLoadingHor && !isLastPageHor
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrollingHor
            if (isAtLastItem && isScrollingHor) {
                viewModel.getUpComingMovies()
                getUpComingMovies()
                isScrollingHor = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrollingHor = true
            }
        }
    }

    private fun showProgressBar() {
        binding?.progressBar?.visibility = View.VISIBLE

        isLoadingHor = true
        isLoadingVer = true
    }

    private fun hideProgressBar() {
        binding?.progressBar?.visibility = View.GONE
        isErrorVer = false
        isErrorHor = false
    }

    private fun initRecyclerView() {
        moviesHorizontalAdapter = MoviesHorezentalAdapter()
        moviesVerticalAdapter = MoviesVerticalAdapter(viewModel)

        binding?.horizontalRv?.apply {
            adapter = moviesHorizontalAdapter
            addOnScrollListener(this@MoviesListFragment.scrollListenerHor)

        }
        binding?.verticalRv?.apply {
            adapter = moviesVerticalAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@MoviesListFragment.scrollListenerVer)
        }

    }
}
