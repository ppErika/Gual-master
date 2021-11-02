package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sample.gual.databinding.ActivityAddSubscribeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditMyPlatformActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSubscribeBinding

    private var mypf_name: String? = ""
    private var price: Int = 0
    private var payment_day: Int = 0
    private var mypf_id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubscribeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        show_view() // 값 매핑
        save_btn() // 저장하기 버튼
        delete_btn() // 삭제하기 버튼

        //하단 버튼 조작
        binding.addressButton.setOnClickListener {
            val intent = Intent(this, AddressmainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
        }
       binding.matchingButton.setOnClickListener {
            val intent = Intent(this, MatchingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
        }
       binding.settingButton.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
           intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
        }
    }

    private fun delete_btn() {
        binding.deleteBtn.setOnClickListener{
            mypf_id = intent.getIntExtra("mypf_id",0)

            val data = MyPlatformDelete(
                mypf_id
            )

            val retrofitClient = RetrofitClient()
            retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

                // 통신 실패 시 콜백
                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.e("Retrofit_MyPlatformU", t.toString())
                    Log.d("Retrofit_MyPlatformU", "fail")
                }

                // 통신 성공 시 콜백
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                    if (response.isSuccessful()) {
                        Log.d("Retrofit_MyPlatformU", response.body().toString())
                        Log.d("Retrofit_MyPlatformU", response.toString())

                        Toast.makeText(this@EditMyPlatformActivity, "삭제되었습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            val intent = Intent(this, CalendarActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            finish() //CalendarActivity로 넘어가면서 창 닫기

        }
    }

    private fun save_btn() {
        binding.saveBtn.setOnClickListener{
            if(binding.mypfName.length() ==0 || binding.price.length() ==0 || binding.paymentDay.length() == 0){
                Toast.makeText(this@EditMyPlatformActivity, "모든 항목을 채워주세요", Toast.LENGTH_SHORT).show()
            }else{
                mypf_id = intent.getIntExtra("mypf_id",0)

                mypf_name= binding.mypfName.text.toString()
                price = Integer.parseInt(binding.price.text.toString())
                payment_day = Integer.parseInt(binding.paymentDay.text.toString())

                val data = MyPlatformUpdate(
                    mypf_name, price, payment_day, mypf_id
                )

                val retrofitClient = RetrofitClient()
                retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

                    // 통신 실패 시 콜백
                    override fun onFailure(call: Call<PostResult>, t: Throwable) {
                        Log.e("Retrofit_MyPlatformU", t.toString())
                        Log.d("Retrofit_MyPlatformU", "fail")
                    }

                    // 통신 성공 시 콜백
                    override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                        // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                        if (response.isSuccessful()) {
                            Log.d("Retrofit_MyPlatformU", response.body().toString())
                            Log.d("Retrofit_MyPlatformU", response.toString())
                            Log.d("Retrofit_MyPlatformU", mypf_id.toString())
                            Log.d("Retrofit_MyPlatformU", mypf_name.toString())

                            Toast.makeText(this@EditMyPlatformActivity, "저장되었습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                val intent = Intent(this, CalendarActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
                startActivity(intent)
                finish() //CalendarActivity로 넘어가면서 창 닫기
            }

        }
    }

    private fun show_view() {
        mypf_name = intent.getStringExtra("mypf_name")
        price = intent.getIntExtra("price",0)
        payment_day = intent.getIntExtra("payment_day",0)

        binding.addText.visibility = View.GONE //구독 정보를 추가합니다 글씨 안보이게
        binding.mypfName.setText(mypf_name)
        binding.price.setText(price.toString())
        binding.paymentDay.setText(payment_day.toString())

    }
}