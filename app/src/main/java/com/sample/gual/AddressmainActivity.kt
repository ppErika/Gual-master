package com.sample.gual

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivityAddressMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressmainActivity : AppCompatActivity() {

    var uid: Long = 0
    var address_con: Boolean? = null
    var total_count: Int = 0
    var myMultiprofileList = arrayListOf<MyMultiprofile>()
    var friendMultiprofileList = arrayListOf<FriendMultiprofile>()
    var friendlist = ArrayList<Long?>();

    var cnt: Int = 0

    private lateinit var binding: ActivityAddressMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserInfo() // 왼쪽 상단 프로필사진 띄우기
//        getFriend()
        showFriend() // Recyclerview 사용
        bottom_button() //하단 버튼 탭 구성
        event_button() //이벤트 처리 버튼

    }

    private fun UserInfo() {
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
                            Glide.with(this).load(user.kakaoAccount?.profile?.thumbnailImageUrl)
                                .circleCrop()
                                .into(findViewById<ImageView>(R.id.profile))
                            Log.i(
                                TAG, "사용자 정보 요청 성공" +
                                        "\n회원번호: ${user.id}" +
                                        "\n이메일: ${user.kakaoAccount?.email}" +
                                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                            )

                            makeImageButton() // 멀티프로필 버튼 만드는 부분
                            getFriend()
                        }
                    }

                }
            }
        } else {
            // 로그인 필요
        }
    }

    private fun getFriend() {

        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                binding.tung.setVisibility(View.VISIBLE)
                Log.e(TAG, "카카오톡 친구 목록 가져오기 실패", error)
            } else {
                Log.d(
                    TAG,
                    "카카오톡 친구 목록 가져오기 성공 \n${friends!!.elements?.joinToString("\n")}"
                )


                if (friends.elements!!.isEmpty()) {
                    binding.tung.setVisibility(View.VISIBLE)
                    Log.e(TAG, "구알에 가입중인 친구가 없습니다.")
                } else {

                    Log.d(TAG, "${friends!!.elements!![0].id}")

                    total_count = friends!!.totalCount

                    for (i in 0..total_count - 1) {
                        Log.d("friend", "${friends!!.elements!![i].id}")
                        Log.d("friend", "${friends!!.elements!![i].profileNickname}")
                    }

                    for (i in 0..total_count - 1) { // 친구 한명씩 받아서 서버에 보내기!
                        friendlist.add(friends!!.elements!![i].id)
                        Log.d("friendlist", friendlist[i].toString())
                    }
                    val retrofitClient = RetrofitClient()

                    Log.d("uid_check",uid.toString())
                    // Enqueue로 비동기 통신 실행

                    retrofitClient.service?.getRequest(friendlist, uid)?.enqueue(object : Callback<List<Friend>> {

                            // 통신 실패 시 콜백
                            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                                Log.e("Retrofit_platform", t.toString())
                                Log.d("Retrofit_platform", "fail")
                            }

                            // 통신 성공 시 콜백
                            override fun onResponse(
                                call: Call<List<Friend>>,
                                response: Response<List<Friend>>
                            ) {
                                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                                if (response.isSuccessful()) {
                                    Log.d("Retrofit_platform", response.body().toString())
                                    Log.d("Retrofit_platform", response.toString())
                                    cnt = response.body()!!.size

                                    Log.i("test", cnt.toString());
                                    Log.d("Retrofit_platform(cnt)", cnt.toString())
                                    for (i in 0..cnt - 1) {
                                        if(response.body()?.get(i)!!.status !=2 ){
                                            friendMultiprofileList.add(FriendMultiprofile(response.body()?.get(i)!!.mtp_id, response.body()?.get(i)!!.uname, response.body()?.get(i)!!.platform, response.body()?.get(i)!!.personnel, response.body()?.get(i)!!.status))
                                        }else{
                                            continue
                                        }
                                    }
                                    binding.listmultiprofile.setVisibility(View.VISIBLE)
                                }
                            }
                        })
                }
            }
        }


    }

    // 동적으로 멀티프로필 버튼 만들기
    private fun makeImageButton() {
        Log.i("Retrofit_MyMultipro", uid.toString())

        val retrofitClient = RetrofitClient()

        // Enqueue로 비동기 통신 실행
        retrofitClient.service?.getRequest(uid)?.enqueue(object : Callback<List<MyMultiprofile>> {

            // 통신 실패 시 콜백
            override fun onFailure(call: Call<List<MyMultiprofile>>, t: Throwable) {
                Log.e("Retrofit_MyMultiprofile", t.toString())
                Log.d("Retrofit_MyMultiprofile", "fail")
            }

            // 통신 성공 시 콜백
            override fun onResponse(
                call: Call<List<MyMultiprofile>>,
                response: Response<List<MyMultiprofile>>
            ) {
                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                if (response.isSuccessful()) {
                    Log.d("Retrofit_MyMultiprofile", response.body().toString())
                    Log.d("Retrofit_MyMultiprofile", response.toString())
                    cnt = response.body()!!.size

                    Log.i("Retrofit_MyMulti(test2)", cnt.toString());
                    Log.d("Retrofit_MyMulti(cnt2)", cnt.toString())
                    for (i in 0..cnt - 1) {
                        myMultiprofileList.add(
                            MyMultiprofile(
                                response.body()?.get(i)!!.mtp_id,
                                response.body()?.get(i)!!.platform,
                                response.body()?.get(i)!!.personnel
                            )
                        )
                    }

                    // 이미지 버튼을 만드는 부분
                    for (i in 0..cnt-1){
                        val buttonview = findViewById<LinearLayout>(R.id.buttonview)

                        val dynamicButton = ImageButton(this@AddressmainActivity)

                        dynamicButton.layoutParams = LinearLayout.LayoutParams(
                            changeDP(100),
                            changeDP(100),
                        )
                        dynamicButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE)
                        dynamicButton.setBackgroundColor(Color.parseColor("#071121"))

                        // 플랫폼 이미지를 붙이는 곳 (이미지가 없는 경우 안드로이드 기본 아이콘을 표시)
                        if (myMultiprofileList[i].platform == 1) {
                            dynamicButton.setImageResource(R.drawable.btn_netflix_active)
                        } else if (myMultiprofileList[i].platform == 2) {
                            dynamicButton.setImageResource(R.drawable.btn_watcha_active)
                        } else if (myMultiprofileList[i].platform == 3) {
                            dynamicButton.setImageResource(R.drawable.btn_wavve_active)
                        } else if (myMultiprofileList[i].platform == 4) {
                            dynamicButton.setImageResource(R.drawable.btn_tving_active)
                        } else {
                            dynamicButton.setImageResource(R.mipmap.ic_launcher)
                        }

                        buttonview.addView(dynamicButton)


                        dynamicButton.setOnClickListener {
                            val intent =
                                Intent(this@AddressmainActivity, AgreeDisagreeActivity::class.java)
                            intent.putExtra("clicked_personnel", myMultiprofileList[i].personnel)
                            intent.putExtra("clicked_mtp_id", myMultiprofileList[i].mtp_id)
                            intent.putExtra("clicked_platform", myMultiprofileList[i].platform)
                            startActivity(intent)
                            overridePendingTransition(0, 0)
                        }
                    }

                }
            }
        })
    }

    private fun changeDP(value: Int): Int {
        var displayMetrics = resources.displayMetrics
        var dp = Math.round(value * displayMetrics.density)
        return dp

    }

    private fun showFriend() {
        val mAdapter = FriendMultiprofileAdapter(this, friendMultiprofileList)
        binding.listmultiprofile.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        binding.listmultiprofile.layoutManager = lm
        binding.listmultiprofile.setHasFixedSize((true))
    }
    private fun bottom_button(){
        val calenderbtn = findViewById<ImageButton>(R.id.calendar_button)

        calenderbtn.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
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
    private fun event_button(){
        binding.makingprofilebtn.setOnClickListener {
            val intent = Intent(this, MakemultiprofileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

}