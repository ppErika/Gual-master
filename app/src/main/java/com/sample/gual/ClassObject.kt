package com.sample.gual

data class MyCalendar(val mtp_id: Int,
                      val platform: Int,
                      val platform_name: String,
                      val payment_day: Int,
                      val price: Int,
                      val master_uid: Long,
                      val account_id: String,
                      val account_pwd: String,
                      val automatch_access: Int,
                      val bank: String,
                      val account_num: String)
data class MyParty(val platformImage: Int, val platformName: String, val userName: String)
data class MyPartyDeleteClass(
    val mtp_id: Int,
    val platformImage: Int,
    val platformName: String
)
data class MyPartyUserDeleteClass(
    val platformImage: Int,
    val platformName: String,
    val ownerName:String
)
data class FriendMultiprofile(val mtp_id: Int, val uname: String, val platformImage: Int, val personnel: Int, val status: Int?)
data class AgreeDisagree(val mtp_id: Int, val uname: String, val platform: Int)
data class NotiData(val noti_img: Int, val text: String, val date: String)