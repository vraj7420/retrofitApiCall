package com.example.api_call.network

import com.example.api_call.model.PageList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("search_by_date")
    fun getData(@Query("tags")tag:String,@Query("page")pageNumber:String):Call<PageList>
}