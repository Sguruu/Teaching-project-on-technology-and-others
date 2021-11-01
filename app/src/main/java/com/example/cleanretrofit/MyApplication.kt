package com.example.cleanretrofit

import android.app.Application
import com.example.cleanretrofit.network.QuestApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {
    //    object RetrofitAPI {
//        //http://www.omdbapi.com/?apikey=[yourkey]&
//        //2798e50c
//        private val HOST = "www.omdbapi.com"
//        private val KEY = "2798e50c"
//        val retrofit: Retrofit = Retrofit.Builder()
//            .baseUrl("${HOST}/?apikey${KEY}")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
    lateinit var questApi: QuestApi

    override fun onCreate() {
        super.onCreate()

        configureRetrofit()
    }

    private fun configureRetrofit() {
        // для логирования запросов
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        // указываем что выводить
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        questApi = retrofit.create(QuestApi::class.java)
    }


}