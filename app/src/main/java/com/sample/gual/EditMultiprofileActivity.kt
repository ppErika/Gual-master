package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sample.gual.databinding.ActivityEditSubscribeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EditMultiprofileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSubscribeBinding
    private var selectedbank: String = ""
    private var bankList: List<String> = listOf("카카오뱅크", "농협", "신한", "IBK기업", "하나", "우리", "국민", "SC제일",
        "대구", "부산", "광주", "새마을", "경남", "전북", "제주", "산업", "우체국", "신협", "수협", "씨티",
        "케이뱅크", "도이치", "BOA", "BNP", "중국공상", "HSBC", "JP모간", "산림조합", "저축은행")
    private var cnt: Int= 0
    private var nameList = mutableListOf<String>()

    private var account_id: String = ""
    private var account_pwd: String = ""
    private var automatch_access: Int = 0
    private var bank: String = ""
    private var account_num: String = ""
    private var mtp_id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSubscribeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        show_membername() // 파티원 이름
        bank_spinner() //은행 스피너
        show_view() // 값 매핑
        save_button() // 저장하기 버튼
        bottom_button()

    }
    private fun show_membername() {

        /*파티원 이름 DB에서 가져오기*/
        mtp_id = intent.getIntExtra("mtp_id",0)

        val retrofitClient = RetrofitClient()
        // Enqueue로 비동기 통신 실행
        retrofitClient.service?.getmRequest(mtp_id)?.enqueue(object :
            Callback<List<MemberName>> {

            // 통신 실패 시 콜백
            override fun onFailure(call: Call<List<MemberName>>, t: Throwable) {
                Log.e("Retrofit_MemberName", t.toString())
                Log.d("Retrofit_MemberName", "fail")
            }

            // 통신 성공 시 콜백
            override fun onResponse(
                call: Call<List<MemberName>>,
                response: Response<List<MemberName>>
            ) {
                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                if (response.isSuccessful) {

                    Log.d("Retrofit_MemberName", response.body().toString())
                    Log.d("Retrofit_MemberName", response.toString())
                    cnt = response.body()!!.size

                    for(i in 0..cnt-1){
                        nameList.add(response.body()!![i].uname.substring(0,1)+"*"+response.body()!![i].uname.substring(2))
                    }

                    when (cnt) {
                        1 -> {
                            binding.member1.visibility = View.VISIBLE
                            binding.name1.text = nameList[0]
                        }
                        2 -> {
                            binding.member1.visibility = View.VISIBLE
                            binding.name1.text = nameList[0]
                            binding.member2.visibility = View.VISIBLE
                            binding.name2.text = nameList[1]
                        }
                        3 -> {
                            binding.member1.visibility = View.VISIBLE
                            binding.name1.text = nameList[0]
                            binding.member2.visibility = View.VISIBLE
                            binding.name2.text = nameList[1]
                            binding.member3.visibility = View.VISIBLE
                            binding.name3.text = nameList[2]
                        }
                    }

                }
            }
        })
    }

    private fun bank_spinner() {
        // 은행 Spinner

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            bankList
        )
        binding.bank.adapter = adapter

        binding.bank.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                if(position != 0) {
                    selectedbank=bankList[position]
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another interface callback
            }
        }
    }

    private fun show_view() {
        /*MyCalendarAdapter에서 Intet값 받기 */
        val platform_name = intent.getStringExtra("platform_name")
        val platform = intent.getIntExtra("platform",0)
        val account_id = intent.getStringExtra("account_id")
        val account_pwd = intent.getStringExtra("account_pwd")
        val automatch_access = intent.getIntExtra("automatch_access",0)
        val bank = intent.getStringExtra("bank")
        val account_num = intent.getStringExtra("account_num")


        if(platform == 1){
            val resourceId = this.resources.getIdentifier("netfilx_logo", "drawable", this.packageName)
            binding.platformImage.setImageResource(resourceId)
        }else if(platform == 2){
            val resourceId = this.resources.getIdentifier("watcha_logo", "drawable", this.packageName)
            binding.platformImage.setImageResource(resourceId)
        }else if(platform == 3){
            val resourceId = this.resources.getIdentifier("wave_logo", "drawable", this.packageName)
            binding.platformImage.setImageResource(resourceId)
        }else if(platform == 4){
            val resourceId = this.resources.getIdentifier("tving_logo", "drawable", this.packageName)
            binding.platformImage.setImageResource(resourceId)
        }else{
            binding.platformImage.setImageResource(R.mipmap.ic_launcher)
        }

        binding.platformName.text = "["+platform_name+" 4인 이용권]"
        binding.accountId.setText(account_id)
        binding.accountPwd.setText(account_pwd)
        binding.automatchAccess.isChecked = automatch_access != 0
        binding.accountNum.setText(account_num)

        for(i in 0..bankList.size-1){
            if(bank == bankList[i]){
                binding.bank.setSelection(i)
            }
        }
    }

    private fun save_button(){
        binding.saveBtn.setOnClickListener{

            account_id = binding.accountId.text.toString()
            account_pwd = binding.accountPwd.text.toString()
            automatch_access = if(binding.automatchAccess.isChecked){
                1
            }else{
                0
            }
            bank = selectedbank
            account_num = binding.accountNum.text.toString()

            val data = MultiprofileUpdate(
                account_id, account_pwd, automatch_access, bank, account_num, mtp_id
            )

            val retrofitClient = RetrofitClient()
            retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

                // 통신 실패 시 콜백
                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.e("Retrofit_MultiprofileU", t.toString())
                    Log.d("Retrofit_MultiprofileU", "fail")
                }

                // 통신 성공 시 콜백
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                    if (response.isSuccessful()) {
                        Log.d("Retrofit_MultiprofileU", response.body().toString())
                        Log.d("Retrofit_MultiprofileU", response.toString())
                        Toast.makeText(this@EditMultiprofileActivity, "저장되었습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            val intent = Intent(this, CalendarActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            finish() //CalendarActivity로 넘어가면서 창 닫기
        }
    }

    private fun bottom_button(){
        binding.addressButton.setOnClickListener {
            val intent = Intent(this, AddressmainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
        }

        binding.calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
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

}