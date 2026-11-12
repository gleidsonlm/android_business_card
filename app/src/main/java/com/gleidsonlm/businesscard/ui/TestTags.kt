package com.gleidsonlm.businesscard.ui

/**
 * Stable identifiers for UI elements.
 *
 * These are applied with `Modifier.testTag(...)` and exposed as Android resource ids
 * (via `testTagsAsResourceId`) so that UiAutomator-based tooling — the Baseline Profile
 * generator, Startup Profile generator and macrobenchmarks in `:baselineprofile` — can
 * find elements with `By.res(...)` without depending on localized text.
 *
 * Keep these values stable: the `:baselineprofile` module duplicates the strings it uses.
 */
object TestTags {
    // Business card screen
    const val CARD_SCREEN = "card_screen"
    const val CARD_AVATAR = "card_avatar"
    const val CARD_QR_CODE = "card_qr_code"
    const val CARD_DETAILS = "card_details"
    const val CARD_FULL_NAME = "card_full_name"
    const val CARD_LINK_PHONE = "card_link_phone"
    const val CARD_LINK_EMAIL = "card_link_email"
    const val CARD_LINK_WEBSITE = "card_link_website"
    const val BUTTON_THREAT_EVENTS = "button_threat_events"
    const val BUTTON_EDIT = "button_edit"
    const val BUTTON_SHARE = "button_share"

    // User input screen
    const val INPUT_SCREEN = "input_screen"
    const val INPUT_AVATAR_PREVIEW = "input_avatar_preview"
    const val INPUT_BUTTON_GALLERY = "input_button_gallery"
    const val INPUT_BUTTON_CAMERA = "input_button_camera"
    const val INPUT_BUTTON_GRAVATAR = "input_button_gravatar"
    const val INPUT_FULL_NAME = "input_full_name"
    const val INPUT_TITLE = "input_title"
    const val INPUT_PHONE = "input_phone"
    const val INPUT_EMAIL = "input_email"
    const val INPUT_COMPANY = "input_company"
    const val INPUT_WEBSITE = "input_website"
    const val INPUT_BUTTON_SAVE = "input_button_save"

    // Threat event list screen
    const val THREAT_LIST_SCREEN = "threat_list_screen"
    const val THREAT_LIST_BACK = "threat_list_back"
    const val THREAT_LIST_REFRESH = "threat_list_refresh"
    const val THREAT_LIST_CLEAR = "threat_list_clear"
    const val THREAT_LIST_EVENTS = "threat_list_events"
    const val THREAT_LIST_EMPTY = "threat_list_empty"
    const val THREAT_EVENT_CARD = "threat_event_card"

    // Threat event detail screen (ThreatEventActivity)
    const val THREAT_DETAIL_SCREEN = "threat_detail_screen"
    const val THREAT_DETAIL_LIST = "threat_detail_list"
}
