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
import com.ahmedr.movies_app.model.response.Result
import com.ahmedr.movies_app.ui.MoviesViewModel
import com.bumptech.glide.Glide

class MoviesVerticalAdapter ( var viewModel: MoviesViewModel): RecyclerView.Adapter<MoviesVerticalAdapter.MoviesViewHolder>() {

    inner class MoviesViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.apply {
                posterMovieName.text = result.title
                try {
                    Glide.with(root).load("https://image.tmdb.org/t/p/w500" + result.backdrop_path)
                        .placeholder(R.drawable.defult)
                        .error(R.drawable.defult)
                        .into(moviePoster)
                } catch (e: Exception) {
                    Log.i("poster", "poster is null")
                }
                parentHor.animation =
                    AnimationUtils.loadAnimation(itemView.context, R.anim.hor_rv_anim)

                rvSaveBtn.setOnClickListener {
                    viewModel.saveMovie(result)
                }
            }
            itemView.apply {
                setOnClickListener {
                    onItemClickListener?.let { it(result) }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.m_id == newItem.m_id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            ItemNewsBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val result = differ.currentList[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private var onItemClickListener: ((Result) -> Unit)? = null

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }


}