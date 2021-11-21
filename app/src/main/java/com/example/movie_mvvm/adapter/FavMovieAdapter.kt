package com.example.movie_mvvm.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie_mvvm.R

import com.example.movie_mvvm.data.model.MovieModel
import com.example.movie_mvvm.databinding.FavMovieListItemBinding
import com.example.movie_mvvm.utils.Constant

class FavMovieAdapter(private val typeface: Typeface, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<FavMovieAdapter.FavMovieViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(movie: MovieModel?)
    }


    inner class FavMovieViewHolder(private val binding: FavMovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.tvMovieTitleFav.typeface = typeface
            binding.tvMovieDirectorFav.typeface = typeface
            binding.tvMovieDurationFav.typeface = typeface
            binding.tvMovieGenreFav.typeface = typeface

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentFavMovie = differ.currentList[position]
                    currentFavMovie?.let {
                        listener.onItemClick(it)
                    }
                }
            }
        }

        fun bind(currentMovie: MovieModel?) {
            binding.apply {

                Glide.with(itemView)
                    .load("${Constant.IMAGE_URL}${currentMovie?.poster_path}")
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewSearchFav)
                tvMovieTitleFav.text = currentMovie?.original_title
                tvMovieDirectorFav.text = currentMovie?.release_date
                tvMovieDurationFav.text = itemView.context.getString(R.string.fake_duration)
                tvMovieGenreFav.text = itemView.context.getString(R.string.genre)
                ratingBarFav.rating = (currentMovie?.vote_average)!!.toFloat()
            }
        }
    }

    private val comparator = object : DiffUtil.ItemCallback<MovieModel>() {
        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel) =
            oldItem.id == newItem.id
    }
    val differ = AsyncListDiffer(this, comparator)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavMovieViewHolder {
        return FavMovieViewHolder(
            FavMovieListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun onBindViewHolder(holder: FavMovieViewHolder, position: Int) {
        val currentFavMovie: MovieModel? = differ.currentList[position]
        currentFavMovie?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount() = differ.currentList.size
}
