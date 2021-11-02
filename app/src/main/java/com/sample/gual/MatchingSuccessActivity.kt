package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.sample.gual.databinding.ActivityMatchingSuccessBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MatchingSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchingSuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Activity에서 인텐트값 받기
        var receive_intent = intent
        var presentPlatformId = receive_intent.getIntExtra("present_platform_id", 0)
        var voucherName = receive_intent.getStringExtra("voucher_name")
        var finalPrice = receive_intent.getIntExtra("final_price", 0)

        show_view(presentPlatformId,voucherName,finalPrice) //값 매핑
        matching_info(presentPlatformId,voucherName,finalPrice)//retrofit통신으로 mtp_id, personnel 가져오기
        bottom_button() //하단 버튼 탭 구성
    }

    private fun show_view(presentPlatformId:Int,voucherName:String?,finalPrice:Int) {

        //현재시간+1h
        val time = System.currentTimeMillis()+3600000
        val dateFormat = SimpleDateFormat("hh:mm:ss")
        val curTime = dateFormat.format(Date(time))

        binding.name.text = voucherName
        binding.totalPrice.text = (finalPrice*4).toString()
        binding.finalPrice.text = finalPrice.toString()
        binding.timeOut.text = curTime+"까지 송금하지 않을 시 매칭이 자동 취소됩니다."

        when (presentPlatformId) {
            1 -> {
                val resourceId = this.resources.getIdentifier("netfilx_logo", "drawable", this.packageName)
                binding.platformImage.setImageResource(resourceId)
            }
            2 -> {
                val resourceId = this.resources.getIdentifier("watcha_logo", "drawable", this.packageName)
                binding.platformImage.setImageResource(resourceId)
            }
            3 -> {
                val resourceId = this.resources.getIdentifier("wave_logo", "drawable", this.packageName)
                binding.platformImage.setImageResource(resourceId)
            }
            4 -> {
                val resourceId = this.resources.getIdentifier("tving_logo", "drawable", this.packageName)
                binding.platformImage.setImageResource(resourceId)
            }
            else -> {
                binding.platformImage.setImageResource(R.mipmap.ic_launcher)
            }
        }
    }

    private fun matching_info(presentPlatformId:Int,voucherName:String?,finalPrice:Int) {

        val retrofitClient = RetrofitClient()

        retrofitClient.service?.getMatchingRequest(presentPlatformId)
            ?.enqueue(object : Callback<List<MatchingInfo>> {

                // 통신 실패 시 콜백
                override fun onFailure(call: Call<List<MatchingInfo>>, t: Throwable) {
                    Log.e("Retrofit_matchingInfo", t.toString())
                    Log.d("Retrofit_matchingInfo", "fail")
                }

                // 통신 성공 시 콜백
                override fun onResponse(
                    call: Call<List<MatchingInfo>>,
                    response: Response<List<MatchingInfo>>
                ) {
                    // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                    if (response.isSuccessful()) {
                        Log.d("Retrofit_matchingInfo", response.body().toString())
                        Log.d("Retrofit_matchingInfo", response.toString())

                        var mtp_id = response.body()!!.get(0).mtp_id
                        var personnel = response.body()!!.get(0).personnel
                        binding.cancelBtn.setOnClickListener{
                            val intent = Intent(applicationContext, MatchingCancelActivity::class.java)
                            intent.putExtra("mtp_id",mtp_id)
                            startActivity(intent)
                            overridePendingTransition(0, 0)
                        }
                        binding.paymentBtn.setOnClickListener{
                            val intent = Intent(applicationContext, PaymentActivity::class.java)
                            intent.putExtra("mtp_id",mtp_id)
                            intent.putExtra("personnel",personnel)
                            intent.putExtra("voucher_name",voucherName)
                            intent.putExtra("final_price",finalPrice)
                            startActivity(intent)
                            overridePendingTransition(0, 0)
                        }
                    }
                }
            })
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
}