package com.ahmedr.movies_app.ui.fragments

import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmedr.ma_task1.adapers.MoviesVerticalAdapter
import com.ahmedr.movies_app.R
import com.ahmedr.movies_app.adapers.MoviesHorezentalAdapter
import com.ahmedr.movies_app.adapers.SearchAdapter
import com.ahmedr.movies_app.databinding.FragmentSearchBinding
import com.ahmedr.movies_app.ui.MainActivity
import com.ahmedr.movies_app.ui.MoviesViewModel
import com.ahmedr.movies_app.utils.Constants
import com.ahmedr.movies_app.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    lateinit var viewModel: MoviesViewModel
    var binding: FragmentSearchBinding? = null
    lateinit var searchAdapter: MoviesHorezentalAdapter
    var isLastPageVer = false
    var isLastPageHor = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        binding = fragmentSearchBinding

        return fragmentSearchBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        initRecyclerView()

        searchAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie_info", it)
            }

            findNavController().navigate(
                R.id.action_historyFragment_to_movieDetailFragment,
                bundle
            )
        }

        var job: Job? = null
        binding?.search?.addTextChangedListener {editable->
            job?.cancel()
            job = MainScope().launch {
                delay(1000)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.searchForMovie(editable.toString())
                    }
                }
            }
        }
        search()
    }

    private fun search() {
        viewModel.searchResultMovies.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        searchAdapter.differ.submitList(moviesResponse.results.toList())
                        val totalPages = moviesResponse.total_pages / Constants.QUERY_PAGE_SIZE + 2
                        isLastPageVer = viewModel.popularMoviesPageNumber == totalPages
                        if (isLastPageVer) {
                            binding?.rvSearchMovie?.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
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
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
//                val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
//                        isTotalMoreThanVisible && isScrollingVer
            if(isAtLastItem && isScrollingVer) {
                viewModel.searchForMovie(binding?.search?.text.toString())
                search()
                isScrollingVer = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrollingVer = true
            }
        }
    }
    private fun showProgressBar(){
        binding?.progressBar?.visibility= View.VISIBLE


    }
    private fun hideProgressBar(){
        binding?.progressBar?.visibility= View.GONE

    }
    private fun initRecyclerView() {
        val manager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        searchAdapter = MoviesHorezentalAdapter()
        binding?.rvSearchMovie?.apply {
            adapter = searchAdapter
            layoutManager = manager
            addOnScrollListener(this@SearchFragment.scrollListenerVer)

        }
    }
}