# Luteh E-commerce KMM

A personal "pet" project designed to channel creativity and explore the capabilities of **Kotlin Multiplatform (KMP)**. This project demonstrates a modern E-commerce application targeting **Android** and **iOS** with a high degree of code sharing, including the UI.

## 🚀 Project Overview

This application serves as a playground for implementing best practices in KMP development, utilizing a modern tech stack to build a robust and scalable mobile application. It features a shared codebase for business logic, data handling, and UI components, ensuring consistency across platforms while maintaining native performance.

## 🛠 Tech Stack

The project leverages a modern suite of libraries and tools:

### Core & Architecture
*   **[Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html):** The foundation for sharing code between Android and iOS.
*   **[Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/):** Declarative UI framework shared across platforms.
*   **Architecture:** MVI (Model-View-Intent) with Clean Architecture principles.
*   **[Arrow Kt](https://arrow-kt.io/):** Functional programming library for Kotlin (Core & FX Coroutines) to handle errors and side effects gracefully.

### Data & Networking
*   **[Apollo GraphQL](https://www.apollographql.com/docs/kotlin/):** Strongly-typed GraphQL client for networking.
*   **[Ktor](https://ktor.io/):** Asynchronous HTTP client for multiplatform networking.
*   **[Room](https://developer.android.com/kotlin/multiplatform/room):** Local database persistence (SQLite).
*   **[Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization):** JSON serialization and deserialization.

### Dependency Injection
*   **[Koin](https://insert-koin.io/):** Pragmatic lightweight dependency injection framework for Kotlin.

### Navigation
*   **[Navigation Compose](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation.html):** Type-safe navigation for Compose Multiplatform.

### Testing & Quality
*   **[Mokkery](https://mokkery.dev/):** Mocking library for Kotlin Multiplatform.
*   **[Turbine](https://github.com/cashapp/turbine):** A small testing library for Kotlin Flow.
*   **[Robolectric](https://robolectric.org/):** Unit testing framework for Android.
*   **[Kover](https://github.com/Kotlin/kotlinx-kover):** Code coverage tool for Kotlin.

### CI/CD
*   **GitHub Actions:** Automated workflows for code quality checks (Lint, Kover) and building the application.

## ✨ Features

The application includes a comprehensive set of E-commerce features:

*   **🔐 Authentication:**
    *   **Login:** Secure user authentication.
    *   **Register:** New user registration.
*   **🏠 Home Dashboard:**
    *   Browse product categories.
    *   View featured and popular products.
*   **🛍️ Product Browsing:**
    *   **Product List:** View products by category or search.
    *   **Product Detail:** Detailed view of product information, price, and description.
*   **🛒 Shopping Cart:**
    *   Add/Remove items.
    *   Update quantities.
    *   View total price.
*   **💳 Checkout:**
    *   Secure checkout process.
    *   Order summary review.
*   **🧾 Transaction History:**
    *   View transaction details and status (Success/Failure).

## 📸 Screenshots

| **Home (Android)** | **Home (iOS)** |
|:---:|:---:|
| <img src="docs/images/android_home.png" width="250" /> | <img src="docs/images/ios_home.png" width="250" /> |

| **Product Detail** | **Cart** | **Checkout** |
|:---:|:---:|:---:|
| <img src="docs/images/product_detail.png" width="250" /> | <img src="docs/images/cart.png" width="250" /> | <img src="docs/images/checkout.png" width="250" /> |

## 📂 Project Structure

*   `/composeApp`: Contains the shared code (UI, Domain, Data) and platform-specific implementations.
    *   `commonMain`: Code shared across all targets.
    *   `androidMain`: Android-specific implementations.
    *   `iosMain`: iOS-specific implementations.
*   `/iosApp`: The iOS application entry point (Xcode project).

## 🚧 Getting Started

1.  **Prerequisites:**
    *   Android Studio (latest version recommended).
    *   Xcode (for iOS development).
    *   JDK 17+.

2.  **Clone the repository:**
    ```bash
    git clone https://github.com/luthfan-maftuh/luteh-ecommerce-kmm.git
    ```

3.  **Open in Android Studio:**
    *   Open the project root directory.
    *   Sync Gradle.

4.  **Run the App:**
    *   **Android:** Select the `composeApp` configuration and run on an emulator or device.
    *   **iOS:** Open `iosApp/iosApp.xcodeproj` in Xcode or run via Android Studio using the KMP plugin.

---
*Relax, this is just the beginning.*

