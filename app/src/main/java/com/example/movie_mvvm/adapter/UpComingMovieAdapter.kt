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
import com.example.movie_mvvm.databinding.UpcomingMovieListItemBinding
import com.example.movie_mvvm.utils.Constant

class UpComingMovieAdapter(
    private val typeface: Typeface,
    private val listener: OnItemClickListener
) :
    PagingDataAdapter<MovieModel, UpComingMovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(movieModel: MovieModel)
    }

    inner class MovieViewHolder(private val binding: UpcomingMovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.includedUpcoming.apply {
                textViewTitle.typeface = typeface
                textViewDate.typeface = typeface
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
            binding.includedUpcoming.apply {
                Glide.with(itemView)
                    .load("${Constant.IMAGE_URL}${currentMovie?.poster_path}")
                    .error(R.drawable.ic_baseline_image_24)
                    .into(imageView)

                textViewTitle.text = currentMovie?.title ?: "venom"
                textViewDate.text = currentMovie?.release_date ?: "10/17"
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
            UpcomingMovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie: MovieModel? = getItem(position)
        holder.bind(currentMovie)
    }
}