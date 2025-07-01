# AGENTS.md - Guidelines for AI Agents (Google Joules)

This document provides guidelines, architectural insights, and project direction for AI agents like Google Joules when working on the My Digital Business Card Android App.

## Best Practices for Google Joules

When assisting with this project, please adhere to the following best practices:

1.  **Targeted Modifications:**
    *   Prefer `replace_with_git_merge_diff` for focused code changes over `overwrite_file_with_block` whenever possible. This helps in reviewing changes and maintaining context.
    *   Ensure search blocks for diffs are copied verbatim to avoid patch application failures.

2.  **Planning and Execution:**
    *   Always use `set_plan()` to outline your approach before starting significant changes. Break tasks into the smallest actionable steps.
    *   Use `plan_step_complete()` after each step to track progress.
    *   If a plan needs to be revised after approval, use `set_plan()` again and notify the user with `message_user()`.

3.  **Codebase Interaction:**
    *   Always use `read_files()` to get the latest version of a file before attempting to modify it. This prevents working with stale information.
    *   Utilize `ls()` and `grep()` for exploration and to locate relevant code sections or files.
    *   If you encounter compilation errors or test failures after a change, attempt to diagnose and fix them. Clearly communicate the error and your proposed solution.

4.  **Understanding `AGENTS.md`:**
    *   Always check for `AGENTS.md` files in the repository, especially in the root or relevant module directories.
    *   Adhere to the instructions and guidelines provided herein unless explicitly overridden by the user for a specific task.
    *   If programmatic checks are mentioned in `AGENTS.md` (none currently, but for future reference), ensure they pass after your changes.

5.  **Communication:**
    *   Use `message_user()` to provide updates, explain complex changes, or clarify actions.
    *   Use `request_user_input()` when you need clarification, are stuck after multiple attempts, or need to make a decision significantly altering the scope.

## Optimized Settings & Interaction with Google Joules

While internal Joules settings are not user-configurable, you can optimize your interaction for better results:

1.  **Clear and Specific Issues:**
    *   Provide detailed GitHub issue descriptions or requests. Include clear objectives, acceptance criteria, and any relevant context.
    *   Link to specific files or code sections if known.

2.  **Incremental Requests:**
    *   For complex features, break them down into smaller, manageable tasks. This allows for iterative development and easier review.

3.  **Plan Review:**
    *   Carefully review the plan proposed by Joules. Ensure it aligns with your expectations and the project goals before approving.

4.  **Prompt Feedback:**
    *   Respond quickly to questions from Joules (`request_user_input`) to avoid delays.

## Software Architecture

The application generally follows the **MVVM (Model-View-ViewModel)** architectural pattern.

### Key Components:

*   **`MainActivity.kt`:** The single entry point Activity. Responsible for setting up the Jetpack Compose UI content, navigation between the main card display and the user input screen, and initializing ViewModels.
*   **ViewModels:**
    *   **`BusinessCardViewModel.kt`:** Manages the state and logic for displaying the business card (user data, QR code). It fetches data from the `UserRepository` and prepares it for the UI.
    *   **`UserInputViewModel.kt`:** Manages the state and logic for the user input screen. It handles form field changes, avatar selection (gallery, camera, Gravatar), and saving data via the `UserRepository`.
*   **UI (Jetpack Compose):**
    *   **`BusinessCardFace.kt`:** A composable function responsible for displaying the main business card UI with the new three-column layout, including the avatar, QR code, and textual user information. It's designed to be responsive.
    *   **`UserInputScreen.kt`:** A composable function that provides the form for users to input and edit their business card details.
    *   **`components/`:** Contains reusable UI components like `BusinessCardLink.kt`.
    *   **`theme/`:** Standard Jetpack Compose theme files (`Color.kt`, `Theme.kt`, `Type.kt`).
*   **Data Layer:**
    *   **`UserRepository.kt` (interface) & `UserRepositoryImpl.kt` (implementation):** Implements the Repository Pattern. Abstracts the data source (currently SharedPreferences with Gson for serialization) for user data. Responsible for loading and saving `UserData`.
    *   **`UserData.kt`:** A data class representing the user's business card information.
