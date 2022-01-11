package com.example.trendingrepo.di

import android.app.Application
import androidx.room.Room
import androidx.work.WorkManager
import com.example.trendingrepo.api.GithubApi
import com.example.trendingrepo.data.local.GithubRepoDao
import com.example.trendingrepo.data.local.GithubRepoDatabase
import com.example.trendingrepo.data.local.GithubReposLocalRepository
import com.example.trendingrepo.data.remote.GithubRemoteRepository
import com.example.trendingrepo.utils.Constants.GITHUB_REPOS_DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWorkManager(app: Application): WorkManager = WorkManager.getInstance(app)

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(GithubApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideGithubRepoApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

    @Singleton
    @Provides
    fun provideGithubRepoDatabase(
        app: Application
    ) = Room.databaseBuilder(app, GithubRepoDatabase::class.java, GITHUB_REPOS_DB_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideGithubRepoDao(githubRepoDb: GithubRepoDatabase): GithubRepoDao =
        githubRepoDb.githubRepoDao()

    @Singleton
    @Provides
    fun provideGithubReposLocalRepository(githubRepoDao: GithubRepoDao) =
        GithubReposLocalRepository(githubRepoDao)

    @Singleton
    @Provides
    fun provideGithubReposRemoteRepository(
        githubApi: GithubApi,
        githubRepoDb: GithubRepoDatabase,
        githubReposLocalRepository: GithubReposLocalRepository
    ) = GithubRemoteRepository(githubApi, githubRepoDb, githubReposLocalRepository)
}