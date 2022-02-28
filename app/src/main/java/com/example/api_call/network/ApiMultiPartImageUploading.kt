package com.example.api_call.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiMultiPartImageUploading {
    @Multipart
    @POST("search_by_date")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("filename") name: RequestBody
    ): Call<String>
}