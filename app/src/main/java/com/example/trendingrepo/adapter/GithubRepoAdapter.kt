package com.example.trendingrepo.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingrepo.data.GithubRepo
import com.example.trendingrepo.databinding.ItemGithubRepoBinding
import com.example.trendingrepo.ui.DetailActivity
import com.example.trendingrepo.ui.KEY_REPO
import com.example.trendingrepo.utils.Constants.NOT_FOUND

class GithubRepoAdapter(val itemClickListener: OnItemClickListener): PagingDataAdapter<GithubRepo, GithubRepoAdapter.RepoViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object: DiffUtil.ItemCallback<GithubRepo>() {
            override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean = oldItem == newItem
        }
    }

    inner class RepoViewHolder(private val binding: ItemGithubRepoBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val githubRepo = getItem(position)
                    if (githubRepo != null) {
                        itemClickListener.onItemClick(githubRepo)
                    }

                }
            }
        }

        fun bind(githubRepo: GithubRepo?) {
            binding.apply {
                tvRepoFullName.text = githubRepo?.fullName ?: NOT_FOUND
                tvRepoDescription.text = githubRepo?.description ?: NOT_FOUND
                tvStars.text = "${(githubRepo?.watchers ?: 0) / 1000}k"
                tvLanguage.text = githubRepo?.language ?: NOT_FOUND
                tvForks.text = githubRepo?.forks.toString() ?: NOT_FOUND
            }
        }
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val githubRepo = getItem(position)
        holder.bind(githubRepo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemGithubRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClick(githubRepo: GithubRepo)
    }
}