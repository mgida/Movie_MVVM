package com.example.movie_mvvm.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_mvvm.data.model.trailer.TrailerModel
import com.example.movie_mvvm.databinding.MovieTrailerListItemBinding

class MovieTrailerAdapter(private val typeface: Typeface,private val listener: OnItemClickListener) :
    RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(trailerModel: TrailerModel?)
    }


    inner class MovieTrailerViewHolder(private val binding: MovieTrailerListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.textViewTrailerName.typeface = typeface

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentTrailer = differ.currentList[position]
                    currentTrailer?.let {
                        listener.onItemClick(it)
                    }
                }
            }
        }

        fun bind(trailerModel: TrailerModel?) {
            binding.apply {

                textViewTrailerName.text = trailerModel?.name
               // textViewTrailerSite.text = trailerModel?.site
            }
        }
    }

    private val comparator = object : DiffUtil.ItemCallback<TrailerModel>() {
        override fun areItemsTheSame(oldItem: TrailerModel, newItem: TrailerModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TrailerModel, newItem: TrailerModel) =
            oldItem.id == newItem.id
    }
    val differ = AsyncListDiffer(this, comparator)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieTrailerViewHolder {
        return MovieTrailerViewHolder(
            MovieTrailerListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun onBindViewHolder(holder: MovieTrailerViewHolder, position: Int) {
        val currentTrailer: TrailerModel? = differ.currentList[position]
        currentTrailer?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount() = differ.currentList.size
}
