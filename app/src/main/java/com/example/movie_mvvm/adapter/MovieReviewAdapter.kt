package com.example.movie_mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_mvvm.data.model.review.ReviewModel
import com.example.movie_mvvm.databinding.MovieReviewListItemBinding

class MovieReviewAdapter : RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder>() {

    class MovieReviewViewHolder(private val binding: MovieReviewListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieReview: ReviewModel) {
            binding.apply {
                textViewReview.text = movieReview.content
                textViewReviewAuthor.text = movieReview.author
            }
        }
    }

    private val comparator = object : DiffUtil.ItemCallback<ReviewModel>() {
        override fun areItemsTheSame(oldItem: ReviewModel, newItem: ReviewModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ReviewModel, newItem: ReviewModel) =
            oldItem.id == newItem.id
    }
    val differ = AsyncListDiffer(this, comparator)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieReviewViewHolder {
        return MovieReviewViewHolder(
            MovieReviewListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun onBindViewHolder(holder: MovieReviewViewHolder, position: Int) {
        val currentReview: ReviewModel? = differ.currentList[position]
        currentReview?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount() = differ.currentList.size
}
