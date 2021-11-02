package com.sample.gual

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.RecyclerView
import com.kakao.sdk.user.UserApiClient


class FriendMultiprofileAdapter(val context: Context, val friendMultiprofileList: ArrayList<FriendMultiprofile>):
RecyclerView.Adapter<FriendMultiprofileAdapter.ItemViewHolder>() {

    var mtp_id: Int = 0
    var mSelectedItems = SparseBooleanArray()
    var uid: Long =0
    var status: Int ?= 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_friend_multiprofile, parent, false)
        return ItemViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder?.bind(friendMultiprofileList[position], context)
        getUid()
        val requestbtn =  holder.itemView.findViewById<Button>(R.id.request)
        val paymentbtn = holder.itemView.findViewById<Button>(R.id.payment)

        //요청 버튼 눌렀을때 요청중으로 바뀌기기
       if(mSelectedItems.get(position,false)){
            requestbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BEBEBE")))
            requestbtn.setText("요청중")
            requestbtn.setTextColor(context.getColor(R.color.black))
        }else{
            requestbtn.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.gual_pink)))
            requestbtn.setText("요청")
            requestbtn.setTextColor(context.getColor(R.color.white))
        }

        //matchig의 status값에 따른 요청버튼 처리
        if(status == 0) {
            requestbtn.setVisibility(View.VISIBLE)
            paymentbtn.setVisibility(View.GONE)
            requestbtn.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.gual_gray)))
            requestbtn.setText("요청중")
            requestbtn.setTextColor(context.getColor(R.color.black))
            requestbtn.setEnabled(false)
        }else if(status ==1) {
            requestbtn.setVisibility(View.GONE)
            paymentbtn.setVisibility(View.VISIBLE)
        }else{
            requestbtn.setVisibility(View.VISIBLE)
            paymentbtn.setVisibility(View.GONE)
        }
    }
    override fun getItemCount(): Int {
        return friendMultiprofileList.size
    }

    inner class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val platformImage = itemView?.findViewById<ImageView>(R.id.platform_image)
        private val uname = itemView?.findViewById<TextView>(R.id.party_uname)
        private val personnel = itemView?.findViewById<TextView>(R.id.user_count)
        private val requestbtn = itemView?.findViewById<Button>(R.id.request)
        private val paymentbtn = itemView?.findViewById<Button>(R.id.payment)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(friendMultiprofile: FriendMultiprofile, context: Context) {
            var total_amount: Int = 0
            var voucher_name: String = ""
            /* platformImage에 로고 넣고, 이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/
            /* total_amount, voucher_name에 가격과 플랫폼 이름 설정*/
            when (friendMultiprofile.platformImage) {
                1 -> {
                    val resourceId =
                        context.resources.getIdentifier(
                            "netfilx_logo",
                            "drawable",
                            context.packageName
                        )
                    platformImage?.setImageResource(resourceId)
                    total_amount = 3625
                    voucher_name = "넷플릭스"
                }
                2 -> {
                    val resourceId =
                        context.resources.getIdentifier(
                            "watcha_logo",
                            "drawable",
                            context.packageName
                        )
                    platformImage?.setImageResource(resourceId)
                    total_amount = 3225
                    voucher_name = "왓챠"
                }
                3 -> {
                    val resourceId =
                        context.resources.getIdentifier(
                            "wave_logo",
                            "drawable",
                            context.packageName
                        )
                    platformImage?.setImageResource(resourceId)
                    total_amount = 3475
                    voucher_name = "웨이브"
                }
                4 -> {
                    val resourceId =
                        context.resources.getIdentifier(
                            "tving_logo",
                            "drawable",
                            context.packageName
                        )
                    platformImage?.setImageResource(resourceId)
                    total_amount = 3475
                    voucher_name = "티빙"
                }
                else -> {
                    platformImage?.setImageResource(R.mipmap.ic_launcher)
                }
            }

            //status에 따른 button
            when (friendMultiprofile.status) {
                0 -> {
                    requestbtn?.visibility = View.VISIBLE
                    paymentbtn?.visibility = View.GONE
                    requestbtn?.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.gual_gray))
                    requestbtn?.text = "요청중"
                    requestbtn?.setTextColor(context.getColor(R.color.black))
                    requestbtn?.isEnabled = false
                }
                1 -> {
                    requestbtn?.visibility = View.GONE
                    paymentbtn?.visibility = View.VISIBLE
                }
                else -> {
                    requestbtn?.visibility = View.VISIBLE
                    paymentbtn?.visibility = View.GONE
                }
            }
            /* 나머지 TextView와 String 데이터를 연결한다. */
            uname?.text = friendMultiprofile.uname
            personnel?.text = friendMultiprofile.personnel.toString()
            status = friendMultiprofile.status
            Log.d("FriendMultip",friendMultiprofile.mtp_id.toString())


            requestbtn?.setOnClickListener{
                getUid()
                val position = adapterPosition
                Log.d("positioncheck",position.toString())

                if(mSelectedItems.get(position,false)){
                    mSelectedItems.put(position,false)
                }else{
                    mSelectedItems.put(position,true)
                    requestbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BEBEBE")))
                    requestbtn.setText("요청중")
                    requestbtn.setTextColor(context.getColor(R.color.black))

                    //받은 (uid 이동)알림함 내역 업데이트해주기
                    val intent = Intent(context, NotiReceieveActivity::class.java)
                    intent.putExtra("mtp_id",friendMultiprofile.mtp_id)
                    intent.putExtra("uid",uid)  //지금 내 uid

                }
                val intent = Intent(context, FriendMultiprofileRequestActivity::class.java)
                intent.putExtra("mtp_id",friendMultiprofile.mtp_id)
                intent.putExtra("uid",uid)
                Log.d("mtp_idcheck",friendMultiprofile.mtp_id.toString())

                itemView.context.startActivity(intent)

            }
            paymentbtn?.setOnClickListener {
                val intent = Intent(context, KakaoPayActivity::class.java)
                intent.putExtra("total_amount",total_amount)
                intent.putExtra("voucher_name",voucher_name)
                itemView.context.startActivity(intent)
            }
        }
    }

    private fun getUid() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                uid = user.id
            }
        }
    }


}