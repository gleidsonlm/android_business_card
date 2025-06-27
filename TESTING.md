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
