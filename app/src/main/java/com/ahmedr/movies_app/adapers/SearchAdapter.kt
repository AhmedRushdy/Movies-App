package com.ahmedr.movies_app.adapers

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedr.movies_app.R
import com.ahmedr.movies_app.databinding.MovieItemBinding
import com.ahmedr.movies_app.ui.MoviesViewModel
import com.ahmedr.movies_app.model.response.Result
import com.bumptech.glide.Glide

class SearchAdapter  ( var viewModel: MoviesViewModel): RecyclerView.Adapter<SearchAdapter.MoviesViewHolder>() {

    inner class MoviesViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {

        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            MovieItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }



    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val result = differ.currentList[position]
        holder.binding.apply {
            movieName.text = result.title
            try {
                Glide.with(root).load("https://image.tmdb.org/t/p/w185" + result.poster_path)
                    .placeholder(R.drawable.defult)
                    .error(R.drawable.defult)
                    .into(movieImage)
            } catch (e: Exception) {
                Log.i("poster", "poster is null" + e )
            }

        }
        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { it(result) }
            }
        }
    }

    private var onItemClickListener: ((Result) -> Unit)? = null

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }

}