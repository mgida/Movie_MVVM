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
//    private val viewModel: MovieViewModel,
//    private val lifecycleOwner: LifecycleOwner
) :
    PagingDataAdapter<MovieModel, SearchMovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(movieModel: MovieModel)
    }

    inner class MovieViewHolder(private val binding: SearchMovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        private var isFav = false

        init {
            binding.tvMovieTitle.typeface = typeface
            binding.tvMovieDirector.typeface = typeface
            binding.tvMovieDuration.typeface = typeface
            binding.tvMovieGenre.typeface = typeface

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

//                viewModel.movieInFav.observe(lifecycleOwner, Observer {
//                    if (it != null) {
//                        isFav = true
//                        imageButton.setBackgroundResource(R.drawable.ic_baseline_bookmark_24)
//                    }
//                })
                Glide.with(itemView)
                    .load("$IMAGE_URL${currentMovie?.poster_path}")
                    .placeholder(R.color.purple_500)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewSearch)
                tvMovieTitle.text = currentMovie?.original_title
                tvMovieDirector.text = currentMovie?.release_date
                tvMovieDuration.text = itemView.context.getString(R.string.fake_duration)
                tvMovieGenre.text = itemView.context.getString(R.string.genre)
                ratingBar.rating = (currentMovie?.vote_average)!!.toFloat()


//                imageButton.setOnClickListener {
//                    isFav = !isFav
//
//                    if (isFav) {
//                        saveMovieToFavourites(itemView.context, currentMovie)
//                        it.setBackgroundResource(R.drawable.ic_baseline_bookmark_24)
//                    } else {
//                        deleteMovieFromFavourites(itemView.context, currentMovie)
//                        it.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24)
//                    }
//                }
            }
        }

    }

//    private fun deleteMovieFromFavourites(context: Context?, currentMovie: MovieModel) {
//        viewModel.deleteMovie(currentMovie)
//        Toast.makeText(context, "deleted ..", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun saveMovieToFavourites(context: Context, movieModel: MovieModel) {
//        viewModel.insertMovie(movieModel)
//        Toast.makeText(context, "Saved ..", Toast.LENGTH_SHORT).show()
//    }

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
        // viewModel.selectMovieById(currentMovie!!.id)
        holder.bind(currentMovie)
    }

}






