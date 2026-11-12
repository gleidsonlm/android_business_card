package com.gleidsonlm.businesscard.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Generates the Baseline Profile by exercising the app's main user journeys:
 *
 * 1. Cold start into the first-run input form.
 * 2. Fill in and save the card (ViewModel + repository + vCard/QR generation).
 * 3. Business card screen with QR code.
 * 4. Threat events list (and the detail screen when an event exists).
 * 5. Edit the card and save again.
 *
 * Run on a Pixel 6 / Android 14 device with `./gradlew :app:generateBaselineProfile`.
 */
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        packageName = TARGET_PACKAGE,
    ) {
        device.resetAppState()

        // Cold start -> first-run input form -> saved business card
        startApp()
        device.fillAndSaveCard()

        // Secondary journeys from the business card screen
        device.browseThreatEvents()
        device.editCard()
    }
}
