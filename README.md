# 🎬 Cinepople — Android Cinema Booking App

Cinepople is a native Android application that lets users browse movies, book seats at cinemas, manage tickets, and chat with an AI assistant. It is built with Java, follows the **MVVM + Repository** architecture pattern, and integrates Firebase for authentication and cloud data storage alongside a custom REST backend.

---

## Table of Contents

1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Architecture Overview](#architecture-overview)
4. [Directory Structure](#directory-structure)
5. [Data Flow](#data-flow)
6. [Key Components](#key-components)
   - [Activities](#activities)
   - [Fragments](#fragments)
   - [ViewModels](#viewmodels)
   - [Repositories](#repositories)
   - [Domain Models](#domain-models)
7. [API Reference](#api-reference)
8. [Authentication](#authentication)
9. [CI/CD](#cicd)
10. [Building & Running](#building--running)
11. [Dependencies](#dependencies)

---

## Features

| Feature | Description |
|---------|-------------|
| 🎞️ Movie Browsing | Home feed with Top movies, Now Playing, and Coming Soon sections |
| 🔍 Search | Search movies by title with live filtering |
| 🎟️ Booking Flow | Select region → cinema → date → showtime → seats → review → payment |
| 🪑 Seat Map | Interactive seat grid with available / held / sold / couple states |
| 🧾 My Tickets | View paid and unpaid/pending tickets |
| 💬 AI Chatbot | In-app chat with an AI assistant |
| 👤 Profile | View and edit personal information |
| 🔐 Auth | Email/password, Google Sign-In, Facebook Login |

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java (Android SDK 24–36) |
| UI | Android Views + Material Design 3 |
| Architecture | MVVM + Repository + Clean Architecture |
| Async / Reactive | LiveData, Retrofit Callbacks |
| Networking | Retrofit 2 · OkHttp 3 · GSON |
| Authentication | Firebase Auth · Google Sign-In · Facebook Login · JWT |
| Cloud Database | Firebase Firestore · Firebase Realtime Database |
| Image Loading | Glide 4 |
| Build | Gradle (Kotlin DSL) |
| CI | GitHub Actions |

---

## Architecture Overview

```
┌──────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                    │
│  Activities  ·  Fragments  ·  Adapters                   │
│  (observe LiveData, dispatch user events to ViewModel)   │
└───────────────────────┬──────────────────────────────────┘
                        │ observe / call
                        ▼
┌──────────────────────────────────────────────────────────┐
│                    VIEWMODEL LAYER                       │
│  Holds MutableLiveData<T> state                         │
│  Delegates data fetching to Repositories                │
│  Survives configuration changes                         │
└───────────────────────┬──────────────────────────────────┘
                        │ call
                        ▼
┌──────────────────────────────────────────────────────────┐
│                   REPOSITORY LAYER                       │
│  Single source of truth for each data domain            │
│  Abstracts remote API calls                             │
│  Posts results back via MutableLiveData callbacks       │
└───────────────────────┬──────────────────────────────────┘
                        │ enqueue (async)
                        ▼
┌──────────────────────────────────────────────────────────┐
│                    REMOTE DATA LAYER                     │
│  ApiService (Retrofit interface)                        │
│  ApiClient (OkHttp + AuthInterceptor + logging)         │
│  Request / Response model classes                       │
└───────────────────────┬──────────────────────────────────┘
                        │ HTTP/JSON over the internet
                        ▼
┌──────────────────────────────────────────────────────────┐
│                   BACKEND SERVER                         │
│  Node.js / Express REST API                             │
│  Base URL: configured in ApiClient.java                 │
│  Stores data in Firebase + relational / document DB     │
└──────────────────────────────────────────────────────────┘
```

---

## Directory Structure

```
NT118/
├── .github/
│   └── workflows/
│       └── android.yml          # GitHub Actions CI pipeline
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   └── java/course/examples/cinepople/
│   │   │       ├── activity/    # 11 Activity classes
│   │   │       ├── adapter/     # 12 RecyclerView adapters
│   │   │       ├── app/         # MyApplication (Firebase/Facebook init)
│   │   │       ├── data/
│   │   │       │   └── remote/
│   │   │       │       ├── api/         # ApiService, ApiClient, AuthInterceptor
│   │   │       │       ├── model/       # Request / Response DTOs
│   │   │       │       └── repository/  # 7 Repository classes
│   │   │       ├── domain/      # 9 business entity (domain model) classes
│   │   │       ├── fragment/    # 12 Fragment classes
│   │   │       ├── model/       # ChatMessage model
│   │   │       ├── utility/     # SessionManager, DepthPageTransformer
│   │   │       └── viewmodel/   # 11 ViewModel classes
│   │   │   └── res/             # layouts, drawables, values, menus
│   │   ├── test/                # JUnit unit tests
│   │   └── androidTest/         # Espresso instrumented tests
│   ├── build.gradle.kts
│   ├── google-services.json     # Firebase project config
│   └── proguard-rules.pro
├── build.gradle.kts             # Project-level build config
├── settings.gradle.kts
├── gradle.properties
└── gradlew / gradlew.bat
```

---

## Data Flow

### Example: Complete Movie Booking Flow

```
User taps "Book Now" on a Movie
        │
        ▼
SelectSessionActivity starts
        │
        ├─► RegionViewModel.fetchRegions()
        │       └─► RegionRepository ──► GET /api/regions
        │                                      └─► LiveData<List<Region>> updated
        │                                             └─► RegionSelectFragment shows list
        │
        ├─► [User selects Region]
        │       └─► CinemaViewModel.getCinemasByRegion(regionId)
        │               └─► CinemaRepository ──► GET /api/cinemas?region=…
        │                                              └─► LiveData updated → UI refreshes
        │
        ├─► [User selects Cinema + Date]
        │       └─► ShowtimeViewModel.getShowtimes(movieId, cinemaId, date)
        │               └─► ShowtimeRepository ──► GET /api/showtimes?…
        │
        └─► [User selects Showtime]
                │
                ▼
        SelectSeatsActivity starts
                │
                ├─► SelectSeatsViewModel.fetchShowtime(showtimeId)
                │       └─► GET /api/showtimes/{id}  (seat map included)
                │               └─► SeatsAdapter renders seat grid
                │
                └─► [User picks seats → taps Confirm]
                        │
                        ▼
                ReviewSummaryActivity starts
                        │
                        └─► [User taps Pay]
                                │
                                ▼
                        PaymentActivity
                                │
                                └─► BookingViewModel.createBooking(showtimeId, seats)
                                        └─► BookingRepository ──► POST /api/booking
                                                                       (Auth token in header)
                                                └─► LiveData<Booking> → success screen
```

### Reactive update cycle (generic)

```
UI Action
    │  viewModel.doSomething(args)
    ▼
ViewModel
    │  repository.fetchData(successLiveData, errorLiveData)
    ▼
Repository
    │  apiService.endpoint(args).enqueue(callback)
    ▼
Retrofit/OkHttp ──► REST API ──► JSON response
    │
    ▼  callback.onResponse / onFailure
Repository posts to successLiveData / errorLiveData
    │
    ▼  LiveData<T>.observe(lifecycleOwner)
UI updates automatically
```

---

## Key Components

### Activities

| Activity | Purpose |
|----------|---------|
| `MainActivity` | Root screen; hosts bottom navigation (Home, Search, Tickets, Profile, Chat) |
| `LoginActivity` | Email/password login + social login entry point |
| `SignUpActivity` | Multi-step sign-up flow (fragments for account, profile, OTP, success) |
| `ForgotActivity` | Password recovery via email OTP |
| `MovieDetailsActivity` | Movie details, trailer link, ratings, cast, similar movies |
| `SelectSessionActivity` | Region → Cinema → Date → Showtime selector |
| `SelectSeatsActivity` | Interactive seat-map for a specific showtime |
| `ReviewSummaryActivity` | Order summary before payment confirmation |
| `PaymentActivity` | Payment step; triggers booking creation API call |
| `TicketPaidActivity` | Displays confirmed/paid tickets |
| `TicketUnpaidActivity` | Displays pending/unpaid tickets |

### Fragments

| Fragment | Activity / Parent | Purpose |
|----------|-------------------|---------|
| `HomeFragment` | MainActivity | Top movies carousel, now playing grid, coming soon grid |
| `SearchFragment` | MainActivity | Live movie search |
| `TicketsFragment` | MainActivity | Tabs for paid and unpaid tickets |
| `ProfileFragment` | MainActivity | Profile picture, name, edit action |
| `PersonalInfoFragment` | MainActivity | Edit personal details |
| `ChatFragment` | MainActivity | AI chatbot conversation |
| `SignUpCreateAccountFragment` | SignUpActivity | Email + password fields |
| `SignUpCreateProfileFragment` | SignUpActivity | Name, phone, avatar |
| `SignUpOTPFragment` | SignUpActivity | OTP code entry |
| `SignUpSuccessLoadingFragment` | SignUpActivity | Animated success screen |
| `ForgotMailFragment` | ForgotActivity | Enter recovery email |
| `ForgotOTPFragment` | ForgotActivity | OTP verification |
| `ForgotResetFragment` | ForgotActivity | New password entry |
| `RegionSelectFragment` | SelectSessionActivity | Pick cinema region/city |
| `CinemaSelectFragment` | SelectSessionActivity | Pick specific cinema |

### ViewModels

| ViewModel | Owned LiveData | Role |
|-----------|----------------|------|
| `HomeViewModel` | movies, topMovies, nowPlaying, comingSoon | Fetches and categorises movie lists |
| `MovieDetailsViewModel` | movie, actors, similarMovies | Loads a single movie + related data |
| `SearchViewModel` | searchResults | Filters movies by query |
| `RegionViewModel` | regions | Fetches list of cinema regions |
| `CinemaViewModel` | cinemas | Fetches cinemas by region |
| `ShowtimeViewModel` | showtimes | Fetches showtimes by movie/cinema/date |
| `SelectSeatsViewModel` | showtime, price | Loads seat map; calculates total price |
| `ReviewSummaryViewModel` | summary | Aggregates booking summary data |
| `BookingViewModel` | bookingResult, myBookings | Creates booking; fetches user's bookings |
| `ChatViewModel` | messages | Manages chat message list; calls chatbot API |
| `LoginViewModel` | loginResult | Handles login state (partially implemented) |

### Repositories

| Repository | API Calls | LiveData Posted |
|------------|-----------|-----------------|
| `MovieRepository` | `GET /api/movies`, `GET /api/movies/{id}` | movies, movieDetail |
| `ShowtimeRepository` | `GET /api/showtimes`, `GET /api/showtimes/{id}` | showtimes, showtime |
| `RegionRepository` | `GET /api/regions` | regions |
| `CinemaRepository` | `GET /api/cinemas` | cinemas |
| `BookingRepository` | `POST /api/booking`, `GET /api/booking` | bookingResult, myBookings |
| `UserRepository` | user profile endpoints | userProfile |
| `ChatRepository` | `POST /api/chatbot` | chatResponse |

### Domain Models

| Model | Key Fields |
|-------|-----------|
| `Movie` | id, title, posterUrl, bannerImageUrl, trailerUrl, director, genres, releaseDate, imdbRating, ageRating, status (`now_showing` / `coming_soon`), isTopMovie |
| `Showtime` | id, movieId, cinemaId, roomName, startTime, endTime, totalSeats, seatMap (`Map<String, Seat>`) |
| `Seat` | code (e.g. `A1`), row, col, type (`standard` / `vip` / `couple`), price, status (`available` / `held` / `sold` / `locked`), userId |
| `Booking` | id, showtimeId, movieTitle, cinemaName, seats (`List<String>`), totalPrice, status (`pending` / `confirmed` / `cancelled`) |
| `Cinema` | id, name, region, address, city, facilities |
| `Region` | id, name |
| `User` | id, email, phone, name, avatar, dateOfJoined |
| `Actor` | name, headshotUrl |
| `DateModel` | Helper for date picker in session selection |

---

## API Reference

**Base URL** — configured in `app/src/main/java/…/data/remote/api/ApiClient.java`:

```java
private static final String BASE_URL = "http://<your-backend-host>/";
```

> ⚠️ Change `BASE_URL` in `ApiClient.java` to point to your own server before building for production.

All responses follow a common envelope:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { }
}
```

| Method | Path | Auth | Request | Response `data` |
|--------|------|------|---------|-----------------|
| GET | `/api/movies` | — | — | `List<Movie>` |
| GET | `/api/movies/{id}` | — | — | `Movie` |
| GET | `/api/showtimes` | — | `?movieId&regionId&date` | `List<Showtime>` |
| GET | `/api/showtimes/{id}` | — | — | `Showtime` (with full seat map) |
| GET | `/api/regions` | — | — | `List<Region>` |
| POST | `/api/booking` | ✅ Bearer | `BookingRequest` | `Booking` |
| GET | `/api/booking` | ✅ Bearer | — | `List<Booking>` |
| POST | `/api/chatbot` | — | `ChatRequest` | `ChatResponse` |

> **Authorization header format**: `Authorization: Bearer <jwt_token>`  
> The `AuthInterceptor` injects this header automatically on every request.

---

## Authentication

```
┌──────────────────────────────────────────────────────┐
│                  AUTHENTICATION FLOW                 │
│                                                      │
│  1. Email / Password                                 │
│     SignUpActivity ──► Firebase Auth createUser()   │
│     LoginActivity  ──► Firebase Auth signIn()       │
│                    ──► POST /api/auth/login          │
│                         └─► Returns JWT token       │
│                              └─► SessionManager     │
│                                   .saveAuthToken()  │
│                                                      │
│  2. Google Sign-In                                   │
│     GoogleSignInClient.getSignInIntent()             │
│     onActivityResult ──► Firebase credential        │
│                      ──► signInWithCredential()     │
│                                                      │
│  3. Facebook Login                                   │
│     FacebookSdk + LoginManager                      │
│     onSuccess ──► Firebase credential               │
│             ──► signInWithCredential()              │
│                                                      │
│  4. Token lifecycle                                  │
│     Stored in SharedPreferences ("AppSession")      │
│     Injected into every request by AuthInterceptor  │
│     Cleared on logout via SessionManager.clear()    │
└──────────────────────────────────────────────────────┘
```

**`SessionManager`** (utility class) is the single access point for the session:

```java
sessionManager.saveAuthToken(token);
sessionManager.getAuthToken();       // used by AuthInterceptor
sessionManager.setLoggedIn(true);
sessionManager.isLoggedIn();
sessionManager.clearSession();       // logout
```

---

## CI/CD

The project uses **GitHub Actions** (`.github/workflows/android.yml`).

| Step | Action |
|------|--------|
| Trigger | `push` or `pull_request` to `main` |
| Checkout | `actions/checkout@v4` |
| Setup JDK | `actions/setup-java@v4` — JDK 17 (Temurin) with Gradle cache |
| Make executable | `chmod +x gradlew` |
| Build | `./gradlew build` |
| Unit tests | `./gradlew test` |
| Upload APK | Debug APK uploaded as artifact (`app-debug-apk`) |

---

## Building & Running

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17
- Android SDK 36 (API 36 platform + build tools)
- A `local.properties` file with the following secrets (obtain from the project owner):

```properties
GOOGLE_WEB_CLIENT_ID=…
FACEBOOK_APP_ID=…
FACEBOOK_CLIENT_TOKEN=…
```

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/hmm0411/NT118.git
cd NT118

# 2. Open in Android Studio or build from the command line
./gradlew assembleDebug

# 3. Install on a connected device / emulator
./gradlew installDebug

# 4. Run unit tests
./gradlew test

# 5. Run instrumented tests (requires a running device/emulator)
./gradlew connectedAndroidTest
```

---

## Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| Android Gradle Plugin | 8.13.1 | Build system |
| `appcompat` | 1.7.1 | AndroidX compatibility |
| `material` | 1.13.0 | Material Design 3 components |
| `activity` | 1.11.0 | `ComponentActivity` base |
| `constraintlayout` | 2.2.1 | Flexible XML layouts |
| `recyclerview` | 1.4.0 | Lists and grids |
| `core-splashscreen` | 1.0.1 | Splash screen API |
| `lifecycle-viewmodel-ktx` | 2.6.2 | ViewModel + LiveData |
| `firebase-bom` | 34.5.0 | Firebase version alignment |
| `firebase-auth` | (BOM) | User authentication |
| `firebase-firestore` | (BOM) | Cloud NoSQL database |
| `firebase-analytics` | (BOM) | Usage analytics |
| `firebase-ui-firestore` | 8.0.2 | Firestore ↔ RecyclerView binding |
| `play-services-auth` | 21.2.0 | Google Sign-In |
| `facebook-android-sdk` | latest.release | Facebook Login |
| `retrofit2` | 2.9.0 | Type-safe HTTP client |
| `retrofit2:converter-gson` | 2.9.0 | JSON ↔ Java conversion |
| `okhttp3` | 4.12.0 | HTTP client |
| `okhttp3:logging-interceptor` | 4.12.0 | Debug HTTP logging |
| `gson` | 2.10.1 | JSON processing |
| `glide` | 4.16.0 | Async image loading & caching |
| `junit` | 4.13.2 | Unit testing |
| `espresso-core` | 3.7.0 | UI instrumented testing |
| `androidx.test.ext:junit` | 1.3.0 | AndroidJUnit4 runner |
