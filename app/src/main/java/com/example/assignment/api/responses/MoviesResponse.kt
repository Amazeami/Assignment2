package com.example.assignment.api.responses


import com.example.assignment.models.Movie
import com.google.gson.annotations.SerializedName

//response for search,popular,top-rated & upcoming requests
class MoviesResponse{

    @SerializedName("page")
    var page:Int=1

    @SerializedName("total_results")
    val totalResults:Int=0

    @SerializedName("total_pages")
    val total_pages:Int=0

    @SerializedName("results")
    val movies:List<Movie>?=null
}