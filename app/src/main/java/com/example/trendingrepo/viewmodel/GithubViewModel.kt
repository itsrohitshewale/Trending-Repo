package com.example.trendingrepo.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.trendingrepo.data.local.GithubReposLocalRepository

class GithubViewModel @ViewModelInject constructor(
    private val githubReposLocalRepository: GithubReposLocalRepository
) : ViewModel() {
    val githubPagedTrendingAndroidRepos = githubReposLocalRepository.getPagedTrendingRepos().liveData.cachedIn(viewModelScope)

    suspend fun getGithubTrendingAndroidRepos() = githubReposLocalRepository.getTrendingRepos()
}