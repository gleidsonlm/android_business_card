# My Digital Business Card Android App

**A modern, interactive digital business card application for Android, built with Kotlin and Jetpack Compose. This project serves as a practical demonstration of core Android development skills and modern UI practices.**

## Overview

This application allows users to create, store, and share their personal business card information seamlessly. Instead of carrying physical cards, users can input their details, generate a vCard, display a QR code for quick scanning, and even customize their avatar. This project was developed as a learning exercise to explore and implement key Android development building blocks, making it a great portfolio piece.

## ✨ Features

*   **Dynamic User Input:** Input and edit your name, title, contact details (phone, email, website), and company.
*   **Persistent Storage:** Your information is saved locally using SharedPreferences, so it's there when you relaunch the app.
*   **vCard Generation & Sharing:** Automatically generates a vCard from your information and allows easy sharing via Android's share functionality.
*   **Integrated QR Code Display:** A QR code representing your vCard is displayed directly on the main card screen for quick scanning by others.
*   **Customizable Avatar:**
    *   Choose an image from your phone's gallery.
    *   Take a new photo using the camera.
    *   Fetch your avatar from Gravatar based on your email address.
*   **Modern UI:** Built entirely with Jetpack Compose, showcasing a declarative UI approach.
*   **Runtime Permissions:** Handles necessary permissions for gallery and camera access gracefully.

## 📸 Screenshots & Demo

*(This section is a placeholder. It's highly recommended to add screenshots of the main screens and perhaps a short GIF demonstrating the app flow.)*

**Key Screens to Showcase:**
*   Main business card display (with integrated QR code, avatar, and info).
*   User input screen (showing fields and avatar selection options).
*   Image picker (gallery).
*   Camera interface (if possible to demo).
*   Sharing vCard intent.

## 🚀 Tech Stack & Learning Journey

This project was an opportunity to work with and learn several important Android technologies and concepts:

*   **Programming Language:** Kotlin (idiomatic, modern, and concise)
*   **UI Toolkit:** Jetpack Compose (declarative UI, Material 3 components, state management)
    *   `mutableStateOf` for managing UI state.
    *   `LaunchedEffect` for side effects tied to Composable lifecycle.
    *   Custom Composables for UI modularity.
    *   Responsive layout design (e.g., weighted sections).
*   **Data Handling & Persistence:**
    *   **SharedPreferences:** For simple key-value data storage.
    *   **Gson:** For serializing/deserializing data objects (UserData) to JSON.
*   **Android Core Components & APIs:**
    *   **Activity Result APIs:** (`rememberLauncherForActivityResult` for image picking, camera, permissions).
    *   **Intents:** For sharing data (`ACTION_SEND`) and launching external activities (image picker, camera, web links).
    *   **FileProvider:** For securely sharing content URIs with the camera app.
    *   **Runtime Permissions:** Requesting and handling permissions for gallery and camera.
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

## 🔮 Future Enhancements

*   **More Robust Image Persistence:** Copy selected gallery/camera images to app-specific storage for more reliable long-term access.
*   **Material You Theming:** Implement dynamic theming based on user's wallpaper (Android 12+).
*   **Improved Error Handling:** More user-friendly error messages and states.
*   **Tablet Layout:** Create an optimized layout for tablet devices.
*   **Direct File Saving for vCard:** Option to save the .vcf file directly to device storage.
*   **Widget:** A home screen widget for the business card.
*   **Unit & UI Tests:** Adding a suite of tests to ensure code quality and reliability.

---

*This project, including this README, was developed with the significant assistance of Jules, an AI software engineering agent from Google.*
