package com.example.trendingrepo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trendingrepo.data.GithubRepo

@Database(entities = [GithubRepo::class], version = 1, exportSchema = false)
abstract class GithubRepoDatabase: RoomDatabase() {
    abstract fun githubRepoDao(): GithubRepoDao
}