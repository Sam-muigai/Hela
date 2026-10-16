# Hela

A Vercel/Geist-style design system for Jetpack Compose: tokens, theme and 30+ components, each with
light and dark previews. Geist Sans and Geist Mono are bundled.

- Min SDK 24, compile SDK 37
- Depends on Compose (BOM `2026.02.01`). Material3 is used internally only.

## Install

**In this repo** (the sample app already does this):
```kotlin
// app/build.gradle.kts
dependencies { implementation(project(":hela")) }
```

**In another project** (via [JitPack](https://jitpack.io/#Sam-muigai/Hela)):
```kotlin
// settings.gradle.kts → dependencyResolutionManagement.repositories
maven("https://jitpack.io")

// app/build.gradle.kts
dependencies { implementation("com.github.Sam-muigai:Hela:0.1.0") }
```

**Local development:** `./gradlew :hela:publishToMavenLocal`, add `mavenLocal()` to your repositories
and depend on `com.samkt:hela:0.1.0`.

## Quick start

```kotlin
setContent {
    HelaTheme {                                   // follows the system theme; pass darkTheme to override
        Column(
            Modifier.fillMaxSize().background(HelaTheme.colors.background100).padding(HelaSpacing.s4),
            verticalArrangement = Arrangement.spacedBy(HelaSpacing.s3),
        ) {
            HelaText("Deployments", style = HelaTheme.typography.heading24)
            HelaInput(query, { query = it }, placeholder = "Search…", prefix = { HelaIcon(HelaIcons.Search, null) })
            HelaButton("Deploy", onClick = ::deploy)
        }
    }
}
```

Previews of your own screens:
```kotlin
@HelaPreviews   // light + dark
@Composable
private fun MyScreenPreview() = HelaPreviewSurface { MyScreen(fakeState) }
```

## Packages

| Package | Contents |
|---|---|
| `com.samkt.hela.theme` | `HelaTheme`, `LocalHelaColors`, `LocalHelaTypography`, `LocalHelaContentColor`, `LocalHelaTextStyle` |
| `com.samkt.hela.tokens` | `HelaColors`, `HelaTypography`, `HelaSpacing`, `HelaRadius`, `HelaShapes`, `HelaBorder`, `HelaMotion` |
| `com.samkt.hela.foundation` | `HelaIndication`, `Modifier.helaFocusRing`, `HelaPreviews`, `HelaPreviewSurface` |
| `com.samkt.hela.components` | All components and `HelaIcons` |

The full component guide and usage rules are in the Claude Code skill at
[`skills/hela-design-system/`](../skills/hela-design-system), and
[`references/components.md`](../skills/hela-design-system/references/components.md) is readable on its own.

## Fonts

Geist and Geist Mono are by Vercel and licensed under the SIL Open Font License 1.1. Include the
license text ([`OFL.txt`](OFL.txt), from https://github.com/vercel/geist-font) with any distribution of
this library.
