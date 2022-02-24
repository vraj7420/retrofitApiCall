package com.example.api_call.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api_call.R
import com.example.api_call.adapter.PageInfoAdapter
import com.example.api_call.model.PageList
import com.example.api_call.network.BaseService
import kotlinx.android.synthetic.main.activity_api_call_simple.*
import kotlinx.android.synthetic.main.activity_api_call_simple.pbWaiting
import kotlinx.android.synthetic.main.activity_api_call_simple.rvPageData
import kotlinx.android.synthetic.main.activity_api_call_simple.tvError
import kotlinx.android.synthetic.main.activity_api_call_with_pagination.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCallSimpleActivity : AppCompatActivity() {
    companion object {
        var selectedCount = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_call_simple)
        setTextChangeListener()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = ""
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
            if (checkForInternet(this)) {
                getPageData()
            } else {
                swipeRefreshPageData.visibility = View.GONE
                tvError.visibility = View.VISIBLE
                tvError.text = getString(R.string.no_internet_error)
            }
        }
        swipeRefreshPageData.setOnRefreshListener {
            swipeRefreshPageData.visibility = View.GONE
            if (checkForInternet(this)) {
                getPageData()
                swipeRefreshPageData.isRefreshing = false
            } else {
                swipeRefreshPageData.visibility = View.GONE
                tvError.visibility = View.VISIBLE
                tvError.text = getString(R.string.no_internet_error)
            }
        }
    }

    private fun getPageData() {
        tvError.visibility = View.GONE
        pbWaiting.visibility = View.VISIBLE
        BaseService().getBaseApi().getData("story",tetPageNumber.text.toString()).enqueue(object : Callback<PageList?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<PageList?>, response: Response<PageList?>) {
                val pageBody = response.body()
                if (pageBody?.pageList?.isEmpty() == true) {
                    swipeRefreshPageData.visibility = View.GONE
                    pbWaiting.visibility = View.GONE
                    tvError.visibility = View.VISIBLE
                    selectedCount = 0
                    tvError.text = getString(R.string.page_empty)
                } else {
                    pbWaiting.visibility = View.GONE
                    tvError.visibility = View.GONE
                    swipeRefreshPageData.visibility = View.VISIBLE
                    val pageInfoAdapter = PageInfoAdapter(this@ApiCallSimpleActivity, pageBody!!.pageList)
                    pageInfoAdapter.setHasStableIds(true)
                    rvPageData.layoutManager = LinearLayoutManager(this@ApiCallSimpleActivity)
                    pageInfoAdapter.notifyDataSetChanged()
                    rvPageData.adapter = pageInfoAdapter
                    selectedCount = 0
                }
            }

            override fun onFailure(call: Call<PageList?>, t: Throwable) {
                pbWaiting.visibility = View.GONE
                swipeRefreshPageData.visibility = View.GONE
                tvError.visibility = View.VISIBLE
                tvError.text = t.message.toString()
            }
        })
    }


    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

