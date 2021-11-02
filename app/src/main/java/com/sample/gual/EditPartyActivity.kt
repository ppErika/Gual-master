package com.sample.gual

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TabHost
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivityEditPartyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditPartyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPartyBinding

    var myPartyDeleteList = arrayListOf<MyPartyDeleteClass>( )
    var myPartyUserDeleteList= arrayListOf<MyPartyUserDeleteClass>()
    val unameList: ArrayList<String> = arrayListOf<String>()
    var uid : Long? = 0
    var cnt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPartyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //myparty 리스트 값 넣기
        setMyParty_tab1()

        //탭바 편집
        tabEdit()
    }
    private fun showMyParty() {
        val mAdapter = MyPartyDeleteAdapter(this, myPartyDeleteList)
        binding.listParty.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        binding.listParty.layoutManager = lm
        binding.listParty.setHasFixedSize((true))
    }

    private fun showMyParty2() {
        val mAdapter = MyPartyUserDeleteAdapter(this, myPartyUserDeleteList)
        binding.listPartyUser.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        binding.listPartyUser.layoutManager = lm
        binding.listPartyUser.setHasFixedSize((true))
    }

    //나중에 데이터값 db와 연결
    private fun setMyParty_tab1(){
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
                            retrofitClient.service?.getsRequest(uid)?.enqueue(object :
                                Callback<List<MyPartyDelete>> {

                                // 통신 실패 시 콜백
                                override fun onFailure(call: Call<List<MyPartyDelete>>, t: Throwable) {
                                    Log.e("Retrofit_mypartydelete", t.toString())
                                    Log.d("Retrofit_mypartydelete", "fail")
                                }

                                // 통신 성공 시 콜백
                                override fun onResponse(
                                    call: Call<List<MyPartyDelete>>,
                                    response: Response<List<MyPartyDelete>>
                                ) {
                                    // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                                    if (response.isSuccessful()) {
                                        Log.d("Retrofit_mypartydelete", response.body().toString())
                                        Log.d("Retrofit_mypartydelete", response.toString())

                                        cnt = response.body()!!.size
                                        for (i in 0..cnt -1) {
                                            myPartyDeleteList.add(
                                                MyPartyDeleteClass(
                                                    response.body()?.get(i)!!.mtp_id,
                                                    response.body()?.get(i)!!.platform,
                                                    response.body()?.get(i)!!.platform_name
                                                )
                                            )
                                            Log.d("Retrofit_calrMyplatform",myPartyDeleteList[i].toString())
                                        }
                                        //리사이클러뷰 myparty리스트 띄우기
                                        showMyParty()
                                        //setMyParty_tab2()
                                    }
                                }
                            })
                            Log.d("myparty_uid", uid.toString())
                            setMyParty_tab2(uid)
                        }
                    }

                }
            }
        }
    }

    private fun setMyParty_tab2(uid:Long?){
        val retrofitClient = RetrofitClient()
        // Enqueue로 비동기 통신 실행

        retrofitClient.service?.getoRequest(uid)?.enqueue(object :
            Callback<List<MyPartyUserDelete>> {

            // 통신 실패 시 콜백
            override fun onFailure(call: Call<List<MyPartyUserDelete>>, t: Throwable) {
                Log.e("Retrofit_mypuserdelete", t.toString())
                Log.d("Retrofit_mypuserdelete", "fail")
            }

            // 통신 성공 시 콜백
            override fun onResponse(
                call: Call<List<MyPartyUserDelete>>,
                response: Response<List<MyPartyUserDelete>>
            ) {
                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                if (response.isSuccessful()) {
                    Log.d("Retrofit_mypuserdelete", response.body().toString())
                    Log.d("Retrofit_mypuserdelete", response.toString())
                    Log.d("recycleview","recycleview")

                    cnt = response.body()!!.size
                    for (i in 0..cnt -1) {
                        myPartyUserDeleteList.add(
                            MyPartyUserDeleteClass(
                                response.body()?.get(i)!!.platform,
                                response.body()?.get(i)!!.platform_name,
                                response.body()?.get(i)!!.uname
                            )
                        )
                        Log.d("Retrofit_mypuserdelete",myPartyUserDeleteList[i].toString())
                    }
                    //리사이클러뷰 myparty리스트 띄우기
                    showMyParty2()
                }
            }
        })
    }

    private fun tabEdit(){
        val tabHost: TabHost = findViewById<TabHost>(R.id.tabHost1)
        tabHost.setup()

        val tab1 = tabHost.newTabSpec("Tab 1")  // 태그(Tag)는 탭 버튼을 식별할 때 사용되는 값
        tab1.setIndicator("파티삭제")    // 탭에 표시될 문자열 지정.
        tab1.setContent(R.id.tab1) //  매핑할 컨텐츠(Layout) 지정.
        tabHost.addTab(tab1)    // TabHost에 탭 추가

        val tab2 = tabHost.newTabSpec("Tab 2")
        tab2.setIndicator("파티원탈퇴")
        tab2.setContent(R.id.tab2)
        tabHost.addTab(tab2)

        //최초 선택 탭 지정
        tabHost.currentTab = 0
    }
}