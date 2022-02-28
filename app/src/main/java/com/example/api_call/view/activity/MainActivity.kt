package com.example.api_call.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.api_call.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListener()
    }

    private fun setListener() {
        btnSimpleApiFetching.setOnClickListener {
            val intentSimpleApiCall=Intent(this, ApiCallSimpleActivity::class.java)
            startActivity(intentSimpleApiCall)

        }
        btnApiFetchingUsingPaging.setOnClickListener {
            val intentApiCallPagination=Intent(this, ApiCallWithPaginationActivity::class.java)
            startActivity(intentApiCallPagination)
        }
        btnApiCallWithJson.setOnClickListener {
            val intentApiCallJson=Intent(this, ApiCallWithJsonActivity::class.java)
            startActivity(intentApiCallJson)
        }
        btnApiWithMultipart.setOnClickListener {
            val intentApiCallWithMultipart=Intent(this, ApiCallWithMultipartActivity::class.java)
            startActivity(intentApiCallWithMultipart)
        }
    }
}