package com.sample.gual

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyPartyAdapter(val context: Context, val myPartyList: ArrayList<MyParty>):
RecyclerView.Adapter<MyPartyAdapter.ItemViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.list_exit_party, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder?.bind(myPartyList[position], context)
    }

    override fun getItemCount(): Int {
        return myPartyList.size
    }
    inner class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val platformImage = itemView?.findViewById<ImageView>(R.id.platform_image)
        private val platformName = itemView?.findViewById<TextView>(R.id.platform_name)
        private val ownerName = itemView?.findViewById<TextView>(R.id.user)

        fun bind (myParty: MyParty, context: Context) {
            /* 나머지 TextView와 String 데이터를 연결한다. */
            if(myParty.platformImage == 1){
                val resourceId = context.resources.getIdentifier("netfilx_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else if(myParty.platformImage == 2){
                val resourceId = context.resources.getIdentifier("watcha_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else if(myParty.platformImage == 3){
                val resourceId = context.resources.getIdentifier("wave_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else if(myParty.platformImage == 4){
                val resourceId = context.resources.getIdentifier("tving_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else{
                platformImage?.setImageResource(R.mipmap.ic_launcher)
            }
            platformName?.text = myParty.platformName
            ownerName?.text=myParty.userName

            /*리사이클 뷰 아이템 클릭 시 실행되는 이벤트*/
            itemView.setOnClickListener {
                Intent(context, EditMultiprofileActivity::class.java).apply {
                    putExtra("data", myPartyList)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}