package com.sample.gual

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivityEditSubscribeBinding
import com.sample.gual.databinding.ActivityMakingMultiprofileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EditSubscribeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSubscribeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSubscribeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        bottom_button()
    }
    private fun bottom_button(){
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
        binding.settingButton.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

    }

}