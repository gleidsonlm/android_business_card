package com.gleidsonlm.businesscard.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Cold start benchmarks for a returning user (saved card), comparing no AOT compilation with the
 * baseline profile (which, in the `benchmarkRelease` build, also carries the startup profile
 * layout). Compare `timeToInitialDisplayMs` between the two tests.
 *
 * Run on a Pixel 6 / Android 14 device (needs a non-debuggable `benchmarkRelease` build):
 *
 * ```
 * ./gradlew :baselineprofile:connectedBenchmarkReleaseAndroidTest \
 *   -Pandroid.testInstrumentationRunnerArguments.class=com.gleidsonlm.businesscard.baselineprofile.StartupBenchmarks
 * ```
 *
 * On an emulator add `-Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.suppressErrors=EMULATOR`;
 * emulator numbers are only indicative.
 */
@RunWith(AndroidJUnit4::class)
class StartupBenchmarks {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    private val device: UiDevice =
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun seedSavedCard() {
        device.resetAppState()
        device.executeShellCommand("am start -W -n $TARGET_PACKAGE/.MainActivity")
        device.fillAndSaveCard()
        device.executeShellCommand("am force-stop $TARGET_PACKAGE")
    }

    @Test
    fun startupCompilationNone() = startup(CompilationMode.None())

    @Test
    fun startupCompilationBaselineProfile() =
        startup(CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require))

    private fun startup(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(StartupTimingMetric()),
        compilationMode = compilationMode,
        startupMode = StartupMode.COLD,
        iterations = 10,
        setupBlock = { pressHome() },
    ) {
        startActivityAndWait()
        device.waitForTag(Tags.CARD_SCREEN)
    }
}
