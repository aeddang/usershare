package com.kakaovx.homet.user.component.vxcore

//import com.kakao.i.service.KakaoIAgent
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.auth.IApplicationConfig
import com.kakao.auth.KakaoAdapter
import com.kakao.auth.Session
import com.kakao.i.KakaoI
import com.kakao.i.KakaoIAuth
import com.kakao.i.KakaoIListeningBinder
import com.kakao.i.http.KakaoIClient
import com.kakao.i.message.ExceptionBody
import com.kakao.i.message.InformRecognizedBody
import com.kakao.i.message.RequestBody
import com.kakao.i.message.ResponseBody
import com.kakao.i.util.SystemInfo
import com.kakaovx.homet.user.component.model.VxCoreLiveData
import com.kakaovx.homet.user.component.model.VxCoreObserver
import com.kakaovx.homet.user.component.preference.SettingPreference
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log
import okhttp3.Request
import okhttp3.Response

class VxKakaoI(val context: Context, val settings: SettingPreference) {

    private val TAG = javaClass.simpleName

    private var _core: MutableLiveData<VxCoreLiveData>? = null
    val core: LiveData<VxCoreLiveData>? get() = _core

    private var kakaoIBinder: KakaoIListeningBinder? = null
    private var kakaoIClient: KakaoIClient? = null
//    private var kakaoIAgent: KakaoIAgent? = null

    private var kakaoIAccountCallback: KakaoI.OnCheckCallback? = null
    private var kakaoIEventListener: KakaoIListeningBinder.EventListener? = null

    private val kakaoIClientInterceptor: KakaoIClient.Interceptor? = object : KakaoIClient.Interceptor {
        override fun onReceiveInstruction(request: Request?, responseBody: ResponseBody?) {
            if (responseBody == null) return
            val type = responseBody.instruction.header.type
            if ("Recognizer.InformRecognized" == type) {
                val inform = responseBody.instruction.getBody(InformRecognizedBody::class.java)
                val informType = inform.type
                Log.d(TAG, "onReceiveInstruction() Recognizer.InformRecognized getType = " + informType!!)
            } else if ("Recognizer.StopCapture" == type) {
                Log.d(TAG, "onReceiveInstruction() Recognizer.StopCapture")
            } else if ("Synthesizer.Speak" == type) {
                val inform = responseBody.instruction.getBody(InformRecognizedBody::class.java)
                val informText = inform.text
                Log.d(TAG, "onReceiveInstruction() Synthesizer.Speak getText = " + informText!!)
                sendCommand(AppConst.HOMET_KAKAOI_CMD_RECV_SPEECH_TEXT, informText)
            }
        }

        override fun onReceiveException(request: Request?, exceptionBody: ExceptionBody?) {
            Log.d(TAG, "onReceiveException()")
        }

        override fun onComplete(request: Request?) {
            Log.d(TAG, "onComplete()")
        }

        override fun onResponse(request: Request?, response: Response?) {
            Log.d(TAG, "onResponse()")
        }

        override fun onReceiveBinaryStream(request: Request?, i: Int) {
            Log.d(TAG, "onReceiveBinaryStream()")
        }

        override fun onRequest(request: Request?, responseBody: RequestBody?) {
            Log.d(TAG, "onRequest()")
        }

        override fun onError(request: Request?, exception: Exception?) {
            Log.d(TAG, "onError()")
        }
    }

    private val kakaoIAuthImpl = object: KakaoIAuth {
        override fun getAccessToken(): String {
            return Session.getCurrentSession().tokenInfo.accessToken
        }

        /**
         * NOTE. SnsKakao SDK 의 Application User ID 를 전달
         *
         * @see com.kakao.auth.network.response.AccessTokenInfoResponse#getUserId()
         */
        override fun getAppUserId(): Long {
            return settings.getAppUserId()
        }

        override fun getRefreshToken(): String {
            return Session.getCurrentSession().tokenInfo.refreshToken
        }
    }

    private val systemInfoProvider = object: SystemInfo() {
        override fun getAdvertisingId(): String {
            return ""
        }
    }

    private val kakaoAdapter = object: KakaoAdapter() {
        override fun getApplicationConfig(): IApplicationConfig {
            return IApplicationConfig { context }
        }
    }

    fun initKakaoI (checkCallback: KakaoI.OnCheckCallback, eventListener: KakaoIListeningBinder.EventListener) {
        kakaoIEventListener = eventListener
        kakaoIAccountCallback = checkCallback
    }

    fun onStart() {
        _core = MutableLiveData()

        KakaoI.checkAccount(kakaoIAccountCallback)
    }

    fun onStop() {
        kakaoIClient?.removeIntercepter(kakaoIClientInterceptor)
        kakaoIClient = null
        unBindKakaoI()

        _core = null
    }

    fun onSuccess() {
        if (KakaoI.isEnabled()) {
            bindKakaoI()
            kakaoIClient = KakaoI.getKakaoIClient()
            kakaoIClient?.apply {
                addInterceptor(kakaoIClientInterceptor)
            } ?: Log.e(TAG, "kakaoIClient is null")
        } else {
            // 사용 설정 확인
            sendCommand(AppConst.HOMET_KAKAOI_CMD_START_SETTING_ACTIVITY)
        }
    }

    fun getKakaoIAuth() = kakaoIAuthImpl
    fun getSystemProvider() = systemInfoProvider
    fun getKakaoSDKAdapter() = kakaoAdapter

    private fun sendCommand(cmd: Int, speechText: String? = null, state: Int = 0) {
        Log.d(TAG, "sendCommand() cmd = [$cmd]")
        val liveData = VxCoreLiveData()
        liveData.cmd = AppConst.LIVE_DATA_VX_CMD_KAKAOI
        liveData.kakaoiCmd = cmd
        liveData.message = speechText
        liveData.state = state
        VxCoreObserver.setData(liveData)
    }

    private fun bindKakaoI() {
        if (kakaoIBinder != null) {
            Log.w(TAG, "already bound")
            return
        }
        kakaoIBinder = KakaoI.startListen(context, kakaoIEventListener)
        kakaoIBinder?.apply {
            addListener(object: KakaoI.StateListener {
                override fun onResult(result: String?) {
                    Log.d(TAG, "onResult() result=$result")
                    sendCommand(AppConst.HOMET_KAKAOI_CMD_SEND_SPEECH_TEXT, result)
                }

                override fun onPartialResult(partialResult: String?) {
                    Log.d(TAG, "onPartialResult() result=$partialResult")
                }

                override fun onStateChanged(state: Int) {
                    sendCommand(AppConst.HOMET_KAKAOI_CMD_STATE, state = state)
                }
            })
        }
    }

    private fun unBindKakaoI() {
        kakaoIBinder?.apply { stopListen() }
        kakaoIBinder = null
    }

    fun startRecognition(enable: Boolean) {
        kakaoIBinder?.apply {
            if (enable) requestRecognition() else stopRecognition()
        } ?: Log.e(TAG, "startRecognition() kakaoIBinder is null")
    }

    fun startSettingActivity(ctx: Context) {
        Log.d(TAG, "start Setting Activity()")
        KakaoI.startSettingActivity(ctx) { error ->
            Log.e(TAG, "startSettingActivity() error = $error")
        }
    }
}