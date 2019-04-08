package com.kakaovx.homet.user.ui.page.report.program


import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.network.RxObservableConverter
import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.network.model.ProgramData
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ApiModel
import com.kakaovx.homet.user.util.Log
import io.reactivex.disposables.Disposable

class PageProgramReportViewModel(repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName
    private val restApi = repo.restApi

    val programList: ProgramList = ProgramList(restApi)

    fun onCreateView() {

    }

    fun onDestroyView() {
        programList.cancel()
    }

    fun getProgramList(): Disposable {
        return programList.load()
    }
}


class ProgramList( restApi: RestfulApi) : ApiModel<List<ProgramData>>( restApi) {


    override fun load(param: Map<String, Any>?): Disposable {
        Log.d(TAG, "getProgramData()")
        return RxObservableConverter.forNetwork( restApi.getProgramList()).subscribe(
            { this.handleComplete(it.data) },
            { this.handleError(it) }
        )
    }

    override fun cancel() {

    }
}
