package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivityPaymentBinding
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.Subject

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Activity에서 인텐트값 받기
        var receive_intent = intent
        var mtpId = receive_intent.getIntExtra("mtp_id", 0)
        var personnel = receive_intent.getIntExtra("personnel", 0)
        var voucherName = receive_intent.getStringExtra("voucher_name")
        var finalPrice = receive_intent.getIntExtra("final_price", 0)

        after_payment(mtpId,personnel,voucherName,finalPrice) //결제가 됐다고 가정

        bottom_button() //하단 버튼 탭 구성
        event_button() //이벤트 처리 버튼

        Handler().postDelayed({
            val intent = Intent(this@PaymentActivity, SubscribeInfoActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }, 3000)
    }

    private fun after_payment(mtpId: Int, personnel: Int, voucherName: String?, finalPrice: Int) {

        //현재시간
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val curTime = dateFormat.format(Date(time))
        Log.d("현재날짜",curTime)
        //결제테이블(insert payment) 매칭테이블(update status = 2)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                var uid = user.id
                var sid = "sid1234567"
                val data = MatchingPayment(
                    uid, sid, finalPrice, curTime, mtpId
                )
                Log.d("checkcheck",uid.toString())
                Log.d("checkcheck",finalPrice.toString())
                Log.d("checkcheck",curTime.toString())
                Log.d("checkcheck",mtpId.toString())

                var retrofitClient = RetrofitClient()
                retrofitClient.service?.postRequest(data)?.enqueue(object :
                    retrofit2.Callback<PostResult> {

                    // 통신 실패 시 콜백
                    override fun onFailure(call: Call<PostResult>, t: Throwable  ) {
                        Log.e("Retrofit_matchingPay", t.toString())
                        Log.d("Retrofit_matchingPay", "fail")
                    }

                    // 통신 성공 시 콜백
                    override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                        // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                        if (response.isSuccessful()) {
                            Log.d("Retrofit_matchingPay", response.body().toString())
                            Log.d("Retrofit_matchingPay", response.toString())

                        }
                    }
                })
            }
        }
    }


    private fun bottom_button(){
        val addressbtn = findViewById<ImageButton>(R.id.address_button)

        addressbtn.setOnClickListener {
            val intent = Intent(this, AddressmainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        val calenderbtn = findViewById<ImageButton>(R.id.calendar_button)

        calenderbtn.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        val settingbtn = findViewById<ImageButton>(R.id.setting_button)

        settingbtn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

    }
    private fun event_button(){

    }

}