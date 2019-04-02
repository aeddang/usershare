package com.kakaovx.homet.user
import androidx.fragment.app.Fragment
import com.facebook.stetho.Stetho
import com.kakao.auth.KakaoSDK
import com.kakao.common.KakaoPhase
import com.kakao.common.PhaseInfo
import com.kakao.i.KakaoI
import com.kakao.i.KakaoIAuth
import com.kakao.i.di.CoreModule
import com.kakao.i.util.SystemInfo
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.di.DaggerAppComponent
import com.kakaovx.homet.user.util.Log
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import com.kakao.auth.IApplicationConfig
import com.kakao.auth.ApprovalType
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionConfig
import com.kakao.auth.KakaoAdapter




class App: DaggerApplication(), HasSupportFragmentInjector {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var repo: Repository

    override fun applicationInjector(): AndroidInjector<out App> {
        return DaggerAppComponent.builder().create(this)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreate() {
        super.onCreate()

        if (AppFeature.APP_MEMORY_DEBUG) {
            Log.d(TAG, "Start Memory Debug")
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return
            }
        }
        if (AppFeature.APP_REMOTE_DEBUG) {
            Log.d(TAG, "Start Remote Debug")
            Stetho.initializeWithDefaults(this)
        }

        repo.kakaoI.run {
            KakaoSDK.init(getKakaoSDKAdapter(), object : PhaseInfo {
                override fun phase(): KakaoPhase {
//                Log.d(TAG, "onCreate() KakaoSDK phase()");
                    return KakaoPhase.ofName("sandbox")
                }

                override fun appKey(): String? {
//                Log.d(TAG, "onCreate() KakaoSDK appKey() = " + key);
                    return getString(R.string.demo_app_key)
                }

                override fun clientSecret(): String? {
                    //                Log.d(TAG, "onCreate() KakaoSDK clientSecret()");
                    return null
                }
            })

            // TODO: Kakao i SDK 1.2.1.x 로 올린 후, "sandbox"로 될 수 있도록 주석 풀어야함.
            KakaoI.with(this@App, "sproxy" /* BuildConfig.PHASE */) // phase 는 서버군을 특정. (기본값은 real)
                .module(object : CoreModule() {
                    override fun provideKakaoIAuth(): KakaoIAuth {
                        Log.d(TAG, "onCreate() KakaoI provideKakaoIAuth()")
                        return getKakaoIAuth()
                    }

                    override fun provideSystemInfo(): SystemInfo {
                        Log.d(TAG, "onCreate() KakaoI provideSystemInfo()")
                        return getSystemProvider()
                    }
                })
                .setDebugEnabled(true)
                .init()
        }

    }
}