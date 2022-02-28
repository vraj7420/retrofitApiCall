package com.example.api_call.network

import com.example.api_call.model.PageModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiPostApiInterface {
    @POST("search_by_date")
    fun postData(@Body page:PageModel):Call<PageModel>
}