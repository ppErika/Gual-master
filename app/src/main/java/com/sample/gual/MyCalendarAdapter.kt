package com.sample.gual

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kakao.sdk.user.UserApiClient

class  MyCalendarAdapter(val context: Context, val myCalendarList: ArrayList<MyCalendar>):
RecyclerView.Adapter<MyCalendarAdapter.ItemViewHolder>(){

    var mtp_id: Int = 0
    var uid: Long = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.list_calendar, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder?.bind(myCalendarList[position], context)
    }

    override fun getItemCount(): Int {
        return myCalendarList.size
    }
    inner class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val payment_day = itemView?.findViewById<TextView>(R.id.payment_day)
        private val platform_name = itemView?.findViewById<TextView>(R.id.platform_name)
        private val payment = itemView?.findViewById<TextView>(R.id.payment)
        private val total_payment = itemView?.findViewById<TextView>(R.id.total_payment)
        private val slash1 = itemView?.findViewById<TextView>(R.id.slash1)
        private val slash2 = itemView?.findViewById<TextView>(R.id.slash2)
        private val slash3 = itemView?.findViewById<TextView>(R.id.slash3)

        fun bind (myCalendar: MyCalendar, context: Context) {

            if(myCalendar.master_uid <0){
                slash1?.visibility = View.GONE
                slash2?.visibility = View.GONE
                slash3?.visibility = View.GONE
                payment?.visibility = View.GONE
            }
            /* 나머지 TextView와 String 데이터를 연결한다. */
            payment_day?.text = myCalendar.payment_day.toString()
            platform_name?.text = myCalendar.platform_name
            total_payment?.text=myCalendar.price.toString()
            payment?.text=(myCalendar.price/4).toString()
            Log.d("Retrofit_CMultiAdapter",myCalendar.account_id)
            Log.d("Retrofit_CMultiAdapter",myCalendar.account_pwd)

            getUid() //현재 로그인한 uid 받기


            /*리사이클 뷰 아이템 클릭 시 실행되는 이벤트*/
            itemView.setOnClickListener {

                var pos = adapterPosition //클릭한 item의 position
                if(pos!=RecyclerView.NO_POSITION){ // 계정주 아이템 클릭시
                    if(myCalendarList[pos].master_uid == uid){
                        Intent(context, EditMultiprofileActivity::class.java).apply {
                            putExtra("mtp_id",myCalendarList[pos].mtp_id)
                            putExtra("platform_name",myCalendarList[pos].platform_name)
                            putExtra("platform",myCalendarList[pos].platform)
                            putExtra("account_id",myCalendarList[pos].account_id)
                            putExtra("account_pwd",myCalendarList[pos].account_pwd)
                            putExtra("automatch_access",myCalendarList[pos].automatch_access)
                            putExtra("bank",myCalendarList[pos].bank)
                            putExtra("account_num",myCalendarList[pos].account_num)
                            putExtra("uid",uid)

                        }.run { context.startActivity(this) }
                    }else if(myCalendarList[pos].master_uid < 0){
                        Intent(context, EditMyPlatformActivity::class.java).apply {
                            putExtra("mypf_name",myCalendarList[pos].platform_name)
                            putExtra("price",myCalendarList[pos].price)
                            putExtra("payment_day",myCalendarList[pos].payment_day)
                            putExtra("mypf_id",myCalendarList[pos].mtp_id)

                        }.run { context.startActivity(this) }
                    }else{ // 파티원 아이템 클릭시
                        Intent(context,  SubscribeInfoActivity::class.java).apply{
                            putExtra("uid",uid)
                            putExtra("mtp_id",myCalendarList[pos].mtp_id)
                            putExtra("platform_name",myCalendarList[pos].platform_name)
                            putExtra("platform",myCalendarList[pos].platform)
                            putExtra("account_id",myCalendarList[pos].account_id)
                            putExtra("account_pwd",myCalendarList[pos].account_pwd)
                            putExtra("price",myCalendarList[pos].price)
                            putExtra("payment_day",myCalendarList[pos].payment_day)
                        }.run{context.startActivity(this)}
                    }
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
}