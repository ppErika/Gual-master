package com.sample.gual

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendMultiprofileRequestActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)


        val mtp_id = intent.getIntExtra("mtp_id",0)
        val uid = intent.getLongExtra("uid",0)

        val data = MatchingA( mtp_id, uid)
        //retrofit실행 => 데이터 보내기
        val retrofitClient = RetrofitClient()
        // Enqueue로 비동기 통신 실행
        retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

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

                }
            }
        })
        finish()
    }


}