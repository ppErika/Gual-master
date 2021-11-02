package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.sample.gual.databinding.ActivityMatchingMainBinding

class MatchingActivity : AppCompatActivity() {

    var clicked_platform_id : Int = 0;
    private lateinit var binding: ActivityMatchingMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottom_button() //하단 탭 버튼 조작
        event_button()  //플랫폼별 버튼 처리

    }
    private fun bottom_button(){
        val calenderbtn = findViewById<ImageButton>(R.id.calendar_button)

        calenderbtn.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        val addressbtn = findViewById<ImageButton>(R.id.address_button)

        addressbtn.setOnClickListener {
            val intent = Intent(this, AddressmainActivity::class.java)
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
        binding.netflixBtn.setOnClickListener {
            clicked_platform_id=1
            val intent = Intent(this, PlatformInfoActivity::class.java)
            intent.putExtra("clicked_platform_id", clicked_platform_id)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.watchaBtn.setOnClickListener {
            clicked_platform_id=2
            val intent = Intent(this, PlatformInfoActivity::class.java)
            intent.putExtra("clicked_platform_id", clicked_platform_id)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.waveBtn.setOnClickListener {
            clicked_platform_id=3
            val intent = Intent(this, PlatformInfoActivity::class.java)
            intent.putExtra("clicked_platform_id", clicked_platform_id)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.tvingBtn.setOnClickListener {
            clicked_platform_id=4
            val intent = Intent(this, PlatformInfoActivity::class.java)
            intent.putExtra("clicked_platform_id", clicked_platform_id)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }


    }
}