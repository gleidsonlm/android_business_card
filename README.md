# My Digital Business Card Android App

**A modern, interactive digital business card application for Android, built with Kotlin and Jetpack Compose. This project serves as a practical demonstration of core Android development skills and modern UI practices.**

## Overview

This application allows users to create, store, and share their personal business card information seamlessly. Instead of carrying physical cards, users can input their details, generate a vCard, display a QR code for quick scanning, and even customize their avatar. This project was developed as a learning exercise to explore and implement key Android development building blocks, making it a great portfolio piece.

## ✨ Features

*   **Dynamic User Input:** Input and edit your name, title, contact details (phone, email, website), and company.
*   **Persistent Storage:** Your information is saved locally using SharedPreferences, so it's there when you relaunch the app.
*   **vCard Generation & Sharing:**
    *   Automatically generates a vCard from your information.
    *   Share your business card as a standard `.vcf` file using the dedicated share icon on the main card display. This allows others to easily import your contact details into their phone's contact list.
    *   The app also previously included a button to share vCard data as plain text, which has been streamlined to focus on `.vcf` file sharing.
*   **Integrated QR Code Display:** A QR code representing your vCard is displayed directly on the main card screen for quick scanning by others.
*   **Redesigned Main Card UI:**
    *   The main business card screen features a responsive layout that adapts to screen orientation:
        *   **Landscape:** Three-column layout (25% - 50% - 25%) for avatar, QR code, and details.
        *   **Portrait:** Single-column layout, stacking avatar, QR code, and details vertically.
    *   The QR code is prominently centered, occupying a significant portion of the screen width (50% in landscape, ~70% in portrait) and scaling dynamically while maintaining a 1:1 aspect ratio.
    *   User avatar and textual details (name, title, company) are placed in responsive sections, with their content and font sizes adjusting gracefully to different screen dimensions and orientations.
*   **Scrollable Input Form:** The user input screen for editing business card details is now scrollable, ensuring all fields are accessible even on smaller screens.
*   **Customizable Avatar:**
    *   Choose an image from your phone's gallery.
    *   Take a new photo using the camera.
    *   Fetch your avatar from Gravatar based on your email address.
*   **Modern UI:** Built entirely with Jetpack Compose, showcasing a declarative UI approach.
*   **Runtime Permissions:** Handles necessary permissions for gallery and camera access gracefully.

## 📸 Screenshots & Demo

