package com.example.trendingrepo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trendingrepo.R
import com.example.trendingrepo.data.GithubRepo
import com.example.trendingrepo.databinding.ActivityDetailBinding
import com.example.trendingrepo.utils.Constants.NOT_FOUND
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
    }

    private fun init() {
        val githubRepo = intent.getParcelableExtra<GithubRepo>(KEY_REPO)
        binding.apply {
            textViewFullName.text = githubRepo?.fullName ?: NOT_FOUND
            textViewDesc.text = githubRepo?.description ?: NOT_FOUND
            textViewStars.text = "${(githubRepo?.watchers ?: 0) / 1000}k"
            textViewLanguage.text = githubRepo?.language ?: NOT_FOUND
            textViewForks.text = "${(githubRepo?.forks ?: 0) / 1000}k"
            textViewOpenIssues.text = "${(githubRepo?.openIssues ?: 0) / 1000}k"
            textViewWatchers.text = "${(githubRepo?.watchers ?: 0) / 1000}k"
            if (githubRepo?.repoOwner?.avatarUrl != null) {
                Picasso.get().load(githubRepo.repoOwner.avatarUrl).error(R.drawable.ic_launcher_foreground).into(imageViewCollapsing);
            }
        }
    }
}