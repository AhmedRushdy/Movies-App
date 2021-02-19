//package com.ahmedr.ma_task1.adapers
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//
//class MoviesVerticalAdapter RecyclerView.Adapter<MoviesVerticalAdapter.NewsViewHolder>() {
//
//    inner class MoviesVerticalAdapter(val binding: ItemNewsBinding) : RecyclerView
//    .ViewHolder(binding.root)
//
//
//
//    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
//        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return newItem.url == oldItem.url
//        }
//
//        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return newItem == oldItem
//        }
//
//    }
//
//    val differ = AsyncListDiffer(this, differCallback)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
//        return NewsViewHolder(ItemNewsBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false))
//    }
//    override fun getItemCount(): Int {
//        return differ.currentList.size
//    }
//
//    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
//        val article = differ.currentList[position]
//
//        holder.binding.apply {
//
//            articleTitle.text = article.title
//            describtion.text = article.description
//            date.text = article.publishedAt
//            source.text = article.source?.name
//            Picasso.get().load(article.urlToImage).into(ivNews)
//
//
//        }
//        holder.itemView.apply {
//            setOnClickListener {
//                onItemClickListener?.let { it(article) }
//            }
//
//        }
//    }
//
//
//    private var onItemClickListener:((Article) -> Unit)? = null
//
//    fun setOnItemClickListener(listener:(Article)->Unit){
//        onItemClickListener = listener
//    }
//
//}