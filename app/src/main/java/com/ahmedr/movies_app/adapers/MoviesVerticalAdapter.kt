package com.ahmedr.ma_task1.adapers

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedr.movies_app.R
import com.ahmedr.movies_app.databinding.ItemNewsBinding
import com.ahmedr.movies_app.databinding.MovieItemBinding
import com.ahmedr.movies_app.model.response.popular.Result
import com.ahmedr.movies_app.utils.Constants.Companion.BASE_URL
import com.bumptech.glide.Glide

class MoviesVerticalAdapter : RecyclerView.Adapter<MoviesVerticalAdapter.MoviesViewHolder>() {
    inner class MoviesViewHolder(val binding: ItemNewsBinding) :
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
        return MoviesViewHolder(
            ItemNewsBinding
            .inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val result = differ.currentList[position]

        holder.binding.apply {
            posterMovieName.text = result.title
            try {
                Glide.with(root).load("https://image.tmdb.org/t/p/w500"+result.backdrop_path).
                   placeholder(R.drawable.defult)
                    .error(R.drawable.defult)
                    .into(moviePoster);
            }catch (e : Exception){
                Log.i("poster","poster is null")
            }
            parentHor.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.hor_rv_anim)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}