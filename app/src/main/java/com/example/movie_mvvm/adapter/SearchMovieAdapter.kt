package com.example.movie_mvvm.adapter

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

class SearchMovieAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<MovieModel, SearchMovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(movieModel: MovieModel)
    }

    inner class MovieViewHolder(private val binding: SearchMovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
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
            binding.apply {
                Glide.with(itemView)
                    .load("$IMAGE_URL${currentMovie?.poster_path}")
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewSearch)
                tvMovieTitle.text = currentMovie?.original_title
                tvMovieDirector.text = currentMovie?.release_date
                tvMovieDuration.text = itemView.context.getString(R.string.fake_duration)
                tvMovieGenre.text = itemView.context.getString(R.string.genre)
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






