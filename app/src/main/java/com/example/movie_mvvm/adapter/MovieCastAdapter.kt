package com.example.movie_mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie_mvvm.R
import com.example.movie_mvvm.data.model.cast.CastModel
import com.example.movie_mvvm.databinding.MovieCastListItemBinding
import com.example.movie_mvvm.utils.Constant.Companion.IMAGE_URL

class MovieCastAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<MovieCastAdapter.MovieCastViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(castModel: CastModel?)
    }


    inner class MovieCastViewHolder(private val binding: MovieCastListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentCast = differ.currentList[position]
                    currentCast?.let {
                        listener.onItemClick(it)
                    }
                }
            }
        }

        fun bind(castModel: CastModel?) {
            binding.apply {
                Glide.with(itemView)
                    .load("$IMAGE_URL${castModel?.profile_path}")
                    .placeholder(R.color.white)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(ivCast)
                textViewTitleCast.text = castModel?.original_name
                textViewPopularityCast.text = castModel?.popularity.toString()
            }
        }
    }

    private val comparator = object : DiffUtil.ItemCallback<CastModel>() {
        override fun areItemsTheSame(oldItem: CastModel, newItem: CastModel) =
            oldItem.cast_id == newItem.cast_id

        override fun areContentsTheSame(oldItem: CastModel, newItem: CastModel) =
            oldItem.cast_id == newItem.cast_id
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
