# Running Unit Tests

This document provides instructions on how to run the unit tests for this Android application.

## Test Location

The unit tests are primarily located in the `app/src/test/java/` directory. For example, tests for the `BusinessCardViewModel` can be found in `app/src/test/java/com/gleidsonlm/businesscard/ui/viewmodel/BusinessCardViewModelTest.kt`.

## Running Tests

To execute the unit tests, you can use the following Gradle command from the root directory of the project:

```bash
./gradlew testDebugUnitTest
```

This command will run all unit tests in the `debug` build variant.

## Important: SDK Location

For the tests to run successfully, especially from the command line or in a CI environment, your Android SDK location must be correctly configured.

You can do this in one of two ways:

1.  **`local.properties` file:**
    Create or ensure you have a `local.properties` file in the root of your project with the `sdk.dir` property pointing to your Android SDK installation. For example:
    ```properties
    sdk.dir=/path/to/your/android/sdk
    ```
    (Replace `/path/to/your/android/sdk` with the actual path to your SDK).
    **Note:** This file should typically not be committed to version control.

2.  **`ANDROID_HOME` environment variable:**
    Alternatively, you can set the `ANDROID_HOME` environment variable to point to your SDK location.

If the SDK location is not found, you might encounter an error like: `SDK location not found. Define a valid SDK location with an ANDROID_HOME environment variable or by setting the sdk.dir path in your project's local properties file...`

## Included Tests

This project includes unit tests for various components. Notably, `BusinessCardViewModel` has a comprehensive test suite covering its logic for data loading and QR code generation.

## Baseline Profile, Startup Profile and Benchmarks

The `:baselineprofile` module drives the app with UiAutomator to collect profiles and measure cold start. It selects UI elements by the test tags defined in `com.gleidsonlm.businesscard.ui.TestTags` (exposed as resource ids with `testTagsAsResourceId`); `Tags` in `BusinessCardJourneys.kt` must stay in sync with them.

### Requirements

- A Pixel 6 / Android 14 (API 34) device. The tested setup is an Android Studio AVD named `Pixel_6` using a **`google_apis`** image (rootable). `google_apis_playstore` images are not rootable and cannot collect profiles. A rooted physical device also works.
- The app is built as a non-debuggable release-like variant automatically by the tasks below.

### Regenerate the profiles

Run on a connected/running device or emulator:

```bash
./gradlew :app:generateBaselineProfile -Pbaselineprofile.useConnectedDevices=true
```

Without the property, a Gradle managed Pixel 6 / API 34 emulator (`pixel6Api34`) is used instead. This runs two generators:

- `BaselineProfileGenerator`: first run form, save, business card with QR code, threat events list, edit and save. The threat event detail screen is only captured if the list contains an event (`ThreatEventActivity` is not exported).
- `StartupProfileGenerator`: only the cold start of a returning user up to the rendered card. The saved card is seeded in `@Before`, outside the profile block.

Output (commit it): `app/src/release/generated/baselineProfiles/baseline-prof.txt` and `startup-prof.txt`.

To check that a release bundle contains the profile:

```bash
./gradlew :app:bundleRelease
unzip -l app/build/outputs/bundle/release/app-release.aab | grep baseline.prof
```

### Startup benchmark

Compares cold start (`timeToInitialDisplayMs`) with no compilation vs. the baseline profile:

```bash
./gradlew :baselineprofile:connectedBenchmarkReleaseAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.gleidsonlm.businesscard.baselineprofile.StartupBenchmarks
```

On an emulator add `-Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.suppressErrors=EMULATOR`. Emulator numbers are only indicative; record real numbers from a physical Pixel 6.

### Troubleshooting

- Taps on the top-right card buttons are ignored: keep `statusBarsPadding()` on that row in `MainActivity` (edge-to-edge puts it under the status bar otherwise).
- `:baselineprofile` has no Kotlin compile task: `kotlin.android` must be applied while `android.builtInKotlin=false`.
- Known issue: unit tests in `GeoComplianceThreatHandlersTest`, `LocationPermissionTest` and `ReadPhoneStatePermissionTest` fail independently of this work.
