package com.sample.gual

import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded


interface RetrofitService {

    // 헤더를 동적으로 업데이트
    // 헤더에서 인증을 받는다(로그인을 해야 페이지를 볼 수 있도록)
    @Headers(
        "accept: application/json",
        "content-type: application/json"
    )

    /* Retrofit - post*/
    // post - 1. login
    @POST("/login")
    fun postRequest(
        @Body jsonparams: LoginModel
    ): Call<PostResult>

    // post - 2. join
    // 미리 매개 변수를 정해두고 보냄(DataModel에서 형식 참고)
    // @FormUrlEncoded (<- @Feild와 같이 쓰임)
    @POST("/join")
    // @Body는 서버에서 유일한 매개 변수로 받고, 클라이언트에서 통째로 직렬화해서 보낼 때 사용
    // Retrofit이 Gson 컨버터와 함께 쓰이기 때문에 자바 오브젝트 직렬화 가능
    fun postRequest(
        @Body jsonparams: JoinModel
    ): Call<PostResult>

    //post -3. 중복확인
    @POST("/nicknamecheck")
    fun postRequest(
        @Body jsonparams: NicknameCheckModel
    ): Call<PostResult>

    // post - 4. address_con
    @POST("/addresscon")
    fun postRequest(
        @Body jsonparams: AddressConModel
    ): Call<PostResult>

    // post - 5. multiprofile
    @POST("/multiprofile")
    fun postRequest(
        @Body jsonparams: MultiProfileModel
    ): Call<PostResult>

    // post - 6. matchingA
    @POST("/matchingA")
    fun postRequest(
        @Body jsonparams: MatchingA
    ): Call<PostResult>

    // post - 7. multiprofileUpdate (계정주 멀티프로필 저징하기 버튼 눌렀을때 업데이트)
    @POST("/multiprofileUpdate")
    fun postRequest(
        @Body jsonparams: MultiprofileUpdate
    ): Call<PostResult>

    // post - 8. addSubscribe
    @POST("/addSubscribe")
    fun postRequest(
        @Body jsonparams: AddSubscribe
    ): Call<PostResult>

    // post - 9. myPlatformUpdate
    @POST("/myPlatformUpdate")
    fun postRequest(
        @Body jsonparams: MyPlatformUpdate
    ): Call<PostResult>

    // post - 10. myPlatformDelete
    @POST("/myPlatformDelete")
    fun postRequest(
        @Body jsonparams: MyPlatformDelete
    ): Call<PostResult>

    // post - 11. agree (수락 버튼 눌렀을 때 status 값을 1로 변경)
    @POST("/agree")
    fun postRequest(
        @Body jsonparams: AgreeMtpId
    ): Call<PostResult>

    // post - 12. disagree (거절 버튼 눌렀을 때 튜플 삭제)
    @POST("/disagree")
    fun postRequest(
        @Body jsonparams: DisagreeMtpId
    ): Call<PostResult>

    // post - 13. editUserSwitch
    @POST("/editUserSwitch")
    fun postRequest(
        @Body jsonparams: EditUserSwitch
    ): Call<PostResult>

    // post - 14. matchingProgress
    @POST("/matchingProgress")
    fun postRequest(
        @Body jsonparams: MatchingProgress
    ): Call<PostResult>

    // post - 15. matchingCancel
    @POST("/matchingCancel")
    fun postRequest(
        @Body jsonparams: MatchingCancel
    ): Call<PostResult>

    // post - 16. userDelete
    // post - 17. mypartyUserDeleteBtn

    //post - 18. matchingPayment
    @POST("/matchingPayment")
    fun postRequest(
        @Body jsonparams: MatchingPayment
    ): Call<PostResult>

    //post - 19. myPartyDeleteProgress
    @POST("/myPartyDeleteProgress")
    fun postRequest(
        @Body jsonparams: MyPartyDeleteProgress
    ): Call<PostResult>
    // post - 17. userDelete
    @POST("/userDelete")
    fun postRequest(
        @Body jsonparams: UserDelete
    ): Call<PostResult>

    // post - 18. mypartyUserDeleteBtn
    @POST("/mypartyUserDeleteBtn")
    fun postRequest(
        @Body jsonparams: MyPartyUserDeleteBtn
    ): Call<PostResult>

    /* Retrofit - get */
    // get - 1. friendplatform (multiprofile에 등록된 친구의 이름, platform 확인
    @GET("/friendplatform")
    fun getRequest(
        @Query("friendlist") friendlist: ArrayList<Long?>,
        @Query("uid") uid: Long
    ): Call<List<Friend>>

    // get - 2. myMultiprofile (로그인 한 유저와 multiprofile에 등록된 master_uid를 비교)
    @GET("/myMultiprofile")
    fun getRequest(
        @Query("uid") uid: Long
    ): Call<List<MyMultiprofile>>

    // get - 3. agreeDisagree
    @GET("/agreeDisagree")
    fun getRequest(
        @Query("mtp_id") mtp_id: Int
    ): Call<List<AgreeDisagreeList>>

    // get - 4. cMultiprofile (로그인한 유저의 multiprofile 정보 가져오기기
    @GET("/cMultiprofile")
    fun getRequest(
        @Query("uid") uid: Long?
    ): Call<List<CalendarMultiprofile>>

    // get - 5. memberName (파티원 이름 가지고오기)
    @GET("/memberName")
    fun getmRequest(
        @Query("mtp_id") mtp_id: Int
    ): Call<List<MemberName>>

    // get - 6. calendarMyplatform
    @GET("/calendarMyplatform")
    fun getcRequest(
        @Query("uid") uid: Long?
    ): Call<List<CalendarMyplatform>>

    // get - 7. editUser
    @GET("/editUser")
    fun getuRequest(
        @Query("uid") uid: Long?
    ): Call<List<EditUser>>

    // get - 8. mypartyDelete
    @GET("/mypartyDelete")
    fun getsRequest(
        @Query("uid") uid: Long?
    ): Call<List<MyPartyDelete>>

    // get - 9. platformInfo
    @GET("/platformInfo")
    fun getpRequest(
        @Query("platform_id") platform_id: Int
    ): Call<List<PlatformInfo>>

    // get - 10. mypartyUserDelete
    @GET("/mypartyUserDelete")
    fun getoRequest(
        @Query("uid") uid: Long?
    ): Call<List<MyPartyUserDelete>>

    // get - 11. matchingInfo
    @GET("/matchingInfo")
    fun getMatchingRequest(
        @Query("platform") platform:Int
    ): Call<List<MatchingInfo>>

    // get - 12. userDeleteCheck
    @GET("/userDeleteCheck")
    fun getiRequest(
        @Query("uid") uid: Long?
    ): Call<List<UserDeleteCheck>>

}