*   **Utility:**
    *   **`VCardHelper.kt`:** Utility object for generating vCard strings and QR code bitmaps (using `ez-vcard` and `zxing`).
    *   **`FileProvider`:** Configured to allow sharing URIs for camera photos.

### Data Flow:

1.  **Initial Load:**
    *   `MainActivity` initializes `BusinessCardViewModel`.
    *   `BusinessCardViewModel` calls `loadInitialData()` which uses `UserRepository` to load saved `UserData`.
    *   Loaded `UserData` (if any) is exposed as `StateFlow` to the UI.
    *   `BusinessCardViewModel` also generates a QR code from this `UserData`.
    *   `BusinessCardFace` observes these states and displays the card.
2.  **User Input & Save:**
    *   User navigates to `UserInputScreen`.
    *   `UserInputViewModel` collects user input into its state variables.
    *   On save, `UserInputViewModel` calls `saveUserData()` which uses `UserRepository` to persist the data.
    *   `MainActivity` is notified, triggers `BusinessCardViewModel` to reload data, and switches back to the main card display.
3.  **Avatar Selection:**
    *   `UserInputScreen` interacts with `UserInputViewModel` for avatar changes.
    *   ViewModel handles URI results from gallery/camera or fetches Gravatar URL.
    *   The selected `avatarUri` is stored in `UserData`.

## Software Architect's Vision & Direction

As the (acting) software architect for this application, facilitated by Google Joules, my vision emphasizes creating a robust, maintainable, and modern Android application.

1.  **Modularity & Reusability (Compose First):**
    *   **Direction:** Continue to break down UI into small, focused, and reusable Composable functions. Avoid monolithic Composables.
    *   **Rationale:** Enhances readability, testability, and maintainability. Makes UI changes less risky.

2.  **Testability:**
    *   **Direction:** Prioritize adding tests. Implement unit tests for all ViewModel logic. Explore UI tests for critical Composable user flows using `compose-test-rule`.
    *   **Rationale:** Ensures code quality, prevents regressions, and provides confidence when refactoring or adding new features. This is a current major gap.

3.  **Clear State Management:**
    *   **Direction:** Strictly adhere to unidirectional data flow. State should primarily be owned and managed by ViewModels. Composables should be as stateless as possible, receiving state and lambdas to communicate events upwards.
    *   **Rationale:** Simplifies debugging, makes UI behavior predictable, and aligns with Jetpack Compose best practices.

4.  **Leverage Dependency Injection (Hilt):**
    *   **Direction:** Hilt has been integrated, replacing the manual ViewModel factory. Continue to use Hilt for managing all dependencies. Ensure new components are Hilt-compatible (e.g., ViewModels annotated with `@HiltViewModel`, dependencies provided via modules or constructor injection).
    *   **Rationale:** Reduces boilerplate, improves scalability, and makes testing easier by allowing for easy swapping of dependencies (e.g., mock repositories in tests).

5.  **UI/UX Consistency & Responsiveness:**
    *   **Direction:** Maintain and enhance UI/UX consistency. Ensure the app adapts gracefully to various screen sizes and orientations. The recent UI redesign is a good step in this direction.
    *   **Rationale:** Provides a professional and user-friendly experience.

6.  **Performance:**
    *   **Direction:** While not currently an issue, be mindful of Composable performance. Use tools like Layout Inspector and Android Studio Profiler to identify and address any jank or performance bottlenecks if they arise, especially with lists or complex custom drawing.
    *   **Rationale:** Ensures a smooth user experience.

7.  **Robust Error Handling & User Feedback:**
    *   **Direction:** Implement more comprehensive error handling (e.g., for network failures when fetching Gravatar, file I/O issues) and provide clear, non-intrusive feedback to the user.
    *   **Rationale:** Improves app stability and user trust.

8.  **Data Persistence Strategy:**
    *   **Direction:** Proceed with the planned migration from SharedPreferences/Gson to Jetpack DataStore (Proto DataStore preferred for typed data like `UserData`).
    *   **Rationale:** DataStore is the modern Android solution for data persistence, offering asynchronous operations and better error handling.

By following these architectural principles, we can ensure the "My Digital Business Card" app evolves into a high-quality, scalable, and maintainable piece of software.
