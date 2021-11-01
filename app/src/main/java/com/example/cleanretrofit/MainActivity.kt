package com.example.cleanretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cleanretrofit.network.POJOMovie
import com.example.cleanretrofit.network.QuestApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class MainActivity : AppCompatActivity() {
    // для чистки запросов RxJava
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val questApi = (this.application as MyApplication).questApi

        compositeDisposable.add(questApi.getSearchMovie("Total war")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                       Log.i("TAG", "$it")
            },{
                Log.i("TAG", "$it")
            }))





    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}

