package com.example.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class MyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val TAG = "workmng"

    override fun doWork(): Result {
        Log.d(TAG, "doWork: start")
        // Тут пишется код для выполнения сервиса
        try {
            // ждем 10 секунд
            TimeUnit.SECONDS.sleep(10)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Log.d(TAG,"doWork: end")
Result.retry()
        Result.failure()
        Result.retry()
            /*
          Вернуть можно три параметра :
          Result.failure() - Задача завершилась ошибкой
          Result.retry() - Задача завершилась с ошибкой, будет перезапуск
          Result.success - Задача завершилась успехом
            */
        return Result.retry()
    }

    override fun onStopped() {
        // Вызывается при отмене задачи из кода
        super.onStopped()
        Log.d(TAG, "onStopped");
    }
}