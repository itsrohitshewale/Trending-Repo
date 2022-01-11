package com.example.trendingrepo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingrepo.R
import com.example.trendingrepo.adapter.GithubRepoAdapter
import com.example.trendingrepo.data.GithubRepo
import com.example.trendingrepo.databinding.ActivityMainBinding
import com.example.trendingrepo.viewmodel.GithubViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val KEY_REPO = "com.example.trendingrepo.ui.MainActivity.github_repo"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), GithubRepoAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var githubReposViewModel: GithubViewModel
    private lateinit var githubRepoAdapter: GithubRepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        githubReposViewModel = ViewModelProvider(this).get(GithubViewModel::class.java)
        val view = binding.root
        setContentView(view)
        init()
        setListeners()
    }

    private fun init() {
        githubRepoAdapter = GithubRepoAdapter(this)
        binding.apply {
            rvGithubTrendingReposList.layoutManager = LinearLayoutManager(applicationContext)
            rvGithubTrendingReposList.setHasFixedSize(true)
            rvGithubTrendingReposList.adapter = githubRepoAdapter
        }
        showCachedReposDbData()
    }

    private fun setListeners() {
        githubReposViewModel.githubPagedTrendingAndroidRepos.observe(this) {
            githubRepoAdapter.submitData(this.lifecycle, it)
            changeVisibilityIfReposPresent()
        }
    }

    private fun showCachedReposDbData() {
        lifecycleScope.launch {
            githubRepoAdapter.submitData(PagingData.from(githubReposViewModel.getGithubTrendingAndroidRepos()))
            changeVisibilityIfReposPresent()
        }
    }

    private fun changeVisibilityIfReposPresent() {
        if (githubRepoAdapter.itemCount == 0) {
            binding.apply {
                rvGithubTrendingReposList.visibility = View.GONE
                pbLoadRepos.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                rvGithubTrendingReposList.visibility = View.VISIBLE
                pbLoadRepos.visibility = View.GONE
            }
        }
    }

    override fun onItemClick(githubRepo: GithubRepo) {
        var githubRepoDetailIntent = Intent(this, DetailActivity::class.java)
        githubRepoDetailIntent.putExtra(KEY_REPO, githubRepo)
        startActivity(githubRepoDetailIntent)
    }
}