package com.gleidsonlm.businesscard.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        packageName = "com.gleidsonlm.businesscard"
    ) {
        // This block defines the user interactions that will be captured in the baseline profile.
        // For example, starting the main activity:
        pressHome()
        startActivityAndWait()

        // You can add more interactions here to capture other hot code paths.
        // For example:
        // device.wait(Until.hasObject(By.text("Some Text")), 5000)
    }
}
