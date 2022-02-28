package com.example.api_call.view.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.api_call.R
import com.example.api_call.model.PageModel
import com.example.api_call.network.BaseService
import kotlinx.android.synthetic.main.activity_api_call_with_json.*
import kotlinx.android.synthetic.main.item_page.*
import retrofit2.Call
import retrofit2.Response

class ApiCallWithJsonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_call_with_json)
        setListener()
    }

    private fun setListener() {
        btnPost.setOnClickListener {
            if(checkForInternet(this)){
            sendData()
            }
            else{
             Toast.makeText(this,"Please Turn On Internet",Toast.LENGTH_SHORT).show()
            }
        }

    }

     private fun sendData() {
          val pageModel=PageModel(tetCreatedAt.text.toString(),tetTitleOfBook.text.toString(),tetUrlOfBook.text.toString())
           BaseService().postBaseApi().postData(pageModel).enqueue(object : retrofit2.Callback<PageModel?> {
               override fun onResponse(call: Call<PageModel?>, response: Response<PageModel?>) {
                   Toast.makeText(this@ApiCallWithJsonActivity,"Successfully Post",Toast.LENGTH_SHORT).show()
               }

               override fun onFailure(call: Call<PageModel?>, t: Throwable) {
                   TODO("Not yet implemented")
               }
           })
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