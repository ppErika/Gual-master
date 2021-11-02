package com.sample.gual

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.AccessController.getContext

class AgreeActivity : AppCompatActivity() {

    var AgList = arrayListOf<AgreeDisagree>()
    var cnt: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val mtp_id = intent.getIntExtra("mtp_id",0)
        val data = AgreeMtpId(mtp_id)

        //retrofit실행 => 데이터 보내기
        val retrofitClient = RetrofitClient()

        // Enqueue로 비동기 통신 실행
        retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

            // 통신 실패 시 콜백
            override fun onFailure(call: Call<PostResult>, t: Throwable) {
                Log.e("Retrofit_agree", t.toString())
                Log.d("Retrofit_agree", "fail")
            }

            // 통신 성공 시 콜백
            override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                if (response.isSuccessful()) {
                    Log.d("Retrofit_agree", response.body().toString())
                    Log.d("Retrofit_agree", response.toString())

                    // 해당 Activity가 없기 때문에 this로 context를 넘겨주지 않고 더 상위인 getApplicationContext()로 넘겨줌
                    // clean up 되지 않은 객체를 가지고 있다면 메모리 누수가 발생할 수 있으니 함부로 쓰지 말기 (웬만하면 this로!)
                    val intent = Intent( getApplicationContext(), AgreeDisagreeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
                    startActivity(intent)
                    overridePendingTransition(0, 0)

                }
            }
        })

        finish()
    }

}