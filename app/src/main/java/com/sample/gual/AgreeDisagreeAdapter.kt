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

class AgreeDisagreeAdapter(val context: Context, val agreeDisagreeList: ArrayList<AgreeDisagree>):
    RecyclerView.Adapter<AgreeDisagreeAdapter.ItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.list_agree_disagree, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder?.bind(agreeDisagreeList[position], context)
    }

    override fun getItemCount(): Int {
        return agreeDisagreeList.size
    }

    inner class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val party_uname = itemView?.findViewById<TextView>(R.id.party_uname)
        private val platform_image = itemView?.findViewById<ImageView>(R.id.platform_image)
        private val agreebtn = itemView?.findViewById<Button>(R.id.btn_agree)
        private val disagreebtn = itemView?.findViewById<Button>(R.id.btn_disagree)

        fun bind (agreeDisagree: AgreeDisagree, context: Context) {
            /* platformImage에 로고 넣고, 이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/
            if (agreeDisagree.platform == 1) {
                val resourceId =
                    context.resources.getIdentifier("netfilx_logo", "drawable", context.packageName)
                platform_image?.setImageResource(resourceId)
            } else if (agreeDisagree.platform == 2) {
                val resourceId =
                    context.resources.getIdentifier("watcha_logo", "drawable", context.packageName)
                platform_image?.setImageResource(resourceId)
            } else if (agreeDisagree.platform == 3) {
                val resourceId =
                    context.resources.getIdentifier("wave_logo", "drawable", context.packageName)
                platform_image?.setImageResource(resourceId)
            } else if (agreeDisagree.platform == 4) {
                val resourceId =
                    context.resources.getIdentifier("tving_logo", "drawable", context.packageName)
                platform_image?.setImageResource(resourceId)
            } else {
                platform_image?.setImageResource(R.mipmap.ic_launcher)
            }
            /* 나머지 TextView와 String 데이터를 연결한다. */
            party_uname?.text = agreeDisagree.uname
            Log.d("Recyclerview_ag",agreeDisagree.uname)

            // 수락 버튼 눌렀을 때
            agreebtn?.setOnClickListener{
                val intent = Intent(context, AgreeActivity::class.java)
                intent.putExtra("mtp_id", agreeDisagree.mtp_id)
                itemView.context.startActivity(intent)
            }

            // 거절 버튼 눌렀을 때
            disagreebtn?.setOnClickListener{
                val intent = Intent(context, DisagreeActivity::class.java)
                intent.putExtra("mtp_id", agreeDisagree.mtp_id)
                itemView.context.startActivity(intent)
            }
        }
    }
}