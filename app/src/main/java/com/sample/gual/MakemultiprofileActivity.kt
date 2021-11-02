package com.sample.gual

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivityMakingMultiprofileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MakemultiprofileActivity : AppCompatActivity() {

    var master_uid: Long = 0
    var platform: Int = 0
    var bank: String = ""
    var automatch_access: Boolean = false
    var personnel: Int = 0
    var inputPaymentDay: Int = 0

    // 임시
    var payment_id1: Int? = null
    var payment_id2: Int? = null
    var payment_id3: Int? = null

    private lateinit var binding: ActivityMakingMultiprofileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakingMultiprofileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        createMultiProfile()

    }

    private fun createMultiProfile() {
        UserApiClient.instance.me { user, error ->
            user?.let {

                // master_uid 처리
                master_uid=user.id

                // 결제일 처리
                val paymentDayList = listOf("-",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31)
                val adapter4 = ArrayAdapter(
                    this,
                    R.layout.simple_list_item_1,
                    paymentDayList
                )
                binding.paymentDay.adapter = adapter4

                binding.paymentDay.onItemSelectedListener = object:
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        // An item was selected. You can retrieve the selected item using
                        // parent.getItemAtPosition(pos)
                        if(position != 0) {
                            inputPaymentDay = position
                            Log.d("Test paymentDay", inputPaymentDay.toString())
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Another interface callback
                    }
                }

                // 플랫폼 카테고리 Spinner
                val categoryList = listOf("플랫폼을 선택해주세요", "넷플릭스 4인 구독권", "왓챠 4인 구독권", "웨이브 4인 구독권", "티빙 4인 구독권")
                val adapter1 = ArrayAdapter(
                    this,
                    R.layout.simple_list_item_1,
                    categoryList
                )
                binding.category.adapter = adapter1

                binding.category.onItemSelectedListener = object:
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        // An item was selected. You can retrieve the selected item using
                        // parent.getItemAtPosition(pos)
                        if(position != 0) {
                            Toast.makeText(this@MakemultiprofileActivity, categoryList[position], Toast.LENGTH_SHORT).show()

                            platform = position
                            Log.d("Test platform", platform.toString())
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Another interface callback
                    }
                }

                // 은행 Spinner
                val bankList = listOf("은행", "카카오뱅크", "농협", "신한", "IBK기업", "하나", "우리", "국민", "SC제일",
                    "대구", "부산", "광주", "새마을", "경남", "전북", "제주", "산업", "우체국", "신협", "수협", "씨티",
                    "케이뱅크", "도이치", "BOA", "BNP", "중국공상", "HSBC", "JP모간", "산림조합", "저축은행")
                val adapter2 = ArrayAdapter(
                    this,
                    R.layout.simple_list_item_1,
                    bankList
                )
                binding.bank.adapter = adapter2

                binding.bank.onItemSelectedListener = object:
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        // An item was selected. You can retrieve the selected item using
                        // parent.getItemAtPosition(pos)
                        if(position != 0) {
                            Toast.makeText(this@MakemultiprofileActivity, bankList[position], Toast.LENGTH_SHORT).show()

                            bank=bankList[position]
                            Log.d("Test bank", bank.toString())
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Another interface callback
                    }
                }

                // 자동매칭 허용 Switch 처리
                binding.allowAutoMatching.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        automatch_access = true
                        Log.d("Test automatch true", automatch_access.toString())
                    } else {
                        automatch_access = false
                        Log.d("Test automatch false", automatch_access.toString())
                    }
                }

                // 모집인원 Spinner -> personnel
                val personnelList = listOf("이미 모집된 인원 수", "0", "1", "2", "3")
                val adapter3 = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    personnelList
                )
                binding.personnel.adapter = adapter3

                binding.personnel.onItemSelectedListener = object:
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                        when(position) {
                            4   ->  {
                                binding.friend1.setVisibility(View.VISIBLE)
                                binding.friend2.setVisibility(View.VISIBLE)
                                binding.friend3.setVisibility(View.VISIBLE)

                            }
                            3   ->  {
                                binding.friend1.setVisibility(View.VISIBLE)
                                binding.friend2.setVisibility(View.VISIBLE)
                                binding.friend3.setVisibility(View.GONE)
                            }
                            //...
                            2   -> {
                                binding.friend1.setVisibility(View.VISIBLE)
                                binding.friend2.setVisibility(View.GONE)
                                binding.friend3.setVisibility(View.GONE)

                            }
                            1   -> {
                                binding.friend1.setVisibility(View.GONE)
                                binding.friend2.setVisibility(View.GONE)
                                binding.friend3.setVisibility(View.GONE)

                            }
                        }

                        if(position != 0) {
                            Toast.makeText(this@MakemultiprofileActivity, personnelList[position], Toast.LENGTH_SHORT).show()

                            personnel = (personnelList[position]).toInt()
                            Log.d("Test personnel", personnel.toString())

                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Another interface callback
                    }
                }

                // 프로필 생성하기 버튼
                binding.createProfile.setOnClickListener {
                    if( inputPaymentDay == 0 || platform == 0 || bank == "") {
                        Toast.makeText(this@MakemultiprofileActivity,"선택하지 않은 항목이 있습니다.",Toast.LENGTH_SHORT).show()
                    }
                    else if(binding.accountId.text.toString().length==0||binding.accountPw.text.toString().length==0||binding.bankAccountNumber.text.toString().length==0) {
                        Toast.makeText(this@MakemultiprofileActivity,"입력하지 않은 항목이 있습니다.",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Log.d("popup", "popup")
                        val intent = Intent(this, PopupActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        createProfile()
                    }

                }
            }
            error?.let {

            }
        }
    }

    private fun createProfile() {

        if(binding.friendId1!=null) {
            // 매칭 테이블에 id 보내기
        }
        if(binding.friendId2!=null) {
            // 매칭 테이블에 id 보내기
        }
        if(binding.friendId3!=null) {
            // 매칭 테이블에 id 보내기
        }

        val data = MultiProfileModel(
            master_uid, platform, binding.accountId.text.toString(), binding.accountPw.text.toString(), inputPaymentDay,
            bank, binding.bankAccountNumber.text.toString(), payment_id1, payment_id2,
            payment_id3, automatch_access, personnel
        )

        val retrofitClient = RetrofitClient()
        // Enqueue로 비동기 통신 실행
        retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

            // 통신 실패 시 콜백
            override fun onFailure(call: Call<PostResult>, t: Throwable) {
                Log.e("Retrofit_mmp", t.toString())
                Log.d("Retrofit_mmp", "fail")
            }

            // 통신 성공 시 콜백
            override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                if (response.isSuccessful()) {
                    Log.d("Retrofit_mmp", response.body().toString())
                    Log.d("Retrofit_mmp", response.toString())
                    Log.d("Retrofit_mmp", master_uid.toString())
                    Log.d("Retrofit_mmp", platform.toString())
                    Log.d("Retrofit_mmp", binding.accountId.text.toString())
                    Log.d("Retrofit_mmp", binding.accountPw.text.toString())
                    Log.d("Retrofit_mmp", inputPaymentDay.toString())
                    Log.d("Retrofit_mmp", bank)
                    Log.d("Retrofit_mmp", binding.bankAccountNumber.text.toString())
                    Log.d("Retrofit_mmp", payment_id1.toString())
                    Log.d("Retrofit_mmp", payment_id2.toString())
                    Log.d("Retrofit_mmp", payment_id3.toString())
                    Log.d("Retrofit_mmp", automatch_access.toString())
                    Log.d("Retrofit_mmp", personnel.toString())
                }
            }
        })
    }
}