package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivityAddSubscribeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddSubscribeActivity : AppCompatActivity() {

    private var mypf_name: String = ""
    private var price: Int = 0
    private var payment_day: Int = 0
    private var uid: Long= 0


    private lateinit var binding: ActivityAddSubscribeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubscribeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        save_btn()
        delete_btn()

        //하단 버튼 조작
        val addressbtn = findViewById<ImageButton>(R.id.address_button)

        addressbtn.setOnClickListener {
            val intent = Intent(this, AddressmainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        val matchingbtn = findViewById<ImageButton>(R.id.matching_button)

        matchingbtn.setOnClickListener {
            val intent = Intent(this, MatchingActivity::class.java)
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

    private fun save_btn() {
        binding.saveBtn.setOnClickListener{

            if(binding.mypfName.length() ==0 || binding.price.length() ==0 || binding.paymentDay.length() == 0){
                Toast.makeText(this@AddSubscribeActivity, "모든 항목을 채워주세요", Toast.LENGTH_SHORT).show()
            }else{
                mypf_name = binding.mypfName.text.toString()
                price = Integer.parseInt(binding.price.text.toString())
                payment_day = Integer.parseInt(binding.paymentDay.text.toString())

                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        uid = user.id

                        val data = AddSubscribe(
                            mypf_name, price, payment_day, uid
                        )

                        val retrofitClient = RetrofitClient()
                        retrofitClient.service?.postRequest(data)?.enqueue(object :
                            Callback<PostResult> {

                            // 통신 실패 시 콜백
                            override fun onFailure(call: Call<PostResult>, t: Throwable) {
                                Log.e("Retrofit_AddSubscribe", t.toString())
                                Log.d("Retrofit_AddSubscribe", "fail")
                            }

                            // 통신 성공 시 콜백
                            override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                                if (response.isSuccessful()) {
                                    Log.d("Retrofit_AddSubscribe", response.body().toString())
                                    Log.d("Retrofit_AddSubscribe", response.toString())
                                    Toast.makeText(this@AddSubscribeActivity, "저장되었습니다", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                        val intent = Intent(this, CalendarActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    }
                }
            }

        }
    }
    private fun delete_btn(){
        binding.deleteBtn.setOnClickListener{
            finish()
        }
    }

}