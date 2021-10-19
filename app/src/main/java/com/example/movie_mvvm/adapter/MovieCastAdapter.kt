package com.example.movie_mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_mvvm.data.model.cast.CastModel
import com.example.movie_mvvm.databinding.MovieCastListItemBinding

class MovieCastAdapter : RecyclerView.Adapter<MovieCastAdapter.MovieCastViewHolder>() {

    class MovieCastViewHolder(private val binding: MovieCastListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(castModel: CastModel) {
            binding.apply {

                textViewTitleCast.text = castModel.original_name
                textViewPopularityCast.text = castModel.popularity.toString()
            }
        }
    }

    private val comparator = object : DiffUtil.ItemCallback<CastModel>() {
        override fun areItemsTheSame(oldItem: CastModel, newItem: CastModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CastModel, newItem: CastModel) =
            oldItem.id == newItem.id
    }
    val differ = AsyncListDiffer(this, comparator)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCastViewHolder {
        return MovieCastViewHolder(
            MovieCastListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun onBindViewHolder(holder: MovieCastViewHolder, position: Int) {
        val currentCast: CastModel? = differ.currentList[position]
        currentCast?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount() = differ.currentList.size
}
