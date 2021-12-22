package com.example.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker3(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val TAG = "workmng3"
    override fun doWork(): Result {
        Log.d(TAG, "doWork3: start")
        // когда задача будет запущена, то внутри ее (в MyWorker1.java) мы можем получить эти
        // входные данные так:
        val valueA = inputData.getString("keyA")
        val valueB = inputData.getInt("keyB", 0)

        // Выходные данные, чтобы задача вернула данные, необходимо передать их в метод setOutputData
        /*
        А у Data.Builder есть метод putAll(Map<String, Object> values), в который вы можете передать
        Map, все данные из которого будут помещены в Data.
        У объекта Data, который хранит данные, есть метод getKeyValueMap, который вернет вам
        immutable Map, содержащий все данные этого Data.
        */
        val output = Data.Builder()
            .putString("keyC", "value11")
            .putInt("keyD", 11)
            .build()


        Log.d(TAG, "Получаем данные: valueA $valueA valueB $valueB")
        Log.d(TAG, "doWork3: end")
        // тут возвращаем наши данные виесте с output
        return Result.success(output)
    }

    override fun onStopped() {
        super.onStopped()
        Log.d(TAG, "doWork3: onStopped")
    }
}

class MyWorker3_1(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val TAG = "workmng3"
    override fun doWork(): Result {
        Log.d(TAG, "doWork3_1: start")

        val valueA = inputData.getString("keyC")
        val valueB = inputData.getInt("keyD", 0)

        Log.d(TAG, "Получаем данные doWork3_1: valueA $valueA valueB $valueB")
        Log.d(TAG, "doWork3_1: end")


        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        Log.d(TAG, "doWork3_1: onStopped")
    }
}