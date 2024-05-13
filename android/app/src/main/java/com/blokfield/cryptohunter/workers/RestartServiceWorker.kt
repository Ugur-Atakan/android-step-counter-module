package com.blokfield.cryptohunter.workers
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.blokfield.cryptohunter.services.BackgroundService

class RestartServiceWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    override fun doWork(): Result {
        val serviceIntent = Intent(applicationContext, BackgroundService::class.java)
        // applicationContext.startForegroundService(serviceIntent)
        ContextCompat.startForegroundService(applicationContext, serviceIntent)
        return Result.success()
    }
}
