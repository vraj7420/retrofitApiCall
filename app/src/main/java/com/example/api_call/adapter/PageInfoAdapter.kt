package com.example.api_call.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.api_call.R
import com.example.api_call.model.PageModel
import com.example.api_call.view.activity.ApiCallSimpleActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.item_page.view.*
import java.text.ParseException
import java.text.SimpleDateFormat


class PageInfoAdapter(private var ctx: Context, private var pageDataList: ArrayList<PageModel>?) :
    RecyclerView.Adapter<PageInfoAdapter.PageInfoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageInfoHolder{
        val layoutInflater = LayoutInflater.from(ctx)
        return PageInfoHolder(
            layoutInflater.inflate(
                R.layout.item_page,
                parent,
                false
            )
        )
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: PageInfoHolder, position: Int) {
        val pageData = pageDataList?.get(position)
        if (pageData != null) {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
            try {
                val date = dateFormat.parse(pageData.created_at)
                holder.tvCratedDate.text = "Created At: $date"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            holder.tvTitle.text = "Title:" + " " + pageData.title
            holder.tvUrl.text = pageData.url
            val mActionBar = (ctx as AppCompatActivity).supportActionBar
            mActionBar?.title = ApiCallSimpleActivity.selectedCount.toString()
            holder.switchSelect.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    pageData.isSelected=true
                    ApiCallSimpleActivity.selectedCount += 1
                    holder.cvBackgroundPageData.setBackgroundResource(R.drawable.selected_item_card_view_background)
                    mActionBar?.title = ApiCallSimpleActivity.selectedCount.toString()
                    notifyDataSetChanged()
                } else {
                    pageData.isSelected=false
                    ApiCallSimpleActivity.selectedCount -= 1
                    holder.cvBackgroundPageData.setBackgroundResource(R.drawable.unselected_card_view_background)
                    mActionBar?.title = ApiCallSimpleActivity.selectedCount.toString()
                    notifyDataSetChanged()
                }
            }
            holder.itemView.setOnClickListener {
                pageData.setSelectItem(!pageData.isSelected)
                if(pageData.isSelected){
                    holder.switchSelect.isChecked=true
                    mActionBar?.title = ApiCallSimpleActivity.selectedCount.toString()
                } else{
                    holder.switchSelect.isChecked=false
                    mActionBar?.title = ApiCallSimpleActivity.selectedCount.toString()
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return pageDataList?.size!!
    }

    class PageInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCratedDate: MaterialTextView = itemView.tvCreatedDate
        val tvTitle: MaterialTextView = itemView.tvTitle
        val tvUrl: MaterialTextView = itemView.tvUrl
        val switchSelect: SwitchMaterial = itemView.switchSelect
        val cvBackgroundPageData: CardView = itemView.cvBackgroundPageData

    }
}