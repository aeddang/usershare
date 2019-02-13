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
    const val LIVE_DATA_CMD_ITEM = 0x0102

    /**
     * List Item Type
     */
    const val HOMET_LIST_ITEM_PROGRAM = 0x0200
    const val HOMET_LIST_ITEM_WORKOUT = 0x0201
    const val HOMET_LIST_ITEM_FREE_WORKOUT = 0x0202
    const val HOMET_LIST_ITEM_TRAINER = 0x0203

    /**
     * Home List Item Type
     */
    const val HOMET_LIST_ITEM_HOME_INDEX = 0x300
    const val HOMET_LIST_ITEM_HOME_PROGRAM = 0x300
    const val HOMET_LIST_ITEM_HOME_WORKOUT_TYPE = 0x301
    const val HOMET_LIST_ITEM_HOME_BANNER = 0x302
    const val HOMET_LIST_ITEM_HOME_FREE_WORKOUT = 0x303
    const val HOMET_LIST_ITEM_HOME_TRAINER = 0x304
    const val HOMET_LIST_ITEM_HOME_HASH_TAG = 0x305
}