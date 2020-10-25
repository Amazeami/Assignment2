package com.example.assignment.api.responses.inner

import com.example.assignment.models.Review
import com.google.gson.annotations.SerializedName

class ReviewsResponse {
    @SerializedName("results")
    val reviews:List<Review>?=null
}