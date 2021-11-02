package com.sample.gual

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPartyUserDeleteAdapter(val context: Context, val myPartyUserDeleteList: ArrayList<MyPartyUserDeleteClass>):
RecyclerView.Adapter<MyPartyUserDeleteAdapter.ItemViewHolder>(){

    var uid : Long? = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.list_exit_party, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder?.bind(myPartyUserDeleteList[position], context)
    }

    override fun getItemCount(): Int {
        return myPartyUserDeleteList.size
    }
    inner class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val platformImage = itemView?.findViewById<ImageView>(R.id.platform_image)
        private val platformName = itemView?.findViewById<TextView>(R.id.platform_name)
        private val user_title = itemView?.findViewById<TextView>(R.id.user_title)
        private val user = itemView?.findViewById<TextView>(R.id.user)
        private val delete_btn=itemView?.findViewById<Button>(R.id.delete_btn)

        fun bind (myPartyUserDelete: MyPartyUserDeleteClass, context: Context) {
            /* 나머지 TextView와 String 데이터를 연결한다. */
            //플랫폼 번호 가져오기
            if(myPartyUserDelete.platformImage == 1){
                val resourceId = context.resources.getIdentifier("netfilx_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else if(myPartyUserDelete.platformImage == 2){
                val resourceId = context.resources.getIdentifier("watcha_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else if(myPartyUserDelete.platformImage == 3){
                val resourceId = context.resources.getIdentifier("wave_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else if(myPartyUserDelete.platformImage == 4){
                val resourceId = context.resources.getIdentifier("tving_logo", "drawable", context.packageName)
                platformImage?.setImageResource(resourceId)
            }else{
                platformImage?.setImageResource(R.mipmap.ic_launcher)
            }
            platformName?.text = myPartyUserDelete.platformName
            user?.text=myPartyUserDelete.ownerName

            //파티원 삭제 타이틀 부분 설정
            user_title?.text="계정주:"
            delete_btn?.text="탈퇴"

            //계정 id값 가져오기
            if (AuthApiClient.instance.hasToken()) {
                UserApiClient.instance.accessTokenInfo { _, error ->
                    if (error != null) {
                        if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                            // 로그인 필요
                            Log.e(TAG, "로그인 필요", error)
                        } else {
                            // 기타 에러
                            Log.e(TAG, "기타 에러", error)
                        }
                    } else {
                        // 토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
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

            //탈퇴버튼 이벤트 처리
            delete_btn?.setOnClickListener{
                val intent = Intent(context, PartyUserDeleteActivity::class.java)
                intent.putExtra("uid", uid)
                itemView.context.startActivity(intent)
            }

        }
    }
}