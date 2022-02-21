package com.example.api_call.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.api_call.R
import com.example.api_call.model.PageModel
import com.example.api_call.view.activity.ApiCallSimpleActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.item_page.view.*


class PageInfoAdapter(private var ctx: Context, private var pageDataList: ArrayList<PageModel>?) :
    RecyclerView.Adapter<PageInfoAdapter.VerifyInfoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerifyInfoHolder {
        val layoutInflater = LayoutInflater.from(ctx)
        return VerifyInfoHolder(
            layoutInflater.inflate(
                R.layout.item_page,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VerifyInfoHolder, position: Int) {
        val pageData = pageDataList?.get(position)
        if (pageData != null) {
            holder.tvCratedDate.text = "Created At:"+" "+pageData.created_at
            holder.tvTitle.text = "Title:"+" "+pageData.title
            holder.tvUrl.text = pageData.url
            val mActionBar = (ctx as AppCompatActivity).supportActionBar
            mActionBar?.title =ApiCallSimpleActivity.selectedCount.toString()
            holder.switchSelect.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    ApiCallSimpleActivity.selectedCount+=1
                    mActionBar?.title =ApiCallSimpleActivity.selectedCount.toString()

                } else {
                   ApiCallSimpleActivity.selectedCount-=1
                    mActionBar?.title =ApiCallSimpleActivity.selectedCount.toString()
                }
            }

        }

    }

    override fun getItemCount(): Int {
        return pageDataList?.size!!
    }

    class VerifyInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCratedDate: MaterialTextView = itemView.tvCreatedDate
        val tvTitle: MaterialTextView = itemView.tvTitle
        val tvUrl: MaterialTextView = itemView.tvUrl
        val switchSelect: SwitchMaterial = itemView.switchSelect
    }
}