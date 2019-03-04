package com.kakaovx.homet.user.component.network.api

import com.kakaovx.homet.user.component.network.model.*
import com.kakaovx.homet.user.constant.ApiConst
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RestfulApi {

    @FormUrlEncoded
    @POST(ApiConst.HOMET_API_PROGRAM_URL)
    fun getProgramList(@Field(ApiConst.HOMET_API_FIELD_KEY_MODE) key: String
                       = ApiConst.HOMET_API_FIELD_VALUE_PROGRAM_LIST): Observable<ApiResponse<ProgramData>>

    @FormUrlEncoded
    @POST(ApiConst.HOMET_API_PROGRAM_URL)
    fun getIssueProgramList(@Field(ApiConst.HOMET_API_FIELD_KEY_MODE) key: String
                            = ApiConst.HOMET_API_FIELD_VALUE_ISSUE_LIST): Observable<ApiResponse<ProgramData>>

    @FormUrlEncoded
    @POST(ApiConst.HOMET_API_PROGRAM_URL)
    fun getRecommendProgramList(@Field(ApiConst.HOMET_API_FIELD_KEY_MODE) key: String
                                = ApiConst.HOMET_API_FIELD_VALUE_RECOMMEND_LIST,
                                @Field(ApiConst.HOMET_API_FIELD_KEY_USER_ID) id: String
                                = ApiConst.HOMET_API_FIELD_VALUE_NONE): Observable<ApiResponse<ProgramData>>

    @FormUrlEncoded
    @POST(ApiConst.HOMET_API_PROGRAM_URL)
    fun getWorkoutList(@Field(ApiConst.HOMET_API_FIELD_KEY_MODE) key: String
                       = ApiConst.HOMET_API_FIELD_VALUE_EXERCISE_LIST): Observable<ApiResponse<WorkoutData>>

    @FormUrlEncoded
    @POST(ApiConst.HOMET_API_PROGRAM_URL)
    fun getFreeWorkoutList(@Field(ApiConst.HOMET_API_FIELD_KEY_MODE) key: String
                                      = ApiConst.HOMET_API_FIELD_VALUE_FREE_LIST): Observable<ApiResponse<WorkoutData>>

    @FormUrlEncoded
    @POST(ApiConst.HOMET_API_TRAINER_URL)
    fun getTrainerList(@Field(ApiConst.HOMET_API_FIELD_KEY_MODE) key: String
                           = ApiConst.HOMET_API_FIELD_VALUE_TRAINER_LIST,
                       @Field(ApiConst.HOMET_API_FIELD_KEY_USER_ID) id: String
                           = "0"): Observable<ApiResponse<TrainerData>>
}