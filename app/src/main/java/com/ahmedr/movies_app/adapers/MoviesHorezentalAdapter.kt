package com.ahmedr.movies_app.adapers

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedr.movies_app.R
import com.ahmedr.movies_app.model.response.Result
import com.ahmedr.movies_app.databinding.MovieItemBinding
import com.bumptech.glide.Glide

class MoviesHorezentalAdapter : RecyclerView.Adapter<MoviesHorezentalAdapter.MoviesViewHolder>() {
    inner class MoviesViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)

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
        return MoviesViewHolder(MovieItemBinding
            .inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val result = differ.currentList[position]

            holder.binding.apply {
                movieName.text = result.title
                try {
                    Glide.with(root).load("https://image.tmdb.org/t/p/w500"+result.poster_path).into(movieImage)

                }catch (e : Exception){
                    Log.i("poster","poster is null")
                }
                parentVer.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.ver_anim)
                holder.itemView.apply {
                    setOnClickListener {
                        onItemClickListener?.let { it(result) }
                    }
                }
            }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Result) -> Unit)? = null

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }

}