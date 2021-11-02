package com.sample.gual

import org.json.JSONObject
import java.time.format.DateTimeFormatter
import java.util.*

// DTO 클래스

data class JoinModel(
    var uid: Long = 0,
    var uname: String? = null,
    var email: Any? = null,
    var address_con: Boolean = false,
    var alert_before: Boolean = false,
    var nickname: String? = null
)
data class NicknameCheckModel(
    var nickname: String? = null
)

data class MultiProfileModel(
    var master_uid: Long = 0,
    var platform: Int = 0,
    var account_id: String = "",
    var account_pwd: String = "",
    var payment_day: Int = 0,
    var bank: String = "",
    var account_num: String = "",
    var payment_id1: Int? = null,
    var payment_id2: Int? = null,
    var payment_id3: Int? = null,
    var automatch_access: Boolean = false,
    var personnel: Int = 0
)

data class LoginModel(
    var uid: Long = 0
)

data class AddressConModel(
    var uid: Long = 0
)

data class FriendPlatformModel(
    var uid: Long? = 0
)

data class PostResult(
    var result: String? = null
)

data class Friend(
    val mtp_id: Int,
    val uname: String,
    val platform: Int,
    val personnel: Int,
    val status: Int?
)


data class MyMultiprofile(
    val mtp_id: Int,
    val platform: Int,
    val personnel: Int
)

data class MatchingA(
    val mtp_id: Int,
    val uid: Long
)

data class AgreeDisagreeList(
    val mtp_id: Int,
    val uname: String,
    val platform: Int
)

data class AgreeMtpId(
    val mtp_id: Int
)

data class DisagreeMtpId(
    val mtp_id: Int
)

data class CalendarMultiprofile(
    val mtp_id: Int,
    val platform: Int,
    val platform_name: String,
    val payment_day: Int,
    val price: Int,
    val master_uid: Long,
    val account_id: String,
    val account_pwd: String,
    val automatch_access: Int,
    val bank: String,
    val account_num: String
)

data class MemberName(
    val uname: String
)

data class MultiprofileUpdate(
    var account_id: String?,
    var account_pwd: String?,
    var automatch_access: Int,
    var bank: String?,
    var account_num: String?,
    var mtp_id: Int
)

data class AddSubscribe(
    var mypf_name: String,
    var price: Int,
    var payment_day: Int,
    var uid: Long
)

data class CalendarMyplatform(
    var mypf_id: Int,
    var mypf_name: String,
    var price: Int,
    var payment_day: Int
)

data class MyPlatformUpdate(
    var mypf_name: String?,
    var price: Int,
    var payment_day: Int,
    var mypf_id: Int
)

data class MyPlatformDelete(
    var mypf_id: Int
)

data class EditUser(
    var uid: Long = 0,
    var uname: String? = null,
    var email: Any? = null,
    var nickname: String?=null,
    var address_con: Int=0,
    var alert_before: Int=0
)

data class EditUserSwitch(
    var address_con: Int,
    var alert_before: Int,
    var uid: Long
)

data class MyPartyDelete(
    var mtp_id: Int,
    var platform: Int,
    var platform_name: String,
)

data class MyPartyUserDelete(
    var platform: Int,
    var platform_name: String,
    var uname: String
)

data class MyPartyUserDeleteBtn(
    var uid: Long
)

data class PlatformInfo(
    var platform_id: Int,
    var platform_name: String?,
    var price: Int
)

data class UserDeleteCheck(
    var mtp_id: Int
)

data class UserDelete(
    var uid: Long?
)
data class MatchingProgress(
    var platform: Int,
    var uid: Long,
    var time: String
)

data class MatchingInfo(
    var mtp_id: Int,
    var personnel: Int
)

data class MatchingCancel(
    var mtp_id: Int,
    var uid: Long
)

data class MatchingPayment(
    var uid :Long,
    var sid : String,
    var finalPrice : Int,
    var curTime : String,
    var mtpId: Int
)

data class MyPartyDeleteProgress(
    var mtp_id: Int
)

