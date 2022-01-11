package com.example.trendingrepo

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.trendingrepo.utils.Constants.GITHUB_REPOS_FETCH_WORK_NAME
import com.example.trendingrepo.worker.GithubRepoWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class TrendingApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory
    @Inject
    lateinit var workManager: WorkManager

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(hiltWorkerFactory)
        .setMinimumLoggingLevel(Log.DEBUG)
        .build()

    override fun onCreate() {
        super.onCreate()
        initWorker()
    }

    private fun initWorker() {
        if (!isWorkScheduled(GITHUB_REPOS_FETCH_WORK_NAME)) {
            Log.e("WORK", "Inside init")
            val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val periodicWorkRequest =
                PeriodicWorkRequestBuilder<GithubRepoWorker>(15, TimeUnit.MINUTES)
                    .addTag(GITHUB_REPOS_FETCH_WORK_NAME)
                    .setConstraints(workConstraints)
                    .build()

            workManager.getWorkInfosForUniqueWork(GITHUB_REPOS_FETCH_WORK_NAME).isDone

            workManager.enqueueUniquePeriodicWork(
                GITHUB_REPOS_FETCH_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }

    private fun isWorkScheduled(tag: String): Boolean {
        val statuses = workManager.getWorkInfosByTag(tag);
        return try {
            val workInfoList = statuses.get();
            for (workInfo in workInfoList) {
                val state = workInfo.state;
                state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED;
            }
            false
        }
        catch (ex: ExecutionException) {
            ex.printStackTrace();
            false
        }
        catch (ex: InterruptedException) {
            ex.printStackTrace();
            false
        }
    }
}