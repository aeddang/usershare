package com.kakaovx.homet.user.constant

object AppConst {

    private const val packageName = "com.kakaovx.homet.user"

    /**
     * Title Tags
     */
    const val APP_TAG = "VX_"

    /**
     * Action names
     */

    const val HOMET_ACTIVITY_SPLASH = "$packageName.action.HOMET_SPLASH"
    const val HOMET_ACTIVITY_MAIN = "$packageName.action.HOMET_MAIN"

    /**
     * Values
     */

    const val HOMET_VALUE_NOTITLE = "NOTITLE"
    const val HOMET_VALUE_SPLASH = "SPLASH"
    const val HOMET_VALUE_HOME = "HOME"
    const val HOMET_VALUE_PROGRAM = "PROGRAM"
    const val HOMET_VALUE_PLANNER = "PLANNER"
    const val HOMET_VALUE_SEARCH = "SEARCH"
    const val HOMET_VALUE_PROFILE = "PROFILE"

    /**
     * LiveData Model index
     */
    const val LIVE_DATA_CMD_NONE = 0x0100
    const val LIVE_DATA_CMD_STRING = 0x0101
}