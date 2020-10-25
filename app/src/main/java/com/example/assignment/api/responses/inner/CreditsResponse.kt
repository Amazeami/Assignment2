package com.example.assignment.api.responses.inner

import com.example.assignment.models.Cast
import com.google.gson.annotations.SerializedName

class CreditsResponse {
    @SerializedName("cast")
    val casts:List<Cast>?=null
}