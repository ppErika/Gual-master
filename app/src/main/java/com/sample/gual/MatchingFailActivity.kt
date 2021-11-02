package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.sample.gual.databinding.ActivityMatchingFailBinding

class MatchingFailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchingFailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingFailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottom_button()
        binding.returnMatchingMain.setOnClickListener{
            val intent = Intent(this, MatchingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            finish()
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
}