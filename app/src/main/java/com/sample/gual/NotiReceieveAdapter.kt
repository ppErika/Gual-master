package com.sample.gual

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotiReceieveAdapter(val context: Context, val notidataList: ArrayList<NotiData>):
    RecyclerView.Adapter<NotiReceieveAdapter.ItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.list_noti, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder?.bind(notidataList[position], context)
    }

    override fun getItemCount(): Int {
        return notidataList.size
    }

    inner class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val noti_image = itemView?.findViewById<ImageView>(R.id.noti_image)
        private val noti_text = itemView?.findViewById<TextView>(R.id.noti_text)
        private val noti_date = itemView?.findViewById<TextView>(R.id.noti_date)



        fun bind (notidata: NotiData, context: Context) {
            /* platformImage에 로고 넣고, 이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/
            if (notidata.noti_img == 1) {
                val resourceId =
                    context.resources.getIdentifier("noti_notification", "drawable", context.packageName)
                noti_image?.setImageResource(resourceId)
            } else if (notidata.noti_img == 2) {
                val resourceId =
                    context.resources.getIdentifier("noti_party_management", "drawable", context.packageName)
                noti_image?.setImageResource(resourceId)
            } else if (notidata.noti_img == 3) {
                val resourceId =
                    context.resources.getIdentifier("noti_payment_completion", "drawable", context.packageName)
                noti_image?.setImageResource(resourceId)
            } else if (notidata.noti_img == 4) {
                val resourceId =
                    context.resources.getIdentifier("noti_money_flow", "drawable", context.packageName)
                noti_image?.setImageResource(resourceId)
            }

            noti_text?.text = notidata.text.toString()
            noti_date?.text= notidata.date.toString()
        }
    }
}