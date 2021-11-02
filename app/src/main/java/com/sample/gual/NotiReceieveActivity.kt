package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivityEditUserBinding
import com.sample.gual.databinding.ActivityNotiReceieveBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Savepoint
import java.util.*

class NotiReceieveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotiReceieveBinding
    var notidataList = arrayListOf<NotiData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotiReceieveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기 버튼
        binding.btnprev.setOnClickListener {
            onBackPressed()
        }

        //내 알림들 불러오기
        getNotification()
    }

    private fun getNotification() {

        notidataList.add(NotiData(4,"Netfilx 가격이 인상되었습니다.","2021.10.22"))
        notidataList.add(NotiData(2,"한은경 님 김채원 님이 파티 요청을 보냈습니다. 확인해주세요.","2021.10.20"))
        notidataList.add(NotiData(3,"한은경 님 Netfilx 이은정 파티가 수락되었습니다.","2021.10.11"))
        notidataList.add(NotiData(1,"한은경 님 다음날 결제 알림 입니다! (Netfilx)","2021.10.6"))

        val mAdapter = NotiReceieveAdapter(this, notidataList)
        binding.listNoti.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        binding.listNoti.layoutManager = lm
        binding.listNoti.setHasFixedSize((true))

    }
}
