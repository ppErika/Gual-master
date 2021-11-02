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
import com.kakao.sdk.user.UserApiClient
import com.sample.gual.databinding.ActivityEditUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Savepoint
import java.util.*


class DeleteUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var uid=intent.getLongExtra("uid",0)
        val data = UserDelete(
            uid
        )

        Log.d("Retrofit_Delete", uid.toString())

        val retrofitClient = RetrofitClient()
        retrofitClient.service?.postRequest(data)?.enqueue(object : Callback<PostResult> {

            // 통신 실패 시 콜백
            override fun onFailure(call: Call<PostResult>, t: Throwable) {
                Log.e("Retrofit_Delete", t.toString())
                Log.d("Retrofit_Delete", "fail")
            }

            // 통신 성공 시 콜백
            override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                // onResponse가 무조건 성공 응답이 아니기 때문에 inSuccessful() 메소드를 통해 확인 필요
                if (response.isSuccessful()) {

                    Toast.makeText(this@DeleteUserActivity, "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(TAG, "연결 끊기 실패", error)
            }
            else {
                Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //해당 Activity를 최상위로, 그 위에 있던 Activity 삭제
        startActivity(intent)
        finish() //MainActivity 넘어가면서 창 닫기

    }
}

