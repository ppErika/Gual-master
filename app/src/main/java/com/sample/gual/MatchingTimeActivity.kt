package com.sample.gual

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivityMatchingTimeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

class MatchingTimeActivity : AppCompatActivity() {

    var time = 0

    private lateinit var binding: ActivityMatchingTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트 할 때 받아온 데이터
        val receive_intent = intent
        val platformId = receive_intent.getIntExtra("present_platform_id", 0)
        val voucherName = receive_intent.getStringExtra("voucher_name")
        var price = receive_intent.getIntExtra("finalPrice", 0)


        Log.i("확인", platformId.toString())

        // platformImage에 로고 넣고, 이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.
        if (platformId == 1) {
            binding.platformImage.setImageResource(R.drawable.netfilx_logo)
        } else if (platformId == 2) {
            binding.platformImage.setImageResource(R.drawable.watcha_logo)
        } else if (platformId == 3) {
            binding.platformImage.setImageResource(R.drawable.wave_logo)
        } else if (platformId == 4) {
            binding.platformImage.setImageResource(R.drawable.tving_logo)
        } else {
            binding.platformImage.setImageResource(R.mipmap.ic_launcher)
        }

        // 인텐트 자료 바인딩
        binding.voucherTxt.setText("["+voucherName+"]")

        bottom_button() //하단 버튼 탭 구성
        event_button() //이벤트 처리 버튼

        starttime()
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
        binding.cancelMatchingBtn.setOnClickListener{
            val intent = Intent(this, MatchingSuccessActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

    }
    private fun starttime(){
        val timer = timer(period = 10){
            time++
            val min=(time/100)/60
            var sec=time/100

            if(sec==0){
                sec=time/100
            }else if(sec/60>=1){
                sec %=60
            }
            runOnUiThread{
                binding.minText.text="$min"
                binding.secText.text="$sec"

            }
       }
    }

}