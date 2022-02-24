package com.example.api_call.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api_call.ApiInterface
import com.example.api_call.R
import com.example.api_call.adapter.PageInfoAdapter
import com.example.api_call.adapter.PageInfoWithPaginationAdapter
import com.example.api_call.model.PageList
import com.example.api_call.model.PageModel
import kotlinx.android.synthetic.main.activity_api_call_simple.*
import kotlinx.android.synthetic.main.activity_api_call_with_pagination.*
import kotlinx.android.synthetic.main.activity_api_call_with_pagination.pbWaiting
import kotlinx.android.synthetic.main.activity_api_call_with_pagination.rvPageData
import kotlinx.android.synthetic.main.activity_api_call_with_pagination.tvError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiCallWithPaginationActivity : AppCompatActivity() {
    companion object {
        var selectedCount = 0
    }

    private var page = 1
    private val pageLimit = 100
    private val pageDataList = ArrayList<PageModel>()
    val pageInfoAdapter = PageInfoWithPaginationAdapter(this@ApiCallWithPaginationActivity, pageDataList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_call_with_pagination)
        setListener()
        setAdapter()
    }

    private fun init() {
        if (checkForInternet(this)) {
            selectedCount=0
            getPageData(page, pageLimit)
        } else {
            tvError.visibility = View.VISIBLE
            tvError.text = getString(R.string.no_internet_error)
        }
    }

    private fun setListener() {
        nsv.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (checkForInternet(this)) {
                    page++
                    pbWaiting.visibility = View.VISIBLE
                    getPageData(page, pageLimit)
                } else {
                    tvError.visibility = View.VISIBLE
                    tvError.text = getString(R.string.no_internet_error)
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        init()
    }
    private fun getPageData(page: Int, pageLimit: Int) {
        if (page > pageLimit) {
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show()
            pbWaiting.visibility = View.GONE
            return

        }
        pbWaiting.visibility = View.VISIBLE
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://hn.algolia.com/api/v1/")
            .build()
            .create(ApiInterface::class.java)
        val pageData = retrofitBuilder.getData("story", page.toString())
        pageData.enqueue(object : Callback<PageList?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<PageList?>, response: Response<PageList?>) {
                val pageBody = response.body()
                if (pageBody?.pageList?.isEmpty() == true) {
                    pbWaiting.visibility = View.GONE
                    tvError.visibility = View.VISIBLE
                    tvError.text = getString(R.string.page_empty)
                } else {
                    pbWaiting.visibility = View.GONE
                    tvError.visibility = View.GONE
                    pageDataList.addAll(pageBody!!.pageList)
                    pageInfoAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<PageList?>, t: Throwable) {
                pbWaiting.visibility = View.GONE
                tvError.visibility = View.VISIBLE
                tvError.text = t.message.toString()
            }
        })
    }

    private fun setAdapter() {
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = selectedCount.toString()
        pageInfoAdapter.setHasStableIds(true)
        rvPageData.layoutManager = LinearLayoutManager(this@ApiCallWithPaginationActivity)
        rvPageData.adapter = pageInfoAdapter
    }

    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}