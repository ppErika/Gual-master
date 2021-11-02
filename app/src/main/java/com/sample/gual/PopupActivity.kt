package com.sample.gual

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class PopupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_activity)

        val mainbtn = findViewById<Button>(R.id.close_button)
        val txtText=findViewById<TextView>(R.id.txtText)

        txtText.setText("프로필이 \n 생성 되었습니다")

        mainbtn.setOnClickListener {
            val intent = Intent(this, AddressmainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

    }


}