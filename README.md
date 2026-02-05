# The movie database search

Welcome to the Beatgrid mobile assignment.

## 🎯 Objective

You are building a Google for movies. Your app allows users to search through a movies dataset using an autocomplete search input. The app should work on both iOS and Android platforms using Kotlin Multiplatform.

### ✅ Requirements

- As the user types in the search input, display a dropdown with autocomplete suggestions.
- When the user selects a movie from the dropdown, update the search input and display a card with detailed information about the selected title.
- The details card should display the movie's name along with any other relevant fields.
- When the search input is focused and empty, the dropdown should show the 10 most recently selected movies, ordered by the most recent to the least recent.

### ⏱️ Time

- This assignment is designed to be completed in about 5 hours.
- You are not expected to build a polished product. Focus on correctness, clarity, and functionality.

## 🧩 Components

### Prerequisites

**macOS is required** for this assignment (Xcode needed for iOS development).

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Android Studio](https://developer.android.com/studio) with Android SDK and Emulator
- [Xcode](https://apps.apple.com/app/xcode/id497799835) with iOS Simulator
- Java 17+ (or use Android Studio's bundled JDK)

### Backend

A REST API and MariaDB database are provided for you.

To run the backend:

- make sure you have [Docker](https://docs.docker.com/) installed
- run `docker compose up -d`

This starts MariaDB (port 53306) and the REST API (port 8080). Wait for `Started ApplicationKt` in the logs.

Verify it's running:
```bash
curl http://localhost:8080/api/movies/search?q=dark
```

### Mobile App

The mobile app uses Kotlin Multiplatform.

**Android:** Open `mobile/` in Android Studio, run `androidApp` configuration.

**iOS:** Open `mobile/iosApp/iosApp.xcodeproj` in Xcode and run.

> **Tip:** Changes to shared Kotlin code require rebuilding before they appear in Xcode.

### Platform Notes

- **Android:** Base URL for emulator is `http://10.0.2.2:8080`
- **iOS:** Base URL for simulator is `http://localhost:8080`

## 🔌 API Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/movies/search?q={query}` | Search movies by name |
| GET | `/api/movies/{id}` | Get movie details |
| POST | `/api/movies/{id}/select` | Record a movie selection |
| GET | `/api/movies/recent` | Get 10 most recent selections |

### Example Responses

**Search** `GET /api/movies/search?q=dark`
```json
[{"id": 5, "name": "The Dark Knight", "year": 2008, "thumbnailUrl": "https://..."}]
```

**Details** `GET /api/movies/5`
```json
{
  "id": 5,
  "name": "The Dark Knight",
  "description": "When the menace known as the Joker...",
  "year": 2008,
  "genres": ["Action", "Crime", "Drama"],
  "actors": ["Christian Bale", "Heath Ledger"],
  "directors": ["Christopher Nolan"],
  "imageUrl": "https://...",
  "thumbnailUrl": "https://...",
  "rating": 9.0,
  "duration": "PT2H32M"
}
```

**Recent** `GET /api/movies/recent`
```json
[{"movieId": 5, "name": "The Dark Knight", "year": 2008, "thumbnailUrl": "https://...", "selectedAt": "2024-01-15T10:30:00Z"}]
```

## 🧪 Notes

- You can use any libraries you want.
- The design is up to you.
- Focus on one platform first if time is limited.
- Feel free to reach out if you have any questions.

## 📦 What to Submit

Please provide:
- The GitHub repository link

## 🙌 Good luck!

We're looking forward to reviewing your solution.
