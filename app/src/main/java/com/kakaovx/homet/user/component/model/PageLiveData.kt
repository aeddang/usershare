package com.kakaovx.homet.user.component.model

import com.kakaovx.homet.user.component.model.HomeFreeWorkoutModel
import com.kakaovx.homet.user.component.model.HomeProgramModel
import com.kakaovx.homet.user.component.model.HomeIssueProgramModel
import com.kakaovx.homet.user.component.model.HomeTrainerModel
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.constant.AppConst

class PageLiveData {

    var cmd: Int = AppConst.LIVE_DATA_CMD_NONE
    var message: String? = null
    var listItemType: Int = AppConst.HOMET_LIST_ITEM_INDEX
    var item: ArrayList<ResultData>? = null
    var homeProgramModel: HomeProgramModel? = null
    var homeFreeWorkoutModel: HomeFreeWorkoutModel? = null
    var homeTrainerModel: HomeTrainerModel? = null
    var homeIssueTags: ArrayList<String>? = null
    var homeIssueProgramModel: HomeIssueProgramModel? = null
}