package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPartyDeleteProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var receive_intent = intent
        var mtp_id = receive_intent.getIntExtra("mtp_id", 0)
        Log.d("myPartyDelete",mtp_id.toString())

        val data = MyPartyDeleteProgress(
            mtp_id
        )

        val retrofitClient = RetrofitClient()
        retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

            // 통신 실패 시 콜백
            override fun onFailure(call: Call<PostResult>, t: Throwable) {
                Log.e("Retrofit_partyDelete", t.toString())
                Log.d("Retrofit_partyDelete", "fail")
            }

            // 통신 성공 시 콜백
            override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                if (response.isSuccessful()) {
                    Log.d("Retrofit_partyDelete", response.body().toString())
                    Log.d("Retrofit_partyDelete", response.toString())

                    Toast.makeText(this@MyPartyDeleteProgressActivity, "삭제되었습니다", Toast.LENGTH_SHORT).show()

                }
            }
        })
        val intent = Intent(this, SettingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
        startActivity(intent)
        finish()

    }
}
