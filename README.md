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
    *   **Dependency Injection (Hilt):** Planned for managing dependencies.
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

## 🔮 Future Enhancements (Post-Refactoring)

*   **Complete MVVM Refactoring:** Fully implement ViewModels, Repository, and Hilt DI.
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
