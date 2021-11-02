package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivityEditUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Savepoint
import java.util.*


class EditUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserBinding
    var uid:Long=0
    private var address_con: Int = 0
    private var alert_before: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기 버튼
        binding.btnprev.setOnClickListener {
            onBackPressed()
        }
        EditUser()  //회원 정보 수정할 때 회원 고정 정보 가져오기
    }

    private fun EditUser() {
        // 토큰이 있는 지 검사 (로그인 상태인지)
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                        // 로그인 필요
                        Log.e(TAG, "로그인 필요", error)
                    } else {
                        // 기타 에러
                        Log.e(TAG, "기타 에러", error)
                    }
                } else {
                    // 토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        } else if (user != null) {
                            uid = user.id

                            val retrofitClient = RetrofitClient()
                            // Enqueue로 비동기 통신 실행
                            retrofitClient.service?.getuRequest(uid)?.enqueue(object :
                                Callback<List<EditUser>> {

                                // 통신 실패 시 콜백
                                override fun onFailure(call: Call<List<EditUser>>, t: Throwable) {
                                    Log.e("Retrofit_edituser", t.toString())
                                    Log.d("Retrofit_edituser", "fail")
                                }

                                // 통신 성공 시 콜백
                                override fun onResponse(
                                    call: Call<List<EditUser>>,
                                    response: Response<List<EditUser>>
                                ) {
                                    // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                                    if (response.isSuccessful()) {
                                        Log.d("Retrofit_edituser", response.body().toString())
                                        Log.d("Retrofit_edituser", response.toString())

                                        binding.nickname.text=response.body()!![0].nickname
                                        if (response.body()!![0].email.toString().length!=0) {
                                            binding.email.text = response.body()!![0].email.toString()
                                        } else {
                                            binding.email.text = "(연동에 동의하지 않았습니다)"
                                        }
                                        binding.uname.text=response.body()!![0].uname
                                        binding.addressCon.isChecked=response.body()!![0].address_con !=0
                                        binding.alertBefore.isChecked=response.body()!![0].alert_before !=0
                                        binding.saveBtn.setOnClickListener{
                                            SaveBtn()
                                        }
                                    }
                                }
                            })
                            binding.deleteUserBtn.setOnClickListener{
                                DeleteUserBtn(uid)
                            }
                        }
                    }

                }

            }
        } else {
            // 로그인 필요
        }
    }

    private fun SaveBtn() {

        address_con = if(binding.addressCon.isChecked){
            1
        }else{
            0
        }
        alert_before = if(binding.alertBefore.isChecked){
            1
        }else{
            0
        }
        val data = EditUserSwitch(
            address_con,alert_before,uid
        )

        val retrofitClient = RetrofitClient()
        retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

            // 통신 실패 시 콜백
            override fun onFailure(call: Call<PostResult>, t: Throwable) {
                Log.e("Retrofit_Switch", t.toString())
                Log.d("Retrofit_Switch", "fail")
            }

            // 통신 성공 시 콜백
            override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                if (response.isSuccessful()) {
                    Log.d("uid", uid.toString())
                    Log.d("retrofitcheck", address_con.toString())
                    Log.d("retrofitcheck", alert_before.toString())
                    Log.d("Retrofit_Switch", response.body().toString())
                    Log.d("Retrofit_Switch", response.toString())
                    Toast.makeText(this@EditUserActivity, "저장되었습니다", Toast.LENGTH_SHORT).show()
                }
            }
        })
        finish() //SettingActivity로 넘어가면서 창 닫기

    }

    private fun DeleteUserBtn(uid:Long?){

        //uid 가져와서 mutliprofile 테이블에 있는지 없는지 확인

        val retrofitClient1 = RetrofitClient()

        Log.d("d_uid",uid.toString())
        // Enqueue로 비동기 통신 실행
        retrofitClient1.service?.getiRequest(uid)?.enqueue(object :
            Callback<List<UserDeleteCheck>> {

            // 통신 실패 시 콜백
            override fun onFailure(call: Call<List<UserDeleteCheck>>, t: Throwable) {
                Log.e("Retrofit_UserDelete", t.toString())
                Log.d("Retrofit_UserDelete", "fail")
            }

            // 통신 성공 시 콜백
            override fun onResponse(
                call: Call<List<UserDeleteCheck>>,
                response: Response<List<UserDeleteCheck>>
            ) {
                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                if (response.isSuccessful()) {
                    Log.d("Retrofit_UserDelete", response.body().toString())
                    Log.d("Retrofit_UserDelete", response.toString())

                    // 나의 uid가 파티에 속해져 있으면 -> 탈퇴 불가능
                    if(response.body().toString()!="[]"){
                        Toast.makeText(this@EditUserActivity, "파티 삭제해주세요", Toast.LENGTH_SHORT).show()
                    }
                    // 나의 uid가 파티에 속해져 있지 않다면-> 탈퇴 가능
                    else{
                        //Delete Retrofit
                        Log.d("else", "success")
                        val intent = Intent(applicationContext, DeleteUserActivity::class.java)
                        intent.putExtra("uid", uid)
                        startActivity(intent)
                    }
                }
            }
        })



    }



    private fun isPasswordFormat(password: String): Boolean {
        return password.matches("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,16}\$".toRegex())
    }

    /*private fun isChangeTextView(){
        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if(isPasswordFormat(binding.password.text.toString())){
                    binding.checkPasswordTextview1.visibility = View.GONE
                }
            }
        })

        binding.checkPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {

                if(binding.password.text.toString()==binding.checkPassword.text.toString()){
                    Log.d("Password_check2",binding.password.text.toString())
                    binding.checkPasswordTextview2.setText("확인 되었습니다.")
                }
                else {
                    Log.d("Password_check3",binding.password.text.toString())
                    binding.checkPasswordTextview2.setText("입력하신 비밀번호와 틀립니다.")
                }
            }
        })*/
    }
