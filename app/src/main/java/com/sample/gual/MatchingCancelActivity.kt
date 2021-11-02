package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Response

class MatchingCancelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                var uid = user.id

                //mtp_id 받기
                var receive_intent = intent
                var mtp_id = receive_intent.getIntExtra("mtp_id", 0)

                //매칭취소 -> 매칭테이블에서 삭제

                val data = MatchingCancel(mtp_id, uid)

                var retrofitClient = RetrofitClient()
                retrofitClient.service?.postRequest(data)?.enqueue(object :
                    retrofit2.Callback<PostResult> {

                    // 통신 실패 시 콜백
                    override fun onFailure(call: Call<PostResult>, t: Throwable  ) {
                        Log.e("Retrofit_matchingCancel", t.toString())
                        Log.d("Retrofit_matchingCancel", "fail")
                    }

                    // 통신 성공 시 콜백
                    override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                        // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                        if (response.isSuccessful()) {
                            Log.d("Retrofit_matchingCancel", response.body().toString())
                            Log.d("Retrofit_matchingCancel", response.toString())
                            Toast.makeText(this@MatchingCancelActivity, "취소되었습니다.", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(applicationContext, MatchingActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
                            startActivity(intent)
                            finish()
                        }
                    }
                })
            }
        }
    }
}