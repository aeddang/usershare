package com.kakaovx.homet.user.constant

object ApiConst {

    /**
     * Rest Api Info
     */
    const val HOMET_DEFAULT_REST_ADDRESS = "http://api.kakaovx.co.kr"

    /**
     * API
     */
    const val HOMET_URL = "index.php"
//    const val HOMET_URL = ""

    /**
     * API URL
     */
    const val HOMET_API_PROGRAM_URL = "$HOMET_URL/program"
    const val HOMET_API_TRAINER_URL = "$HOMET_URL/trainer"

    /**
     * API FIELD DEFINED KEY
     */
    const val HOMET_API_FIELD_KEY_MODE = "mode"
    const val HOMET_API_FIELD_KEY_USER_ID = "user_id"
    const val HOMET_API_FIELD_KEY_KEYWORD = "keyword"
    const val HOMET_API_FIELD_KEY_CONFIRM_STATUS = "confirm_status"
    const val HOMET_API_FIELD_KEY_IS_DISPLAY = "is_display"
    const val HOMET_API_FIELD_KEY_DIFFICULTY = "difficulty"
    const val HOMET_API_FIELD_KEY_PROVIDER_ID = "provider_id"
    const val HOMET_API_FIELD_KEY_PROVIDER_NAME = "provider_name"
    const val HOMET_API_FIELD_KEY_SORT = "sort"
    const val HOMET_API_FIELD_KEY_PROGRAM_ID = "program_id"
    const val HOMET_API_FIELD_KEY_BODY_PART_LIST = "body_part_list"
    const val HOMET_API_FIELD_KEY_REGISTERED_PROGRAM = "registered_program"
    const val HOMET_API_FIELD_KEY_EXERCISE_ID = "exercise_id"
    const val HOMET_API_FIELD_KEY_PLAY_TIME = "play_time"
    const val HOMET_API_FIELD_KEY_ENABLED = "enabled"
    const val HOMET_API_FIELD_KEY_MOTION_ID = "motion_id"

    /**
     * API FIELD DEFINED VALUE
     */
    const val HOMET_API_FIELD_VALUE_NONE = ""
    const val HOMET_API_FIELD_VALUE_RECOMMEND_LIST = "home_recommend_program"
    const val HOMET_API_FIELD_VALUE_FREE_LIST = "home_free_program"
    const val HOMET_API_FIELD_VALUE_ISSUE_LIST = "home_issue_program"
    const val HOMET_API_FIELD_VALUE_PROGRAM_LIST = "program_list"
    const val HOMET_API_FIELD_VALUE_PROGRAM_CONTENT = "program_content"
    const val HOMET_API_FIELD_VALUE_EXERCISE_LIST = "exercise_list"
    const val HOMET_API_FIELD_VALUE_EXERCISE_CONTENT = "exercise_content"
    const val HOMET_API_FIELD_VALUE_MOTION_LIST = "motion_list"
    const val HOMET_API_FIELD_VALUE_MOTION_CONTENT = "motion_content"
    const val HOMET_API_FIELD_VALUE_TRAINER_LIST = "home_trainer"
}