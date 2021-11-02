package com.sample.gual

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivityCalendarBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CalendarActivity : AppCompatActivity() {
    var myCalendarList = arrayListOf<MyCalendar>( )
     var uid : Long? = 0
    var cnt: Int = 0

    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //오늘 날짜 띄우기
        showToday()
        //mycalendar 리스트 값 넣기
        setMultiprofile()
        setMyPlatform()


        //하단 탭 버튼 조작
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
        val addsubscribebtn = findViewById<Button>(R.id.add_subscribe_btn)

        addsubscribebtn.setOnClickListener {
            val intent = Intent(this, AddSubscribeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

    }

    private fun setMyPlatform() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                uid = user.id
                val retrofitClient = RetrofitClient()

                // Enqueue로 비동기 통신 실행
                retrofitClient.service?.getcRequest(uid)?.enqueue(object : Callback<List<CalendarMyplatform>> {

                    // 통신 실패 시 콜백
                    override fun onFailure(call: Call<List<CalendarMyplatform>>, t: Throwable) {
                        Log.e("Retrofit_calrMyplatform", t.toString())
                        Log.d("Retrofit_calrMyplatform", "fail")
                    }

                    // 통신 성공 시 콜백
                    override fun onResponse(
                        call: Call<List<CalendarMyplatform>>,
                        response: Response<List<CalendarMyplatform>>
                    ) {
                        // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                        if (response.isSuccessful()) {
                            Log.d("Retrofit_calrMyplatform", response.body().toString())
                            Log.d("Retrofit_calrMyplatform", response.toString())
                            cnt = response.body()!!.size
                            for (i in 0..cnt -1) {
                                myCalendarList.add(
                                    MyCalendar(
                                        response.body()?.get(i)!!.mypf_id,
                                        response.body()?.get(i)!!.mypf_id,
                                        response.body()?.get(i)!!.mypf_name,
                                        response.body()?.get(i)!!.payment_day,
                                        response.body()?.get(i)!!.price,
                                        -1,
                                        response.body()?.get(i)!!.mypf_name,
                                        response.body()?.get(i)!!.mypf_name,
                                        response.body()?.get(i)!!.mypf_id,
                                        response.body()?.get(i)!!.mypf_name,
                                        response.body()?.get(i)!!.mypf_name
                                    ))
                                Log.d("Retrofit_calrMyplatform",myCalendarList[i].mtp_id.toString())
                            }
                            //리사이클러뷰 mycalendar리스트 띄우기
                            showMyCalendar()
                        }
                    }
                })
            }
        }
    }

    private fun showToday(){
        val instance = Calendar.getInstance()
        val month = (instance.get(Calendar.MONTH)+1).toString()
        val date = instance.get(Calendar.DATE).toString()

        Log.d("month",month)
        Log.d("date",date)

        val today_month=binding.month
        val today_date=binding.day

        today_month.setText(month)
        today_date.setText(date)

    }

    private fun setMultiprofile(){
        //uid얻기
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                uid = user.id
                val retrofitClient = RetrofitClient()
                Log.d("Retrofit_C_uid1",uid.toString())

                // Enqueue로 비동기 통신 실행
                retrofitClient.service?.getRequest(uid)?.enqueue(object : Callback<List<CalendarMultiprofile>> {

                    // 통신 실패 시 콜백
                    override fun onFailure(call: Call<List<CalendarMultiprofile>>, t: Throwable) {
                        Log.e("Retrofit_CMultiprofile", t.toString())
                        Log.d("Retrofit_CMultiprofile", "fail")
                    }

                    // 통신 성공 시 콜백
                    override fun onResponse(
                        call: Call<List<CalendarMultiprofile>>,
                        response: Response<List<CalendarMultiprofile>>
                    ) {
                        // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                        if (response.isSuccessful()) {
                            Log.d("Retrofit_CMultiprofile", response.body().toString())
                            Log.d("Retrofit_CMultiprofile", response.toString())
                            cnt = response.body()!!.size
                            Log.d("Retrofit_CMulticnt",cnt.toString())

                            for (i in 0..cnt -1) {
                                myCalendarList.add(
                                    MyCalendar(
                                    response.body()?.get(i)!!.mtp_id,
                                    response.body()?.get(i)!!.platform,
                                    response.body()?.get(i)!!.platform_name,
                                    response.body()?.get(i)!!.payment_day,
                                    response.body()?.get(i)!!.price,
                                    response.body()?.get(i)!!.master_uid,
                                    response.body()?.get(i)!!.account_id,
                                    response.body()?.get(i)!!.account_pwd,
                                    response.body()?.get(i)!!.automatch_access,
                                    response.body()?.get(i)!!.bank,
                                    response.body()?.get(i)!!.account_num
                                ))
                                Log.d("Retrofit_CmyCalendar",myCalendarList[i].mtp_id.toString())
                            }
                            //리사이클러뷰 mycalendar리스트 띄우기
                            showMyCalendar()

                        }
                    }
                })
            }
        }
    }
    private fun showMyCalendar() {
        val mAdapter = MyCalendarAdapter(this, myCalendarList)
        binding.listcalendar.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        binding.listcalendar.layoutManager = lm
        binding.listcalendar.setHasFixedSize((true))
    }
}