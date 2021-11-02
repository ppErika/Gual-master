package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.gual.databinding.ActivityAgreeDisagreeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgreeDisagreeActivity : AppCompatActivity() {

    var AgList = arrayListOf<AgreeDisagree>()
    var cnt: Int = 0

    private lateinit var binding: ActivityAgreeDisagreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgreeDisagreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이전 버튼
        val btnPrev = findViewById<ImageButton>(R.id.btn_prev)
        btnPrev.setOnClickListener {
            val intent = Intent(this, AddressmainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        // 인텐트 할 때 받아온 데이터
        val receive_intent = intent
        val presentNum = receive_intent.getIntExtra("clicked_personnel", 0)
        val presentMtpId = receive_intent.getIntExtra("clicked_mtp_id", 0)

        // 현재 모집된 파티원 텍스트 set
        binding.txtPresentNum.setText(presentNum.toString())

        // matching 가져오기 위해 통신 (Enqueue로 비동기 통신 실행)
        val retrofitClient = RetrofitClient()

        retrofitClient.service?.getRequest(presentMtpId)
            ?.enqueue(object : Callback<List<AgreeDisagreeList>> {

                // 통신 실패 시 콜백
                override fun onFailure(call: Call<List<AgreeDisagreeList>>, t: Throwable) {
                    Log.e("Retrofit_ag", t.toString())
                    Log.d("Retrofit_ag", "fail")
                }

                // 통신 성공 시 콜백
                override fun onResponse(
                    call: Call<List<AgreeDisagreeList>>,
                    response: Response<List<AgreeDisagreeList>>
                ) {
                    // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                    if (response.isSuccessful()) {
                        Log.d("Retrofit_ag", response.body().toString())
                        Log.d("Retrofit_ag", response.toString())
                        cnt = response.body()!!.size

                        Log.d("Retrofit_ag(cnt)", cnt.toString())
                       for (i in 0..cnt - 1) {
                            AgList.add(
                                AgreeDisagree(
                                    response.body()?.get(i)!!.mtp_id,
                                    response.body()?.get(i)!!.uname,
                                    response.body()?.get(i)!!.platform
                                )
                            )

                        }
                    }
                    show_view()

                }
            })

    }
    private fun show_view(){
        // Recyclerview 사용
        val mAdapter = AgreeDisagreeAdapter(this, AgList)
        binding.listAgreeDisagree.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        binding.listAgreeDisagree.layoutManager = lm
        binding.listAgreeDisagree.setHasFixedSize((true))
    }

}