package com.kakaovx.homet.user.ui.oldPlayer

import android.content.Intent
import android.os.Bundle
import com.kakao.i.KakaoI
import com.kakao.i.KakaoIListeningBinder
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.VxCoreObserver
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.vxcore.VxKakaoI
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log
import dagger.android.support.DaggerAppCompatActivity
import org.jetbrains.anko.toast
import javax.inject.Inject

class PlayerActivity : DaggerAppCompatActivity() {

    val TAG = javaClass.simpleName

    @Inject
    lateinit var repository: Repository

    private var kakaoI: VxKakaoI? = null

    private val kakaoIEventListener: KakaoIListeningBinder.EventListener = object: KakaoIListeningBinder.EventListener {
        override fun onStartListen() {
            Log.d(TAG, "onStartListen()")
        }

        override fun onWithdrawal() {
            Log.d(TAG, "onWithdrawal()")
            // 탈퇴됨
        }

        override fun onAuthorizeFailed() {
            Log.d(TAG, "onAuthorizeFailed()")
            // 토큰이 만료됨
        }

        override fun onStopListen() {
            Log.d(TAG, "onStopListen()")
        }

        override fun onAgreementRequired(followingIntentFunc: KakaoI.IntentSupplier) {
            Log.d(TAG, "onAgreementRequired()")
            // 약관 동의 필요
            startActivity(followingIntentFunc.supply(this@PlayerActivity))
        }

        override fun onError(e: Exception?) {
            Log.d(TAG, "onError()")
        }

        override fun onMicUnavailable() {
            Log.d(TAG, "onMicUnavailable()")
        }
    }

    private val kakaoIAccountCallback: KakaoI.OnCheckCallback = object: KakaoI.OnCheckCallback {
        override fun onSuccess() {
            Log.d(TAG, "onSuccess()")
            kakaoI?.onSuccess()
        }

        override fun onAuthorizeFailed() {
            Log.d(TAG, "onAuthorizeFailed()")
            // 토큰 만료
        }

        override fun onSignUpRequired(followingIntentFunc: KakaoI.IntentSupplier) {
            Log.d(TAG, "onSignUpRequired()")
            // 가입필요
            startActivity(followingIntentFunc.supply(this@PlayerActivity))
        }

        override fun onAgreementRequired(followingIntentFunc: KakaoI.IntentSupplier) {
            Log.d(TAG, "onAgreementRequired()")
            // 약관 동의 필요
            startActivity(followingIntentFunc.supply(this@PlayerActivity))
        }

        override fun onError(e: Exception?) {
            Log.d(TAG, "onError()")
            // 알수 없는 오류
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val id = intent.getStringExtra(AppConst.HOMET_VALUE_MOTION_ID)
        val url = intent.getStringExtra(AppConst.HOMET_VALUE_VIDEO_URL)

        kakaoI = repository.kakaoI

        kakaoI?.apply {
            initKakaoI(kakaoIAccountCallback, kakaoIEventListener)
            VxCoreObserver.addObserver{ observable, _ ->
                if (observable is VxCoreObserver) {
                    observable.getData()?.let {
                        if (it.cmd == AppConst.LIVE_DATA_VX_CMD_KAKAOI) {
                            when (it.kakaoiCmd) {
                                AppConst.HOMET_KAKAOI_CMD_STATE -> {
                                    when (it.state) {
                                        KakaoI.STATE_DEACTIVATED -> {
                                            Log.d(TAG, "onStateChanged() KakaoI.STATE_DEACTIVATED")
                                        }
                                        KakaoI.STATE_IDLE -> {
                                            Log.d(TAG, "onStateChanged() KakaoI.STATE_IDLE")
                                        }
                                        KakaoI.STATE_PROCESSING -> {
                                            Log.d(TAG, "onStateChanged() KakaoI.STATE_PROCESSING")
                                        }
                                        KakaoI.STATE_RECOGNIZING -> {
                                            Log.d(TAG, "onStateChanged() KakaoI.STATE_RECOGNIZING")
                                        }
                                        else -> {
                                            Log.d(TAG, "onStateChanged() state=${it.state}")
                                        }
                                    }
                                }
                                AppConst.HOMET_KAKAOI_CMD_SEND_SPEECH_TEXT -> {
                                    it.message?.run { toast(this) }
                                }
                                AppConst.HOMET_KAKAOI_CMD_RECV_SPEECH_TEXT -> {
                                    it.message?.run { toast(this) }
                                }
                                AppConst.HOMET_KAKAOI_CMD_START_SETTING_ACTIVITY -> {
                                    startSettingActivity(this@PlayerActivity)
                                }
                                else -> {
                                    Log.e(TAG, "wrong kakao cmd")
                                }
                            }
                        } else if (it.cmd == AppConst.LIVE_DATA_VX_CMD_CAMERA) {

                        } else {
                            Log.e(TAG, "wrong cmd")
                        }
                    }
                }
            }
        } ?: Log.e(TAG, "kakaoI is null")

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PlayerFragment.newInstance(id, url))
                .commitNow()
        }
//        Log.d(TAG, "onCreate() default rotation = [${this.windowManager.defaultDisplay.rotation}]")
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d(TAG, "onNewIntent()")
        super.onNewIntent(intent)
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
    }

    override fun onStart() {
        Log.d(TAG, "onStart()")
        super.onStart()
        kakaoI?.onStart()
    }

    override fun onStop() {
        Log.d(TAG, "onStop()")
        super.onStop()
        kakaoI?.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()
        VxCoreObserver.deleteObservers()
    }
}