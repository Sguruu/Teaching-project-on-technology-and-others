package com.example.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker2 (appContext: Context, workerParams: WorkerParameters) :
Worker(appContext, workerParams){
    private val TAG = "workmng2"
    override fun doWork(): Result {
        Log.d(TAG, "doWork2: start")
        Log.d(TAG, "doWork2: end")
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        Log.d(TAG, "doWork2: onStopped")
    }

}

class MyWorker2_1 (appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams){
    private val TAG = "workmng2"
    override fun doWork(): Result {
        Log.d(TAG, "doWork2_1: start")
        Log.d(TAG, "doWork2_1: end")
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        Log.d(TAG, "doWork2_1: onStopped")
    }

}

class MyWorker2_2 (appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams){
    private val TAG = "workmng2"
    override fun doWork(): Result {
        Log.d(TAG, "doWork2_2: start")
        Log.d(TAG, "doWork2_2: end")
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        Log.d(TAG, "doWork2_2: onStopped")
    }

}

class MyWorker2_3 (appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams){
    private val TAG = "workmng2"
    override fun doWork(): Result {
        Log.d(TAG, "doWork2_3: start")
        Log.d(TAG, "doWork2_3: end")
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        Log.d(TAG, "doWork2_3: onStopped")
    }

}

class MyWorker2_4 (appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams){
    private val TAG = "workmng2"
    override fun doWork(): Result {
        Log.d(TAG, "doWork2_4: start")
        Log.d(TAG, "doWork2_4: end")
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        Log.d(TAG, "doWork2_4: onStopped")
    }

}

class MyWorker2_5 (appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams){
    private val TAG = "workmng2"
    override fun doWork(): Result {
        Log.d(TAG, "doWork2_5: start")
        Log.d(TAG, "doWork2_5: end")
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        Log.d(TAG, "doWork2_5: onStopped")
    }

}