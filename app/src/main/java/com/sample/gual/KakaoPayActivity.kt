package com.sample.gual

import android.content.Intent
import android.net.UrlQuerySanitizer
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.sample.gual.databinding.ActivityKakaoPayBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException


class KakaoPayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKakaoPayBinding
    private lateinit var webView: WebView
    private var tidPin :String? = ""
    private var pgToken :String? = ""

    val api = PayRetrofitService.create()

    // 결제 준비 파라미터(Field)에 담을 데이터
    var cid = "TC0ONETIME"
    var partnerOrderId = "1001"
    var partnerUserId = "erika"
    var itemName = "넷플릭스"
    var quantity = 1
    var totalAmount = 4000
    var taxFreeAmount = 1000
    var approvalUrl = "http://gual.cafe24app.com"
    var cancelUrl = "http://gual.cafe24app.com"
    var failUrl = "http://gual.cafe24app.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKakaoPayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webView = binding.webView
        webView.webViewClient = MyWebViewClient()

        // 인텐트 할 때 받아온 데이터
        val receive_intent = intent
        val voucherName = receive_intent.getStringExtra("voucher_name")
        val voucherPrice = receive_intent.getIntExtra("total_amount", 0)

        itemName = voucherName.toString()
        totalAmount = voucherPrice

        // 결제 준비
        api.postRequest(cid, partnerOrderId, partnerUserId, itemName, quantity, totalAmount, taxFreeAmount, approvalUrl, cancelUrl, failUrl)?.enqueue(object : Callback<KakaoPayReadyResponse> {
            override fun onFailure(call: Call<KakaoPayReadyResponse>, t: Throwable) {
                Log.d("CometChatAPI::", "Failed API call with call: " + call +
                        " + exception: " + t)
            }
            override fun onResponse(call: Call<KakaoPayReadyResponse>, response: Response<KakaoPayReadyResponse>) {
                Log.d("Response 코드:: ", response.code().toString())
                Log.d("Response 메시지:: ", response.message())
                var tid: String? = response.body()?.tid
                val url: String? = response.body()?.next_redirect_app_url

                if (url == null) {
                    Log.d("url==null, body값을 출력:", response.body().toString())
                } else {
                    webView.loadUrl(url)
                    tidPin = tid

                    val sanitizer = UrlQuerySanitizer("?pg_token=")
                    pgToken = sanitizer.getValue("pg_token")
                    Log.i("pgToken 값 확인하기", pgToken.toString())
                }
            }
        })
    }

    inner class MyWebViewClient : WebViewClient() {
        // URL 변경시 발생 이벤트
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.e("Debug ::", url);

            if (url != null && url.contains("pg_token=")) {
                pgToken = url.substring(url.indexOf("pg_token=") + 9);
                Log.i("이너 클래스에서 pgToken 값 확인하기", pgToken.toString())

                api.postRequest(cid, tidPin, partnerOrderId, partnerUserId, pgToken)?.enqueue(object : Callback<KakaoPayApprovalResponse> {
                    override fun onFailure(call: Call<KakaoPayApprovalResponse>, t: Throwable) {
                        Log.d("CometChatAPI2::", "Failed API call with call: " + call +
                                " + exception: " + t)
                    }
                    override fun onResponse(
                        call: Call<KakaoPayApprovalResponse>,
                        response: Response<KakaoPayApprovalResponse>
                    ) {
                        Log.i("결제 승인 통신 성공", response.body().toString())
                    }
                })

            } else if (url != null && url.startsWith("intent://")) {
                try {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    val existPackage =
                        packageManager.getLaunchIntentForPackage(intent.getPackage()!!)
                    if (existPackage != null) {
                        startActivity(intent);
                    }
                    return true
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }
            }
            view.loadUrl(url)
            return false
        }
    }
}