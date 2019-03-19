package com.kakaovx.homet.user.ui.page


import android.view.View
import com.kakaovx.homet.user.R
import com.kakaovx.homet.lib.page.PageGestureView
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageDividedGestureFragment
import kotlinx.android.synthetic.main.popup_player.*

class PopupPlayer : RxPageDividedGestureFragment() {

    private val TAG = javaClass.simpleName


    override fun getLayoutResId(): Int { return R.layout.popup_player }
    override fun getGestureView(): PageGestureView { return gestureView }
    override fun getContentsView(): View { return contents }
    override fun getBackgroundView(): View { return bg }
    override fun getDividedView(): View { return divided }

    override fun onCreated() {
        super.onCreated()
        player.onInit()
        camera.onInit()
        player.load("http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4")
        player.resume()
        camera.takePicture()
    }


    override fun onResume() {
        super.onResume()
        player.onResume()
        camera.onResume()
    }

    override fun onPause() {
        super.onPause()
        player.onPause()
        camera.onPause()
    }


}