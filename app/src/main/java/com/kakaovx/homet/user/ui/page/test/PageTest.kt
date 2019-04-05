package com.kakaovx.homet.user.ui.page.test

import android.Manifest
import android.widget.Toast
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.lib.page.PageRequestPermission
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.component.ui.view.toast.VXToast
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.page_test.*


class PageTest : RxPageFragment() {
    val TAG = javaClass.simpleName
    override fun getLayoutResId(): Int { return R.layout.page_test }

    override fun onCreatedView() {
        AndroidSupportInjection.inject(this)
        super.onCreatedView()
    }

    override fun onSubscribe() {
        super.onSubscribe()
        btnGo.clicks().subscribe(this::onGo).apply { disposables.add(this) }
        btnPopup.clicks().subscribe(this::onPopup).apply { disposables.add(this) }
        btnShare.clicks().subscribe(this::onShare).apply { disposables.add(this) }
        btnCamera.clicks().subscribe(this::onCamera).apply { disposables.add(this) }
        btnPermission.clicks().subscribe(this::onPermission).apply { disposables.add(this) }
        btnGraph.clicks().subscribe(this::onGraph).apply { disposables.add(this) }
    }

    /**
     * 페이지 이동
     */
    @Suppress("UNUSED_PARAMETER")
    private fun onGo(v: Unit) {
        //일반이동
        //PagePresenter.getInstance<PageID>().pageChange(PageID.TRAINER)

        //param 등록시 -> call setParam(param: Map<String, Any>)
        val param = HashMap<String, Any>()
        param["TEST"] = "TEST"
        PagePresenter.getInstance<PageID>().pageChange(PageID.TRAINER, param)
    }

    /**
     * 팝업 페이지 호출
     */
    @Suppress("UNUSED_PARAMETER")
    private fun onPopup(v: Unit) {
        //일반팝업
        // PagePresenter.getInstance<PageID>().openPopup(PageID.POPUP_PLAYER)

        // 화면전환 이미지 공유
        val param = HashMap<String, Any>()
        param[PopupDividedGestureTest.SHARE_IMAGE_KEY] = imageView.drawable
        PagePresenter.getInstance<PageID>().openPopup(PageID.POPUP_DIVIDED_GESTURE_TEST, param, imageView, PopupDividedGestureTest.SHARE_IMAGE_NAME)
    }

    /**
     * 공유하기
     * title
     * imagePath
     * 설명
     * 링크시 이동할페이지
     * 페이지에 전달할 param
     */
    @Suppress("UNUSED_PARAMETER")
    private fun onShare(v: Unit) {
        val param = HashMap<String, Any>()
        param["TEST"] = "test"
        val ac = activity as MainActivity
        ac.deepLinkManager.sendSns("testTitle", "testPath", "testDesc",PageID.TEST, param, true)
    }

    /**
     * 카메라모듈
     */
    @Suppress("UNUSED_PARAMETER")
    private fun onCamera(v: Unit) {
        PagePresenter.getInstance<PageID>().openPopup(PageID.POPUP_CAMERA)
    }

    /**
     * 퍼미션 요청
     * 퍼미션 필요시점에서 체크필수
     * 이미 요청완료시 -> onRequestPermissionResult
     */
    @Suppress("UNUSED_PARAMETER")
    private fun onPermission(v: Unit) {
        // 미리체크
        //PagePresenter.getInstance<PageID>().hasPermissions(arrayOf( Manifest.permission.CAMERA ) )

        PagePresenter.getInstance<PageID>().requestPermission( arrayOf( Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE ),
            object : PageRequestPermission{
                override fun onRequestPermissionResult(resultAll: Boolean, permissions: List<Boolean>?) {
                    Log.d(TAG, "resultAll $resultAll")
                    permissions?.let {  permission ->
                        var log = ""
                        permission.forEachIndexed { index, b ->
                                  log += " $index $b"
                        }
                        Log.d(TAG, "permissions $log")
                    }
                    if( resultAll ) return
                    context?.let { VXToast.makeToast(it, R.string.error_need_permission, Toast.LENGTH_SHORT).show() }
                }
            })
    }

    /**
     * 그래프 테스트 페이지 호출
     */
    @Suppress("UNUSED_PARAMETER")
    private fun onGraph(v: Unit) {
        PagePresenter.getInstance<PageID>().openPopup(PageID.POPUP_GRAPH_TEST)
    }


}