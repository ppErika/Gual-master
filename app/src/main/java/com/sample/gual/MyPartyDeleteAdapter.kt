package com.sample.gual

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView

class MyPartyDeleteAdapter(val context: Context, val myPartyDeleteList: ArrayList<MyPartyDeleteClass>):
RecyclerView.Adapter<MyPartyDeleteAdapter.ItemViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.list_exit_party, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder?.bind(myPartyDeleteList[position], context)
    }

    override fun getItemCount(): Int {
        return myPartyDeleteList.size
    }
    inner class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val platformImage = itemView?.findViewById<ImageView>(R.id.platform_image)
        private val platformName = itemView?.findViewById<TextView>(R.id.platform_name)
        private val user_title = itemView?.findViewById<TextView>(R.id.user_title)
        private val user = itemView?.findViewById<TextView>(R.id.user)
        private val delete_btn=itemView?.findViewById<Button>(R.id.delete_btn)

        fun bind (myPartyDelete: MyPartyDeleteClass, context: Context) {
            /* 나머지 TextView와 String 데이터를 연결한다. */
            //플랫폼 번호 가져오기
            if(myPartyDelete.platformImage == 1){
                val resourceId = context.resources.getIdentifier("netfilx_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else if(myPartyDelete.platformImage == 2){
                val resourceId = context.resources.getIdentifier("watcha_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else if(myPartyDelete.platformImage == 3){
                val resourceId = context.resources.getIdentifier("wave_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else if(myPartyDelete.platformImage == 4){
                val resourceId = context.resources.getIdentifier("tving_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else{
                platformImage?.setImageResource(R.mipmap.ic_launcher)
            }
            platformName?.text = myPartyDelete.platformName

            //파티원 삭제 타이틀 부분 설정
            user_title?.visibility = View.GONE
            user?.visibility = View.GONE
            delete_btn?.text="삭제"
            delete_btn?.setOnClickListener{
                Intent(context,  MyPartyDeleteProgressActivity::class.java).apply{
                    putExtra("mtp_id",myPartyDeleteList[adapterPosition].mtp_id)
                }.run{context.startActivity(this)}

            }

        }
    }
}