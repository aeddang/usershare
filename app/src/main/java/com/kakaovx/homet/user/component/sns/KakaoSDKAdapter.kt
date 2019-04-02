package com.kakaovx.homet.user.component.sns

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.widget.Toast
import com.kakao.auth.*
import com.kakao.network.ErrorResult
import com.kakao.util.helper.log.Logger
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.component.ui.view.toast.VXToast
import com.kakaovx.homet.user.ui.PageID
import java.io.UnsupportedEncodingException
import java.util.*

class KakaoSDKAdapter : KakaoAdapter() {
    companion object {
        const val PROPERTY_DEVICE_ID = "device_id"
    }

    override fun getSessionConfig(): ISessionConfig {
        return object : ISessionConfig {
            override fun getAuthTypes(): Array<AuthType> {
                return arrayOf(AuthType.KAKAO_LOGIN_ALL)
            }

            override fun isUsingWebviewTimer(): Boolean {
                return false
            }

            override fun isSecureMode(): Boolean {
                return false
            }

            override fun getApprovalType(): ApprovalType? {
                return ApprovalType.INDIVIDUAL
            }

            override fun isSaveFormData(): Boolean {
                return true
            }
        }
    }

    override fun getApplicationConfig(): IApplicationConfig {
        return IApplicationConfig { PagePresenter.getInstance<PageID>().activity!!.getCurrentContext() }
    }

    override fun getPushConfig(): IPushConfig {
        return object : IPushConfig {

            @SuppressLint("MissingPermission", "HardwareIds")
            override fun getDeviceUUID(): String {

                val deviceUUID: String?
                val cache = Session.getCurrentSession().appCache
                val id = cache.getString(PROPERTY_DEVICE_ID)

                if (id != null) {
                    deviceUUID = id
                    return deviceUUID
                } else {
                    var uuid: UUID? = null
                    val context = applicationConfig.applicationContext
                    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                    uuid = try {
                        if ("9774d56d682e549c" != androidId) {
                            UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
                        } else {
                            var deviceId: String? = null
                            if( PagePresenter.getInstance<Any>().hasPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE)) ) {
                                deviceId = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId  // 페이지에서 미리체크함 무시
                            }
                            if (deviceId != null) UUID.nameUUIDFromBytes(deviceId.toByteArray(charset("utf8"))) else UUID.randomUUID()
                        }
                    } catch (e: UnsupportedEncodingException) {
                        throw RuntimeException(e)
                    }

                    val bundle = Bundle()
                    bundle.putString(PROPERTY_DEVICE_ID, uuid!!.toString())
                    cache.save(bundle)

                    deviceUUID = uuid.toString()
                    return deviceUUID
                }
            }

            override fun getTokenRegisterCallback(): ApiResponseCallback<Int> {
                return object : ApiResponseCallback<Int>() {
                    override fun onFailure(errorResult: ErrorResult?) {
                        VXToast.makeToast(
                            applicationConfig.applicationContext,
                            errorResult!!.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onSessionClosed(errorResult: ErrorResult) {
                        Logger.e(errorResult.errorMessage)
                        Logger.e("login again...")
                    }

                    override fun onNotSignedUp() {
                        Logger.e("You should signup first")
                    }

                    override fun onSuccess(result: Int?) {
                        VXToast.makeToast(
                            applicationConfig.applicationContext,
                            "succeeded to register fcm token...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}