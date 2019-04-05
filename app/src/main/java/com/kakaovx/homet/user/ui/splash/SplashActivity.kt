package com.kakaovx.homet.user.ui.splash

import android.content.Intent
import android.os.Bundle
import com.kakao.auth.Session
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.deeplink.DeepLinkManager
import com.kakaovx.homet.user.component.firebase.FirebaseDynamicLink
import com.kakaovx.homet.user.util.AppPermissionManager
import com.kakaovx.homet.user.util.Log
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    val TAG = javaClass.simpleName

    private fun startFragmentTransaction(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SplashFragment.newInstance())
                .commitNow()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        /*
        if (!AppPermissionManager.checkPermissions(this)) {
            AppPermissionManager.requestPermissions(this)
        } else {
            Log.i(TAG, "Accept All User Permissions")
            startFragmentTransaction(savedInstanceState)
        }
        */
        startFragmentTransaction(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        var notOk = false
        when (requestCode) {
            AppPermissionManager.PERMISSIONS_REQUEST_CODE -> {
                for (i in grantResults.indices) {
                    if (grantResults[i] != 0) {
                        Log.e(TAG, "onRequestPermissionsResult = "
                                + permissions[i]
                                + ", grantResult = "
                                + "NOK")
                        notOk = true
                    }
                }
                if (notOk) {
                    finish()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SplashFragment.newInstance())
                        .commitNow()
                }
            }
            else -> {
                Log.e(TAG, "unknown error")
            }
        }
    }
}
