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
    implementation("com.github.Sam-muigai:Hela:0.2.0")
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

## Claude Code skill

[`skills/hela-design-system`](skills/hela-design-system) teaches Claude Code how to install and use
Hela: setup, which component to use when, tokens, screen recipes and Vercel-style UX rules. To use it
in your project, copy the folder into `.claude/skills/`:

```bash
git clone --depth 1 https://github.com/Sam-muigai/Hela.git /tmp/hela
mkdir -p .claude/skills && cp -r /tmp/hela/skills/hela-design-system .claude/skills/
```

Claude Code then picks it up automatically when you work on Compose UI.

## License

Hela bundles the Geist and Geist Mono fonts by Vercel, licensed under the
[SIL Open Font License 1.1](hela/OFL.txt).
