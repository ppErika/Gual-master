package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivitiyJoinBinding
import com.sample.gual.databinding.ActivityAddressMainBinding
import com.sample.gual.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    var address_con: Boolean = false
    var alert_before: Boolean = false
    var uid: Long = 0
    var nicknamecheck: Boolean = false

    private lateinit var binding: ActivityLoginBinding
    private lateinit var binding2: ActivityAddressMainBinding
    private lateinit var binding3: ActivitiyJoinBinding

    // 활동에서 뷰 결합 사용하기: onCreate() 메서드에서
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        카카오 키 해시 값 알아보는 코드
//        var keyHash = Utility.getKeyHash(this)
//        Log.i("키", keyHash)

        // inflate() 메서드를 호출 <- 활동에서 사용할 결합 클래스 인스턴스가 생성
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding2 = ActivityAddressMainBinding.inflate(layoutInflater)
        binding3 = ActivitiyJoinBinding.inflate(layoutInflater)

        // 루트 뷰를 전달하여 화면상의 활성 뷰로 만듦
        setContentView(binding.root)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) { // 토큰 무
                Log.e(TAG, "토큰 정보 보기 실패", error)
                binding.login.setOnClickListener {
                    //로그인
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) { //카카오톡으로 로그인
                        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                            Log.i(TAG, "loginWithKaKaoTalk $token $error")
                            UserApiClient.instance.me { user, error ->
                                if (error != null) { //유저정보 없으면 에러
                                    Log.e("토큰 에러",error.toString())
                                }
                                // 유저정보가 존재할 경우 -> user Table에서 uid 존재 여부 확인
                                else if (user != null) {
                                    val data = LoginModel(    user.id     )

                                    val retrofitClient = RetrofitClient()
                                    // Enqueue로 비동기 통신 실행
                                    retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

                                        // 통신 실패 시 콜백
                                        override fun onFailure(call: Call<PostResult>, t: Throwable) {
                                            Log.e("Retrofit", t.toString())
                                            Log.d("Retrofit", "fail")
                                        }

                                        // 통신 성공 시 콜백
                                        override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                                            // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                                            if (response.isSuccessful()) {
                                                Log.d("Retrofit", response.body().toString())
                                                Log.d("Retrofit", response.toString())

                                                if(response.body().toString() == "PostResult(result=0)"){
                                                    updateLoginInfo() // user.uid 없는 경우 (신규 사용자 -> Join 화면)
                                                }else{
                                                    val intent = Intent(this@LoginActivity, AddressmainActivity::class.java)
                                                    startActivity(intent) // user.uid 있는 경우 (기존 사용자 -> address 화면)

                                                }

                                            }
                                        }
                                    })

                                }

                            }
                        }

                    } else { // 카카오 계정으로 로그인
                        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                            Log.i(TAG, "loginWithKakaoAccount $token $error")
                            UserApiClient.instance.me { user, error ->
                                if (error != null) { //유저정보 없으면 에러
                                    Log.e("토큰 에러",error.toString())
                                }
                                // 유저정보가 존재할 경우 -> user Table에서 uid 존재 여부 확인
                                else if (user != null) {
                                    val data = LoginModel(    user.id     )

                                    val retrofitClient = RetrofitClient()
                                    // Enqueue로 비동기 통신 실행
                                    retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

                                        // 통신 실패 시 콜백
                                        override fun onFailure(call: Call<PostResult>, t: Throwable) {
                                            Log.e("Retrofit", t.toString())
                                            Log.d("Retrofit", "fail")
                                        }

                                        // 통신 성공 시 콜백
                                        override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                                            // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                                            if (response.isSuccessful()) {
                                                Log.d("Retrofit", response.body().toString())
                                                Log.d("Retrofit", response.toString())

                                                if(response.body().toString() == "PostResult(result=0)"){
                                                    updateLoginInfo()
                                                }else{
                                                    val intent = Intent(this@LoginActivity, AddressmainActivity::class.java)
                                                    startActivity(intent)

                                                }

                                            }
                                        }
                                    })

                                }

                            }
                        }
                    }


                }
            } else if (tokenInfo != null) { //토큰 유
                Log.i(
                    TAG, "토큰 정보 보기 성공" +
                            "\n회원번호: ${tokenInfo.id}" +
                            "\n만료시간: ${tokenInfo.expiresIn} 초"
                )
                val intent = Intent(this, AddressmainActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun updateLoginInfo() {
        UserApiClient.instance.me { user, error ->
            user?.let {
                Log.i(
                    TAG,
                    "updateLoginInfo: ${user.id} ${user.kakaoAccount?.email} ${user.kakaoAccount?.profile?.nickname} ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
                // 루트 뷰를 전달하여 화면상의 활성 뷰로 만듦
                setContentView(binding3.root)



                binding3.uname.text = user.kakaoAccount?.profile?.nickname
                binding3.email.text = user.kakaoAccount?.email
                uid = user.id

                // 친구목록 허용 Switch 처리
                binding3.addressCon.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        address_con = true
                        // 카카오톡 친구 목록 가져오기 (기본)
                        TalkApiClient.instance.friends { friends, error ->
                            if (error != null) {
                                Log.e(TAG, "카카오톡 친구 목록 가져오기 실패", error)
                            } else if (friends != null) {
                                Log.i(
                                    TAG,
                                    "카카오톡 친구 목록 가져오기 성공 \n${friends.elements?.joinToString("\n")}"
                                )
                                // 친구의 UUID 로 메시지 보내기 가능

                            }
                        }
                    } else {
                        // 동의 철회할 동의 항목 ID의 목록
                        address_con = false
                        val scopes = mutableListOf("friends")
                        UserApiClient.instance.revokeScopes(scopes) { scopeInfo, error ->
                            if (error != null) {
                                Log.e(TAG, "동의 철회 실패", error)
                            } else if (scopeInfo != null) {
                                Log.i(TAG, "동의 철회 성공\n 현재 가지고 있는 동의 항목 $scopeInfo")
                            }
                        }
                    }
                }

                // 결제일 전 알림 Switch 처리
                binding3.alertBefore.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        alert_before = true
                    } else {
                        alert_before = false
                    }
                }
                binding3.nicknameBtnCheck.setOnClickListener {

                    nicknameCheck()
                }

                binding3.gualBtnStart.setOnClickListener {
                    if (binding3.nickname.getText().toString().length == 0) {
                        Toast.makeText(this@LoginActivity, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        if (nicknamecheck == true) {
                            startGual()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "닉네임 중복확인을 해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                }
            }
            error?.let {
                binding3.uname.text = null
            }
        }
    }

    private fun nicknameCheck() {
        Log.d("nicknameCheck Click", binding3.nickname.text.toString())
        val data = NicknameCheckModel(
            binding3.nickname.text.toString()
        )
        val retrofitClient = RetrofitClient()
        retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

            // 통신 실패 시 콜백
            override fun onFailure(call: Call<PostResult>, t: Throwable) {
                Log.e("Retrofit", t.toString())
                Log.d("Retrofit", "fail")
            }

            // 통신 성공 시 콜백
            override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                if (response.isSuccessful()) {
                    Log.d("Retrofit", response.body().toString())
                    Log.d("Retrofit", response.toString())
                    Log.d("Retrofit", binding3.nickname.text.toString())
                    if (response.body().toString() == "PostResult(result=0)") {
                        Toast.makeText(this@LoginActivity, "사용할 수 있는 닉네임입니다.", Toast.LENGTH_SHORT)
                            .show()
                        nicknamecheck = true
                    } else {
                        if (binding3.nickname.getText().toString().length == 0) {
                            Toast.makeText(this@LoginActivity, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "이미 사용중인 닉네임입니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding3.nickname.getText().clear()
                        }
                        nicknamecheck = false

                    }


                }

            }

        })

    }

    private fun startGual() {
        val data = JoinModel(
            uid, binding3.uname.text.toString(), binding3.email.text.toString(),
            address_con, alert_before, binding3.nickname.text.toString()
        )

        val retrofitClient = RetrofitClient()
        // Enqueue로 비동기 통신 실행
        retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

            // 통신 실패 시 콜백
            override fun onFailure(call: Call<PostResult>, t: Throwable) {
                Log.e("Retrofit", t.toString())
                Log.d("Retrofit", "fail")
            }

            // 통신 성공 시 콜백
            override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                if (response.isSuccessful()) {
                    Log.d("Retrofit", response.body().toString())
                    Log.d("Retrofit", response.toString())
                    Log.d("Retrofit", binding3.uname.text.toString())
                    Log.d("Retrofit", binding3.email.text.toString())
                    Log.d("Retrofit", binding3.nickname.text.toString())
                    Log.d("Retrofit", address_con.toString())
                    Log.d("Retrofit", alert_before.toString())
                    Log.d("Retrofit", uid.toString())
                }
            }
        })

        val intent = Intent(this, AddressmainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}