package com.kakaovx.homet.user.component.ui.skeleton.model.data

import com.kakaovx.homet.user.component.model.HomeFreeWorkoutModel
import com.kakaovx.homet.user.component.model.HomeProgramModel
import com.kakaovx.homet.user.component.model.HomeRecommendModel
import com.kakaovx.homet.user.component.model.HomeTrainerModel
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.component.network.model.ResultData

class PageLiveData {
    var cmd: Int = AppConst.LIVE_DATA_CMD_NONE
    var message: String? = null
    var listItemType: Int = AppConst.HOMET_LIST_ITEM_INDEX
    var item: ArrayList<ResultData>? = null
    var homeProgramModel: HomeProgramModel? = null
    var homeFreeWorkoutModel: HomeFreeWorkoutModel? = null
    var homeTrainerModel: HomeTrainerModel? = null
    var homeRecommendModel: HomeRecommendModel? = null
}