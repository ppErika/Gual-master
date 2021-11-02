package com.sample.gual

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sample.gual.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottom_button()
        event_button()

    }
    private fun bottom_button() {
        binding.addressButton.setOnClickListener {
            val intent = Intent(this, AddressmainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        binding.calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.matchingButton.setOnClickListener {
            val intent = Intent(this, MatchingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

    }
    private fun event_button() {
        binding.editPartyBtn.setOnClickListener {
            val intent = Intent(this, EditPartyActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.editUserBtn.setOnClickListener {
            val intent = Intent(this, EditUserActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.reportBtn.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://pf.kakao.com/_kAhxgK"))
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.notiReceieveBtn.setOnClickListener {
            var intent = Intent(this, NotiReceieveActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
}