![App before user information](http://gleidsonlm.com.br/app-bussiness-card/2025-06-13_08-25-06.png)
![User input screen](http://gleidsonlm.com.br/app-bussiness-card/2025-06-13_08-25-18.png)
![Camera interface](http://gleidsonlm.com.br/app-bussiness-card/2025-06-13_08-38-14.png)
![Sharing vCard intent](http://gleidsonlm.com.br/app-bussiness-card/2025-06-13_08-30-11.png)
![Main business card display](http://gleidsonlm.com.br/app-bussiness-card/2025-06-13_08-38-27.png)

## 🚀 Tech Stack & Learning Journey

This project was an opportunity to work with and learn several important Android technologies and concepts:

*   **Programming Language:** Kotlin (idiomatic, modern, and concise)
*   **UI Toolkit:** Jetpack Compose (declarative UI, Material 3 components, state management)
    *   `mutableStateOf` for managing UI state.
    *   `LaunchedEffect` for side effects tied to Composable lifecycle.
    *   Custom Composables for UI modularity.
    *   Responsive layout design (e.g., weighted sections).
*   **Data Handling & Persistence:**
    *   **SharedPreferences:** For simple key-value data storage. (Soon to be Jetpack DataStore as per ongoing refactoring)
    *   **Gson:** For serializing/deserializing data objects (UserData) to JSON.
*   **Architecture:**
    *   **MVVM (Model-View-ViewModel):** Currently being refactored towards this pattern.
    *   **Repository Pattern:** For abstracting data sources.
    *   **Dependency Injection (Hilt):** Integrated for managing dependencies, simplifying the creation and provision of objects throughout the application.
*   **Android Core Components & APIs:**
    *   **Activity Result APIs:** (`rememberLauncherForActivityResult` with `PickVisualMedia`, `TakePicture` for image picking, camera, permissions).
    *   **Intents:** For sharing data (`ACTION_SEND`) and launching external activities (image picker, camera, web links).
    *   **FileProvider:** For securely sharing content URIs with the camera app.
    *   **Runtime Permissions:** Requesting and handling permissions for camera (gallery permissions handled by `PickVisualMedia`).
*   **Image Loading:**
    *   **Coil (Compose Image Loader):** For asynchronously loading and displaying images from URIs/URLs (gallery, camera, Gravatar).
*   **Data Formatting & Libraries:**
    *   **ez-vcard:** For generating vCard (VCF) formatted data.
    *   **ZXing (via journeyapps:zxing-android-embedded):** For generating QR code bitmaps.
*   **Build System:** Gradle & Kotlin DSL (`build.gradle.kts`)
*   **Version Control:** Git (as implied by this development process)

This project demonstrates an ability to integrate various components, manage application state, handle user input, interact with device hardware (camera), manage permissions, and build a functional, user-friendly interface using modern Android development practices.

## 🛠️ Setup & Installation

1.  Clone the repository: `git clone <repository-url>`
2.  Open the project in Android Studio (latest stable version recommended).
3.  Let Android Studio sync Gradle dependencies.
4.  Build and run the app on an Android emulator or a physical device.

## 🧪 Testing

This project includes unit tests to ensure the reliability and correctness of its components. For detailed instructions on how to run these tests and configure your environment, please refer to the [TESTING.md](TESTING.md) file.

## Appdome Threat Event Handling

This feature is designed to detect and respond to specific security threats identified by the Appdome security platform integrated into the application. It provides immediate feedback to the user regarding potential security issues on their device.

The application is configured to handle the following Appdome Threat-Events:

### Existing Threat Events
*   `RootedDevice`: Detects if the device is rooted.
*   `GoogleEmulatorDetected`: Detects if the application is running on a Google emulator.
*   `DeveloperOptionsEnabled`: Detects if developer options are enabled on the device.
*   `DebuggerThreatDetected`: Detects if a debugger is attached to the application.
*   `UnknownSourcesEnabled`: Detects if "Unknown Sources" are enabled on the device.
*   `AppIsDebuggable`: Detects if the app is running in debuggable mode.
*   `AppIntegrityError`: Detects app integrity errors (tampering, repackaging, etc).
*   `EmulatorFound`: Detects if the app is running in an emulator environment.

### Comprehensive Appdome Threat Event Coverage

The application now implements comprehensive Appdome threat event handling with support for all major threat categories:

#### Event List Screen

*   **Threat Events List:** A dedicated screen that displays all received threat events as individual cards, showing event type, detection time, and summary message.
*   **Navigation:** Access the threat events list via the warning icon in the top-right corner of the main business card screen.
*   **Event Details:** Tap any event card to view the complete threat event details with all available metadata.
*   **Event Management:** Refresh the list or clear all events using the action buttons in the toolbar.
*   **Dynamic Updates:** New threat events automatically appear in the list as they are detected.

#### SSL/TLS Security Threats
*   `SslCertificateValidationFailed`: Detects SSL certificate validation failures indicating potential MITM attacks.
*   `SslNonSslConnection`: Detects non-SSL connections which may indicate security vulnerabilities.
*   `SslIncompatibleVersion`: Detects incompatible SSL/TLS versions which may indicate security vulnerabilities.
*   `SslIntegrityCheckFail`: Detects SSL integrity check failures which may indicate SSL pinning bypass attempts.

#### Network Security Threats
*   `NetworkProxyConfigured`: Detects network proxy configurations which may indicate man-in-the-middle attacks.

#### Bot and Automation Detection
*   `MobileBotDefenseCheck`: Advanced bot detection with configurable response actions.
*   `ClickBotDetected`: Detects mobile auto-clicker applications which may indicate fraudulent activity.
*   `ClickBotDetectedByPermissions`: Detects mobile auto-clicker applications through permission analysis.

#### Fraud Prevention
*   `KeyInjectionDetected`: Detects keystroke injection attacks which may indicate fraudulent input.
*   `ActiveADBDetected`: Detects active Android Debug Bridge connections which may indicate debugging attacks.
*   `BlockSecondSpace`: Detects secondary space applications which may indicate privacy bypass attempts.
*   `RunningInVirtualSpace`: Detects virtual devices and environments which may indicate fraud or testing.
*   `SeccompDetected`: Detects seccomp abuse which may indicate AI-assisted attacks.
*   `CorelliumFileFound`: Detects Corellium ARM-in-ARM virtual devices which may indicate testing or fraud.
*   `NotInstalledFromOfficialStore`: Detects applications not installed from official stores which may indicate sideloading.

#### Anti-Cheat and Modding Detection
*   `GameGuardianDetected`: Detects GameGuardian cheating applications which may indicate fraud or gaming exploits.
*   `SpeedHackDetected`: Detects speed hacking applications which may indicate cheating or fraud.
*   `CodeInjectionDetected`: Detects code and process injection attacks which may indicate malicious modifications.
*   `OatIntegrityBadCommandLine`: Detects APK modding tools through OAT integrity checks which may indicate tampering.
*   `RuntimeBundleValidationViolation`: Detects runtime bundle validation violations which may indicate dynamic modding.

### Flow of Event Handling

1.  **Detection:** When Appdome's security mechanisms detect one of the configured threat events, it broadcasts an Intent within the application.
2.  **Reception:** The `ThreatEventReceiver`, programmatically registered in `BusinessCardApplication.kt`, listens for these specific broadcast actions.
3.  **Data Extraction:** Upon catching a broadcast, the `ThreatEventReceiver` extracts detailed meta-data associated with the threat event from the Intent. This data is encapsulated in a `ThreatEventData` object with a unique ID and timestamp.
4.  **Storage:** The threat event is automatically saved to the local repository for display in the events list.
5.  **List Display:** Events appear in the threat events list screen (accessed via the warning icon on the main screen) as individual cards.
6.  **User Navigation:** Users must tap on specific threat event cards in the list to view the detailed payload and metadata.
7.  **Immediate Display Exception:** Only critical security actions (such as `SECURITY_MEASURES` or `APP_PROTECTION` from bot defense checks) may show immediate notifications that require urgent user attention.

This new navigation flow ensures users have control over when they view threat details while maintaining immediate alerts for the most critical security events.

### Key Components

*   **`ThreatEventReceiver.kt`**: A `BroadcastReceiver` that listens for Appdome threat event broadcasts. It processes these events, extracts data, saves them to the repository for list display. Critical security events may trigger immediate notifications, but most events are displayed only when users tap them in the list.
*   **`ThreatEventRepository.kt`** & **`ThreatEventRepositoryImpl.kt`**: Repository pattern implementation for storing and managing threat events using SharedPreferences with Gson serialization.
*   **`ThreatEventListViewModel.kt`**: ViewModel that manages the state and business logic for the threat events list screen, following MVVM architecture patterns.
*   **`ThreatEventListScreen.kt`**: Jetpack Compose screen that displays all threat events as a list of interactive cards with refresh and clear functionality.
*   **`ThreatEventCard.kt`**: Reusable Composable component for displaying individual threat event summaries in the list.
*   **`ThreatEventActivity.kt`**: An `Activity` responsible for displaying the detailed information of a detected threat event to the user.
*   **`ThreatEventScreen.kt`**: Contains the Jetpack Compose UI (`ThreatEventScreenContent`) that renders the details from the `ThreatEventData` object.
*   **`ThreatEventData.kt`**: A Kotlin data class (`Parcelable`) that models all the meta-data fields associated with a threat event, enhanced with unique IDs and timestamps.
*   **`BusinessCardApplication.kt`**: The custom `Application` class where the `ThreatEventReceiver` is instantiated and programmatically registered on application startup. It registers intent filters for all supported threat events.
*   **Security Handlers**: Individual handler classes for each threat event type, providing specialized logging and processing for different security threats.
*   **`AndroidManifest.xml`**: Contains necessary application permissions (like `REQUEST_INSTALL_PACKAGES`). The `ThreatEventReceiver` itself is no longer declared here as it's registered programmatically.

### Threat Event Handler Architecture

Each threat event is handled by a dedicated handler class that follows these principles:
- **Single Responsibility**: Each handler focuses on one specific threat type
- **Dependency Injection**: All handlers are injected via Hilt for testability and modularity
- **Consistent Logging**: All handlers use sanitized logging to avoid exposing sensitive data
- **Extensibility**: New threat handlers can be easily added following the established pattern

## Permissions Required

The application requires the `android.permission.REQUEST_INSTALL_PACKAGES` permission for the following reason:

*   **Threat Event Functionality**: This permission is necessary for the proper functioning of the threat-event screen. Certain threat events may require the ability to initiate package installations or updates as a response or mitigation step. Without this permission, the threat-event screen may not load correctly or some of its features related to package management might be disabled. Users will be prompted to grant this permission if it's not already available.

## 🔮 Future Enhancements (Post-Refactoring)

*   **Complete MVVM Refactoring:** Fully implement ViewModels and Repository. Hilt DI is already integrated.
*   **Jetpack DataStore:** Migrate from SharedPreferences to Jetpack DataStore for data persistence.
*   **More Robust Image Persistence:** Copy selected gallery/camera images to app-specific storage.
*   **Material You Theming:** Implement dynamic theming.
*   **Improved Error Handling & User Feedback:** Comprehensive error states and messages.
*   **Tablet Layout:** Optimized layout for tablets.
*   **Direct File Saving for vCard:** Option to save .vcf file.
*   **Widget:** Home screen widget.
*   **Unit & UI Tests:** Comprehensive test suite.

---

*This project, including this README, is being developed with the significant assistance of Jules, an AI software engineering agent from Google.*

## Appdome Threat-Events Integration

This application is integrated with Appdome Threat-Events to provide comprehensive runtime threat detection and response. The implementation covers all major threat categories including SSL/TLS security, network security, bot detection, fraud prevention, and anti-cheat mechanisms.

**Total Coverage**: 29+ threat event types across all major security categories.

Key threat events include SSL certificate validation failures, network proxy detection, mobile bot defense, keystroke injection detection, virtual environment detection, and gaming/modding prevention.

When any threat is detected, the application saves it to the events list. Users can view all threat events by tapping the warning icon on the main screen, and then tap individual event cards to view detailed threat information and comprehensive metadata about the specific security concern.
