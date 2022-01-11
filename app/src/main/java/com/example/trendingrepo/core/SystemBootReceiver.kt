package com.example.trendingrepo.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.trendingrepo.utils.Constants
import com.example.trendingrepo.worker.GithubRepoWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SystemBootReceiver : BroadcastReceiver(), Configuration.Provider {
    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory
    @Inject
    lateinit var workManager: WorkManager

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            initWorker()

        }
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(hiltWorkerFactory)
        .setMinimumLoggingLevel(android.util.Log.DEBUG)
        .build()

    private fun initWorker() {
        if (!isWorkScheduled(Constants.GITHUB_REPOS_FETCH_WORK_NAME)) {
            val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val periodicWorkRequest =
                PeriodicWorkRequestBuilder<GithubRepoWorker>(15, TimeUnit.MINUTES)
                    .addTag(Constants.GITHUB_REPOS_FETCH_WORK_NAME)
                    .setConstraints(workConstraints)
                    .build()

            workManager.getWorkInfosForUniqueWork(Constants.GITHUB_REPOS_FETCH_WORK_NAME).isDone

            workManager.enqueueUniquePeriodicWork(
                Constants.GITHUB_REPOS_FETCH_WORK_NAME,
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