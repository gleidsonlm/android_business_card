package com.gleidsonlm.businesscard.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until

/**
 * Package of the app under test.
 */
const val TARGET_PACKAGE = "com.gleidsonlm.businesscard"

/**
 * Resource ids exposed by `Modifier.testTag` + `testTagsAsResourceId` in `:app`.
 * Keep in sync with `com.gleidsonlm.businesscard.ui.TestTags`.
 */
object Tags {
    const val CARD_SCREEN = "card_screen"
    const val CARD_QR_CODE = "card_qr_code"
    const val CARD_FULL_NAME = "card_full_name"
    const val BUTTON_THREAT_EVENTS = "button_threat_events"
    const val BUTTON_EDIT = "button_edit"

    const val INPUT_SCREEN = "input_screen"
    const val INPUT_FULL_NAME = "input_full_name"
    const val INPUT_TITLE = "input_title"
    const val INPUT_PHONE = "input_phone"
    const val INPUT_EMAIL = "input_email"
    const val INPUT_COMPANY = "input_company"
    const val INPUT_WEBSITE = "input_website"
    const val INPUT_BUTTON_SAVE = "input_button_save"

    const val THREAT_LIST_SCREEN = "threat_list_screen"
    const val THREAT_LIST_BACK = "threat_list_back"
    const val THREAT_LIST_REFRESH = "threat_list_refresh"
    const val THREAT_EVENT_CARD = "threat_event_card"
}

private const val UI_TIMEOUT_MS = 10_000L

private val RUNTIME_PERMISSIONS = listOf(
    "android.permission.READ_PHONE_STATE",
    "android.permission.ACCESS_COARSE_LOCATION",
)

/**
 * Puts the app in a known first-run state: no saved card, runtime permissions granted
 * (so the system permission dialogs requested in `MainActivity.onCreate` do not cover the UI).
 */
fun MacrobenchmarkScope.resetAppState() {
    device.executeShellCommand("pm clear $packageName")
    RUNTIME_PERMISSIONS.forEach { device.executeShellCommand("pm grant $packageName $it") }
}

/** Waits for the element with the given test tag to appear; fails the run if it never does. */
fun MacrobenchmarkScope.waitForTag(tag: String, timeoutMs: Long = UI_TIMEOUT_MS) {
    check(device.wait(Until.hasObject(By.res(tag)), timeoutMs)) {
        "Timed out after ${timeoutMs}ms waiting for test tag '$tag'"
    }
}

fun MacrobenchmarkScope.clickTag(tag: String) {
    waitForTag(tag)
    device.findObject(By.res(tag)).click()
}

fun MacrobenchmarkScope.typeIntoTag(tag: String, value: String) {
    waitForTag(tag)
    device.findObject(By.res(tag)).text = value
}

/**
 * Cold start journey: launch the app and wait for the first real screen. On a first run that is
 * the input form; afterwards it is the business card. This is the path a Startup Profile should cover.
 */
fun MacrobenchmarkScope.startApp() {
    pressHome()
    startActivityAndWait()
}

/** Fills the input form and saves it, landing on the business card screen. */
fun MacrobenchmarkScope.fillAndSaveCard() {
    waitForTag(Tags.INPUT_SCREEN)
    typeIntoTag(Tags.INPUT_FULL_NAME, "Ada Lovelace")
    typeIntoTag(Tags.INPUT_TITLE, "Software Engineer")
    typeIntoTag(Tags.INPUT_PHONE, "+15551234567")
    typeIntoTag(Tags.INPUT_EMAIL, "ada@example.com")
    typeIntoTag(Tags.INPUT_COMPANY, "Analytical Engines Ltd")
    typeIntoTag(Tags.INPUT_WEBSITE, "https://example.com")
    scrollToSaveAndClick()
    waitForTag(Tags.CARD_SCREEN)
    // QR code is generated asynchronously after the card is shown.
    waitForTag(Tags.CARD_QR_CODE)
}

/** Opens the threat events list, exercises refresh, optionally opens an event, then returns. */
fun MacrobenchmarkScope.browseThreatEvents() {
    clickTag(Tags.BUTTON_THREAT_EVENTS)
    waitForTag(Tags.THREAT_LIST_SCREEN)
    clickTag(Tags.THREAT_LIST_REFRESH)
    device.waitForIdle()

    // ThreatEventActivity is not exported, so the detail screen is only reachable through a real
    // event. If the list has one (e.g. received from Appdome), open it to capture that path too.
    device.findObject(By.res(Tags.THREAT_EVENT_CARD))?.let {
        it.click()
        device.waitForIdle()
        device.pressBack()
    }

    clickTag(Tags.THREAT_LIST_BACK)
    waitForTag(Tags.CARD_SCREEN)
}

/** Edits the existing card and saves it again (card -> input -> card). */
fun MacrobenchmarkScope.editCard() {
    clickTag(Tags.BUTTON_EDIT)
    waitForTag(Tags.INPUT_SCREEN)
    typeIntoTag(Tags.INPUT_TITLE, "Principal Engineer")
    scrollToSaveAndClick()
    waitForTag(Tags.CARD_SCREEN)
}

/**
 * The input form is a vertically scrollable column and the IME can cover the Save button.
 * Scroll the form down (never `pressBack()`, which would leave the app if the IME is hidden)
 * until Save is on screen, then click it.
 */
private fun MacrobenchmarkScope.scrollToSaveAndClick() {
    repeat(3) {
        if (device.hasObject(By.res(Tags.INPUT_BUTTON_SAVE))) return@repeat
        device.findObject(By.res(Tags.INPUT_SCREEN))?.scroll(Direction.DOWN, 1f)
    }
    clickTag(Tags.INPUT_BUTTON_SAVE)
}
