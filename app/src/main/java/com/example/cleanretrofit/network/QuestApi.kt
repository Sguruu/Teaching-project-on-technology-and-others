package com.example.cleanretrofit.network


import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.*

interface QuestApi {
    companion object {
        const val PATH = "/?"
        const val KEY_API = "apikey=2798e50c"
    }

    /*
    В аннотации GET указываем, что к базовому URL надо будет добавить ""
    , чтобы получилась нужная нам ссылка.
     */
    @GET("${PATH}${KEY_API}")
    // для RX_Java вместо Call указываем Single
    // @Headers("Content-Type: application/json")
    fun getSearchMovie(
        @Query("s") searchMovie: String,
        @Query("page") page: Int = 1,
    ): Single<List<POJOMovie>>
}