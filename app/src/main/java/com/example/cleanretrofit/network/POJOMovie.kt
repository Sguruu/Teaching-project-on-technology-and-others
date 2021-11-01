package com.example.cleanretrofit.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * POJO_Movie
 */
data class POJOMovie(
    val search: List<POJOMovieDetail>,
)

data class POJOMovieDetail(
    @SerializedName("Title")
    val title: String?,
    @SerializedName("Year")
    val year: String?,
    @SerializedName("imdbID")
    val id: String?,
    @SerializedName("Type")
    val type: String?,
    @SerializedName("Poster")
    val poster: String?,
)
