package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.sample.gual.databinding.ActivityPaymentBinding
import com.sample.gual.databinding.ActivitySubscribeInfoBinding

class SubscribeInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubscribeInfoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscribeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        show_view() // 값 매핑
        bottom_button() //하단 버튼 탭 구성
        event_button() //이벤트 처리 버튼
    }

    private fun show_view() {

        /*MyCalendarAdapter에서 Intet값 받기 */
        val platform_name = intent.getStringExtra("platform_name")
        val platform = intent.getIntExtra("platform",0)
        val account_id = intent.getStringExtra("account_id")
        val account_pwd = intent.getStringExtra("account_pwd")
        val price = intent.getIntExtra("price",0)
        val payment_day = intent.getIntExtra("payment_day",0)

        when (platform) {
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
        binding.platformName.text = "["+platform_name+" 4인 이용권]"
        binding.accountId.text = account_id.toString()
        binding.accountPwd.text = account_pwd.toString()
        binding.totalPrice.text = price.toString()
        binding.price.text = (price/4).toString()
        binding.paymentDay.text = payment_day.toString()
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
        binding.btnprev.setOnClickListener {
            onBackPressed()
        }
    }

}