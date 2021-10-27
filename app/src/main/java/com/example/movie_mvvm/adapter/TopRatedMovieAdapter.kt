package com.example.movie_mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie_mvvm.R
import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.databinding.TopRatedMovieListItemBinding
import com.example.movie_mvvm.utils.Constant

class TopRatedMovieAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<MovieModel, TopRatedMovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(movieModel: MovieModel)
    }

    inner class MovieViewHolder(private val binding: TopRatedMovieListItemBinding) :
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
                    .load("${Constant.IMAGE_URL}${currentMovie?.poster_path}")
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewTopRated)

                textViewTitleTopRated.text = currentMovie?.original_title ?: "venom"
                textViewDateTopRated.text = currentMovie?.release_date ?: "10/17"

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
            TopRatedMovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie: MovieModel? = getItem(position)
        holder.bind(currentMovie)
    }
}