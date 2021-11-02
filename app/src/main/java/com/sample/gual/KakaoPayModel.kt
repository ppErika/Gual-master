package com.sample.gual

/* 카카오페이 Request, Response 모델 */

data class KakaoPayRequest(
    var cid: String,
    var partner_order_id: String,
    var partner_user_id: String,
    var item_name: String,
    var quantity: Int,
    var total_amount: Int,
    var tax_free_amount: Int,
    var approval_url: String,
    var cancel_url: String,
    var fail_url: String
)

data class KakaoPayReadyResponse(
    var tid: String = "",
    var next_redirect_app_url: String = "",
    var next_redirect_mobile_url: String = "",
    var next_redirect_pc_url: String = "",
    var android_app_scheme: String = "",
    var ios_app_scheme: String = "",
    var created_at: String ="" // Datetime
)

data class KakaoPayApprovalRequest(
    var cid: String,
    var tid: String,
    var patner_order_id: String,
    var patner_user_id: String,
    var pg_token: String
)

data class PgTokenResponse(
    var pg_token: String
)

data class KakaoPayApprovalResponse(
    var aid: String = "",
    var tid: String = "",
    var cid: String = "",
    var sid: String = "",
    var patner_order_id: String = "",
    var patner_user_id: String = "",
    var payment_method_type: String = "",
    var amount: Amount,
    var card_info: CardInfo,
    var item_name: String = "",
    var item_code: String = "",
    var quantity: Int = 0,
    var created_at: String = "", // Datetime
    var approved_at: String = "", // Datetime
    var payload: String = ""
)

data class Amount(
    var total: Int = 0,
    var tax_free: Int = 0,
    var vat: Int = 0,
    var point: Int = 0,
    var discount: Int = 0
)
data class CardInfo(
    var purchase_corp: String = "",
    var purchase_corp_code: String = "",
    var issuer_corp: String = "",
    var issuer_corp_code: String = "",
    var kakaopay_purchase_corp: String = "",
    var kakaopay_purchase_corp_code: String = "",
    var kakaopay_issuer_corp: String = "",
    var kakaopay_issuer_corp_code: String = "",
    var bin: String = "",
    var card_type: String = "",
    var install_month: String = "",
    var approved_id: String = "",
    var card_mid: String = "",
    var interest_free_install: String = "",
    var card_item_code: String = ""
)


