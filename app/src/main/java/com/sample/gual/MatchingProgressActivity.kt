package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MatchingProgressActivity : AppCompatActivity() {

    private var success : Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //PlatformInfoActivity에서 인텐트값 받기
        var receive_intent = intent
        var presentPlatformId = receive_intent.getIntExtra("present_platform_id", 0)
        var voucherName = receive_intent.getStringExtra("voucher_name")
        var finalPrice = receive_intent.getIntExtra("final_price", 0)

        //현재시간
        val Time = System.currentTimeMillis()
        val date = Date(Time)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        val nowTime = dateFormat.format(date)

        Log.d("matchingProgress_plat",presentPlatformId.toString())
        Log.d("matchingProgress_name",voucherName.toString())
        Log.d("matchingProgress_price",finalPrice.toString())

        // 매칭
        MatchingProgressFun(presentPlatformId, nowTime, voucherName, finalPrice)

    }

    private fun MatchingProgressFun(presentPlatformId: Int, nowTime: String, voucherName: String?, finalPrice: Int) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                var uid = user.id
                val data = MatchingProgress(
                    presentPlatformId, uid, nowTime
                )

                var retrofitClient = RetrofitClient()
                retrofitClient.service?.postRequest(data)?.enqueue(object :
                    retrofit2.Callback<PostResult> {

                    // 통신 실패 시 콜백
                    override fun onFailure(call: Call<PostResult>, t: Throwable  ) {
                        Log.e("Retrofit_matching", t.toString())
                        Log.d("Retrofit_matching", "fail")
                    }

                    // 통신 성공 시 콜백
                    override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                        // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                        if (response.isSuccessful()) {
                            Log.d("Retrofit_matching", response.body().toString())
                            Log.d("Retrofit_matching", response.toString())
                            success = 1
                            val intent = Intent(applicationContext, MatchingSuccessActivity::class.java)
                            intent.putExtra("present_platform_id", presentPlatformId)
                            intent.putExtra("voucher_name", voucherName)
                            intent.putExtra("final_price", finalPrice)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
                            startActivity(intent)
                            finish()
                        }
                    }
                })
                if(success==0){
                    val intent = Intent(this, MatchingFailActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
                    startActivity(intent)
                    finish()
                }
            }
        }

    }
}

