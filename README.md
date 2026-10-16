# Hela

A Vercel/Geist-style design system for Jetpack Compose.

[![](https://jitpack.io/v/Sam-muigai/Hela.svg)](https://jitpack.io/#Sam-muigai/Hela)

## Installation

**1. Add the JitPack repository** in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

**2. Add the dependency** in your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.Sam-muigai:Hela:0.1.0")
}
```

**Requirements:** min SDK 24, and Jetpack Compose enabled in your module.

## Usage

Wrap your UI in `HelaTheme`:

```kotlin
setContent {
    HelaTheme {
        HelaButton("Deploy", onClick = { /* ... */ })
    }
}
```

See [`hela/README.md`](hela/README.md) for the full component list, theming and previews.

## License

Hela bundles the Geist and Geist Mono fonts by Vercel, licensed under the
[SIL Open Font License 1.1](hela/OFL.txt).
