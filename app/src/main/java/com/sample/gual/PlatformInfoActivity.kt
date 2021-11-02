package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.gual.databinding.ActivityCalendarBinding
import com.sample.gual.databinding.ActivityMatchingMainBinding
import com.sample.gual.databinding.ActivityPlatformInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PlatformInfoActivity : AppCompatActivity() {

    var platformList = arrayListOf<PlatformInfo>()
    var cnt: Int = 0
    var voucherName: String? = ""
    var finalPrice: Int? = 0

    private lateinit var binding: ActivityPlatformInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatformInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트 할 때 받아온 데이터
        val receive_intent = intent
        val presentPlatformId = receive_intent.getIntExtra("clicked_platform_id", 0)

        // matching 가져오기 위해 통신 (Enqueue로 비동기 통신 실행)
        val retrofitClient = RetrofitClient()

        retrofitClient.service?.getpRequest(presentPlatformId)
            ?.enqueue(object : Callback<List<PlatformInfo>> {

                // 통신 실패 시 콜백
                override fun onFailure(call: Call<List<PlatformInfo>>, t: Throwable) {
                    Log.e("Retrofit_pi", t.toString())
                    Log.d("Retrofit_pi", "fail")
                }

                // 통신 성공 시 콜백
                override fun onResponse(
                    call: Call<List<PlatformInfo>>,
                    response: Response<List<PlatformInfo>>
                ) {
                    // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                    if (response.isSuccessful()) {
                        Log.d("Retrofit_p", response.body().toString())
                        Log.d("Retrofit_p", response.toString())
                        cnt = response.body()!!.size

                        for (i in 0..cnt - 1) {
                            platformList.add(
                                PlatformInfo(
                                    response.body()?.get(i)!!.platform_id,
                                    response.body()?.get(i)!!.platform_name,
                                    response.body()?.get(i)!!.price
                                )
                            )
                        }
                    }
                    voucherName = platformList[0].platform_name+" 4인 이용권"
                    finalPrice = platformList[0].price / 4

                    // 플랫폼 name, price, priceResult 바인딩
                    binding.platformName.setText(voucherName)
                    binding.platformPrice.setText(platformList[0].price.toString())
                    binding.platformPriceResult.setText(finalPrice.toString())

                    event_button(presentPlatformId,voucherName,finalPrice) //이벤트 버튼 처리

                }
            })



    }

    private fun event_button(presentPlatformId:Int, voucherName: String?, finalPrice: Int?) {
        //뒤로가기 버튼
        binding.prevBtn.setOnClickListener {
            onBackPressed()
        }

        // 매칭 시작
        binding.matchingStartBtn.setOnClickListener {
            val intent = Intent(this, MatchingProgressActivity::class.java)
            intent.putExtra("present_platform_id", presentPlatformId)
            intent.putExtra("voucher_name", voucherName)
            intent.putExtra("final_price", finalPrice)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

}