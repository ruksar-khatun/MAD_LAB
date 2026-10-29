package com.example.exp12.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.exp12.R
import com.example.exp12.data.model.Article
import com.example.exp12.databinding.ItemNewsArticleBinding
import com.example.exp12.utils.DateUtils

class NewsAdapter(
    private val onArticleClick: (Article) -> Unit
) : ListAdapter<Article, NewsAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemNewsArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ArticleViewHolder(
        private val binding: ItemNewsArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            val context = binding.root.context

            binding.tvTitle.text = article.title ?: "No Title Available"

            if (!article.description.isNullOrBlank()) {
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvDescription.text = article.description
            } else {
                binding.tvDescription.visibility = View.GONE
            }

            binding.tvSource.text = article.source?.name ?: "News Source"
            binding.tvPublishedAt.text = DateUtils.formatDate(article.publishedAt)

            if (!article.category.isNullOrBlank()) {
                binding.tvCategory.visibility = View.VISIBLE
                binding.tvCategory.text = article.category
            } else {
                binding.tvCategory.visibility = View.GONE
            }

            // Load article thumbnail image asynchronously using Glide
            if (!article.urlToImage.isNullOrBlank()) {
                binding.ivArticleImage.visibility = View.VISIBLE
                Glide.with(context)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.ivArticleImage)
            } else {
                binding.ivArticleImage.setImageResource(R.drawable.placeholder_image)
            }

            // Click listener for opening full article
            binding.root.setOnClickListener {
                onArticleClick(article)
            }

            binding.btnReadMore.setOnClickListener {
                onArticleClick(article)
            }

            // Share article link
            binding.btnShare.setOnClickListener {
                val url = article.url
                if (!url.isNullOrBlank()) {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, article.title)
                        putExtra(Intent.EXTRA_TEXT, "${article.title}\n\nRead more at: $url")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share article via"))
                }
            }
        }
    }

    private class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}
