package com.example.api_call.model

import com.google.gson.annotations.SerializedName

class PageList {
    @SerializedName("hits")
    val pageList=ArrayList<PageModel>()

}