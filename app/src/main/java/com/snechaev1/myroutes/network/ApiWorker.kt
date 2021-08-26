package com.snechaev1.myroutes.network

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

class ApiWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {

        if (runAttemptCount > 10) {
            Timber.d("too many failed attempts, give up")
            return Result.failure()
        }
        val imageUriInput =
            inputData.getString("IMAGE_URI") ?: return Result.failure()

//        uploadFile(imageUriInput)

        return Result.success()
    }
}
