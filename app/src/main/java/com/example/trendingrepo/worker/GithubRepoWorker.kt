package com.example.trendingrepo.worker

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.trendingrepo.R
import com.example.trendingrepo.data.remote.GithubRemoteRepository
import com.example.trendingrepo.utils.Utilities
import java.lang.Exception

class GithubRepoWorker @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted workParams: WorkerParameters,
    val githubRemoteRepository: GithubRemoteRepository
) : CoroutineWorker(appContext, workParams) {

    override suspend fun doWork(): Result {
        return try {
            Log.e("IN doWork", "Received callback ")
            githubRemoteRepository.fetchSaveTrendingRepos()
            Utilities.sendNotification(applicationContext,
                applicationContext.getString(R.string.app_name),
                applicationContext.getString(R.string.notification_message))
            Result.success()
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure()
        }
    }
}