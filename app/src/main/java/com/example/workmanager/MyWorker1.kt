package com.example.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class MyWorker1(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val TAG = "workmng1"

    override fun doWork(): Result {
        Log.d(TAG, "doWork1: start")
        try {
        /*
        Цикл из 10 пауз и логирование статуса isStopped.
        Если задача была остановлена, то выходим с статусом FAILURE.
        Также логируем метод onStopped.
        */
            for (i in 0..9) {
                TimeUnit.SECONDS.sleep(1)
                Log.d(TAG, ", isStopped $isStopped")
                if (isStopped)
                    return Result.failure()
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Log.d(TAG, "doWork1: end")
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        Log.d(TAG, "doWork1: onStopped")
    }
}