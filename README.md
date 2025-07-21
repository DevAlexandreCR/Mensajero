# Mensajero

Mensajero is an Android application for requesting messenger and delivery services. The app allows users to request a driver (on a motorcycle or car), follow the service in real time, and chat with the assigned driver. It relies on Firebase and Google Maps services for location, authentication, notifications, and storage.

## Project Overview

This project contains a single Android module located in the `app/` directory. The main features include:

- User authentication and profile management.
- Display of nearby couriers on a Google Map.
- Booking requests for motorcycle or car deliveries.
- Real‑time updates of service status using Firebase Realtime Database.
- In‑app chat between customers and couriers via Firebase Cloud Functions.
- Push notifications handled by Firebase Cloud Messaging.

The project is written mostly in Java with some Kotlin data classes. It targets Android API level 29.

## Tech Stack

- **Android SDK** with Java and Kotlin
- **Firebase** (Authentication, Realtime Database, Cloud Messaging, Storage, Cloud Functions)
- **Google Maps & Places SDK**
- Third‑party libraries: Glide, Lottie, EventBus, Jsoup, and others defined in `app/build.gradle`.

## Installation

1. Clone this repository.
2. Ensure [Android Studio](https://developer.android.com/studio) is installed.
3. Copy your own `google-services.json` into `app/`.
4. Open the project in Android Studio or run Gradle via `./gradlew assembleDebug` to build the debug APK.

Building with the provided Gradle wrapper requires Java 8. Modern JDKs (e.g., JDK 21) may fail; use an older JDK if you encounter `NoClassDefFoundError` during Gradle initialization.

### Running locally

- Connect an Android device or start an emulator.
- From Android Studio, press **Run** or execute `./gradlew installDebug`.

### Production build

- Configure signing properties in `gradle.properties` or your `~/.gradle/` directory.
- Run `./gradlew assembleRelease` to generate `app-release.apk` (see `release/output.json`).

## API Usage

The application communicates with Firebase. Important database nodes and Cloud Functions include:

- `gerente/admin/pedido`, `gerente/admin/domicilio` – store service requests.
- `ChatServicio` – Firebase Cloud Function used to dispatch chat messages and push notifications.

Refer to `Constantes.java` for a list of database keys and message actions used throughout the app.

## Components

- **Mobile app** – located in the `app/` directory, containing all source code, resources, and Gradle configuration.
- **Backend** – implemented using Firebase Realtime Database and Cloud Functions; no separate server component is included in this repository.

## WhatsApp Integration Flow

The app allows users to share referral codes or service information using Android sharing intents. When selecting WhatsApp from the sharing menu, the text is pre‑filled with a download link and referral code (see `Fragment_Bonos.java`). This provides a simple flow for inviting contacts via WhatsApp.

## Contribution

Contributions are welcome. Please fork the repository and open a pull request with clear explanations of your changes.

## License

This project is released under the MIT License. See [LICENSE](LICENSE) for details.

