package com.example.api_call.model

data class PageModel(var created_at:String,var title:String,var url:String){
    var isSelected = false
   fun setSelectItem(selection:Boolean){
       isSelected=selection
   }
}
