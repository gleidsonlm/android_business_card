package com.gleidsonlm.businesscard.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Generates the Startup Profile: only the code that runs on a cold start of a returning user, up
 * to the first rendered business card. R8 uses it for DEX layout optimization
 * (`dexLayoutOptimization = true` in `:app`), placing startup classes together to cut page faults.
 *
 * Unlike the Baseline Profile, nothing but the start path may be inside the collected block, because
 * everything in it ends up in the startup profile. The saved card is therefore seeded in [setUp],
 * outside the profile block.
 */
@RunWith(AndroidJUnit4::class)
class StartupProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    private val device: UiDevice =
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun setUp() {
        // Returning-user state: a saved card, so the cold start lands directly on the card screen.
        device.resetAppState()
        device.executeShellCommand("am start -W -n $TARGET_PACKAGE/.MainActivity")
        device.fillAndSaveCard()
        device.executeShellCommand("am force-stop $TARGET_PACKAGE")
    }

    @Test
    fun generate() = baselineProfileRule.collect(
        packageName = TARGET_PACKAGE,
        includeInStartupProfile = true,
    ) {
        killProcess()
        startApp()
        device.waitForTag(Tags.CARD_SCREEN)
        device.waitForTag(Tags.CARD_QR_CODE)
    }
}
