package com.example.movie_mvvm.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie_mvvm.R
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.databinding.SearchMovieListItemBinding
import com.example.movie_mvvm.utils.Constant.Companion.IMAGE_URL

class SearchMovieAdapter(
    private val typeface: Typeface,
    private val listener: OnItemClickListener,
) :
    PagingDataAdapter<MovieModel, SearchMovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(movieModel: MovieModel)
    }

    inner class MovieViewHolder(private val binding: SearchMovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.includedSearch.apply {
                tvMovieTitle.typeface = typeface
                tvMovieDate.typeface = typeface
                tvMovieOverview.typeface = typeface
                tvMovieRate.typeface = typeface
            }
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentMovie = getItem(position)
                    currentMovie?.let {
                        listener.onItemClick(it)
                    }
                }
            }
        }

        fun bind(currentMovie: MovieModel?) {

            binding.includedSearch.apply {
                Glide.with(itemView)
                    .load("$IMAGE_URL${currentMovie?.poster_path}")
                    .placeholder(R.color.blackColor)
                    .error(R.drawable.ic_baseline_image_24)
                    .into(imageView)
                tvMovieTitle.text = currentMovie?.original_title
                tvMovieDate.text = currentMovie?.release_date
                tvMovieOverview.text = currentMovie?.overview
                tvMovieRate.text = currentMovie?.vote_average.toString()
                ratingBar.rating = (currentMovie?.vote_average)!!.toFloat()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieModel>() {
            override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel) =
                oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        return MovieViewHolder(
            SearchMovieListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie: MovieModel? = getItem(position)
        holder.bind(currentMovie)
    }

}






