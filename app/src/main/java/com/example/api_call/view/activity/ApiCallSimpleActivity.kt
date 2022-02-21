package com.example.api_call.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api_call.ApiInterface
import com.example.api_call.R
import com.example.api_call.adapter.PageInfoAdapter
import com.example.api_call.model.PageList
import kotlinx.android.synthetic.main.activity_api_call_simple.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiCallSimpleActivity : AppCompatActivity() {
  companion object{
      var selectedCount=0
  }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_call_simple)
        setTextChangeListener()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title=""
    }


    private fun setTextChangeListener() {
        tetPageNumber.addTextChangedListener {
            if (tetPageNumber.text.toString().trim().isNotEmpty()) {
                btnFetchData.isClickable = true
                btnFetchData.setBackgroundResource(R.drawable.round_button_with_purple_color)
                setButtonListener()
            } else {
                btnFetchData.isClickable = false
                btnFetchData.setBackgroundResource(R.drawable.round_button_with_gray_color)
            }
        }
    }

    private fun setButtonListener() {
        btnFetchData.setOnClickListener {
            getPageData()
        }
    }

    private fun getPageData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://hn.algolia.com/api/v1/")
            .build()
            .create(ApiInterface::class.java)
        val pageData = retrofitBuilder.getData("story", tetPageNumber.text.toString())
        pageData.enqueue(object : Callback<PageList?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<PageList?>, response: Response<PageList?>) {

                val pageBody = response.body()
                tvError.visibility=View.GONE
                rvPageData.visibility=View.VISIBLE
                val pageInfoAdapter = PageInfoAdapter(this@ApiCallSimpleActivity, pageBody!!.pageList)
                rvPageData.layoutManager = LinearLayoutManager(this@ApiCallSimpleActivity)
                pageInfoAdapter.notifyDataSetChanged()
                rvPageData.adapter = pageInfoAdapter
                selectedCount=0

            }

            override fun onFailure(call: Call<PageList?>, t: Throwable) {
                rvPageData.visibility=View.GONE
                tvError.visibility=View.VISIBLE
                tvError.text=t.message.toString()

            }
        })
    }
}

