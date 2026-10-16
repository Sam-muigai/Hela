---
name: hela-design-system
description: How to build Android UI with Hela, a Vercel/Geist-style Jetpack Compose design system library (com.github.Sam-muigai:Hela, package com.samkt.hela), and how to make apps that look and feel like Vercel. Use whenever adding Hela to a project, writing or changing Compose UI in a project that uses Hela (screens, composables, previews, theming, colors, typography, spacing, icons, dark mode), reviewing UI code, contributing a component to Hela, or when the user asks for a "Vercel-like", "Geist-like", minimal, monochrome or developer-tool style Android app.
---

# Hela design system

Hela is a Jetpack Compose port of Vercel's Geist design system, published as an Android library.
It provides tokens (colors, type, spacing, radius, motion), a theme, 30+ stateless components and
the Geist Sans/Mono fonts. Source: https://github.com/Sam-muigai/Hela

Read the reference that matches the task. Don't load them all up front:

| Task | Read |
|---|---|
| Using a component: signatures, variants, gotchas | `references/components.md` |
| Colors, type, spacing, radius, motion: which token for which job | `references/tokens.md` |
| Building a whole screen: list, settings, form, detail, states | `references/screen-recipes.md` |
| Product and UX judgment, the "Vercel feel", copy, states, a11y | `references/vercel-best-practices.md` |
| Working inside the Hela repo itself: adding a component, releasing | `references/contributing.md` |

## Install

Check first: if the project already depends on `com.github.Sam-muigai:Hela` (or `project(":hela")`
in the Hela repo), skip to Setup.

1. JitPack repository, in `settings.gradle.kts`:
   ```kotlin
   dependencyResolutionManagement {
       repositories {
           google()
           mavenCentral()
           maven("https://jitpack.io")
       }
   }
   ```
2. Dependency, in the app module's `build.gradle.kts` (or `implementation(libs.hela)` with a
   version catalog entry `hela = { module = "com.github.Sam-muigai:Hela", version = "0.2.0" }`):
   ```kotlin
   dependencies {
       implementation("com.github.Sam-muigai:Hela:0.2.0")
   }
   ```
3. Requirements: `minSdk` ≥ 24, Compose enabled (`buildFeatures { compose = true }` and the
   `org.jetbrains.kotlin.plugin.compose` plugin). Hela brings Compose UI, foundation and
   ui-tooling-preview in through `api`, pinned by Compose BOM 2026.02.01. Material3 is used only
   internally, so the app doesn't need it.
4. Verify with `./gradlew :app:compileDebugKotlin`.

Note the coordinates differ from the package: the Maven artifact is `com.github.Sam-muigai:Hela`,
but imports are `com.samkt.hela.*`.

Hela bundles the Geist fonts under the SIL Open Font License 1.1. Apps that ship it should include
the license text (`hela/OFL.txt` in the Hela repo) in their open-source notices.

## Setup

Wrap the whole app in `HelaTheme` once, at the `setContent` root:

```kotlin
import com.samkt.hela.theme.HelaTheme

setContent {
    val systemDark = isSystemInDarkTheme()
    var dark by rememberSaveable { mutableStateOf(systemDark) }
    DisposableEffect(dark) { // keep status-bar icons readable when the app theme != system theme
        val style = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { dark }
        enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
        onDispose {}
    }
    HelaTheme(darkTheme = dark) { AppContent() }
}
```

If the app doesn't offer a theme toggle, `HelaTheme { AppContent() }` follows the system theme.
Set the activity's XML theme to a plain no-action-bar theme
(`android:Theme.Material.Light.NoActionBar`) so no Material chrome shows behind Compose.

`HelaTheme` provides the tokens and sets `LocalHelaContentColor` to `textPrimary` and
`LocalHelaTextStyle` to `compact` (14/20). It also installs `HelaIndication` as the default
`LocalIndication`, so a plain `Modifier.clickable {}` gets the flat Geist press tint, not a ripple.

Common imports:

```kotlin
import com.samkt.hela.components.*          // all components + HelaIcons
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaSpacing    // also HelaShapes, HelaRadius, HelaBorder, HelaMotion
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.foundation.HelaPreviewSurface
```

## Non-negotiable rules

1. **Hela components only.** Never use Material `Button`, `Text`, `TextField`, `Card`, `Switch`,
   `Checkbox`, `AlertDialog`, `Scaffold`'s defaults or `Icons.*` in feature code. Use `HelaButton`,
   `HelaText`, `HelaInput`, `HelaCard` and so on. If Hela lacks a component, compose one in the app
   from Hela tokens and primitives (`HelaText`, `HelaIcon`, `HelaCard`, `helaFocusRing`) following
   `references/contributing.md`, rather than reaching for Material.
2. **Tokens, never literals.** Colors come from `HelaTheme.colors`, text styles from
   `HelaTheme.typography`, spacing from `HelaSpacing`, corners from `HelaShapes`, and durations from
   `HelaMotion`. No `Color(0xFF…)`, `Color.Gray`, `14.sp` or `RoundedCornerShape(10.dp)` in feature
   code. Odd one-off dp values (1dp hairlines, a 2dp indicator) belong inside components, not screens.
3. **Prefer semantic roles to raw steps.** Use `textPrimary`, `textSecondary`, `border`, `surface`,
   `error` and similar before `gray.s900` or `red.s700`. Reach for a raw scale step only when no role
   fits, and pick it by the step's job (see tokens.md).
4. **Wrap the app (and every preview) in `HelaTheme`.** Previews use
   `@HelaPreviews` + `HelaPreviewSurface { }`, which renders light and dark side by side.
5. **Every screen has a preview.** Previews show every state (default, disabled, loading, error,
   empty) and must look right in both themes.
6. **Monochrome first.** Black and white with gray hierarchy. Color is reserved for meaning: blue
   for info/links/focus, red for errors and destructive actions, amber for warnings, green for
   success. Use one `Primary` button per screen.
7. **Hairlines, not shadows.** Separate surfaces with `HelaTheme.colors.border` 1dp borders or
   spacing. Only floating layers (menu, toast, modal, sheet) get shadows, and they already have them.
8. **Accessibility is part of done.** Icon-only buttons need a `contentDescription`. Never carry state
   by color alone (Hela pairs color with an icon or dot). Touch targets should be ≥ 48dp on primary
   mobile actions.

## Choosing a component

| Need | Use |
|---|---|
| Action | `HelaButton` (Primary once per screen, Secondary everyday, Tertiary in rows/toolbars, Error destructive). `HelaIconButton` for icon-only |
| Text | `HelaText` with a typography role |
| Single-line field | `HelaInput` (label, helper, error, prefix/suffix) |
| Multi-line field | `HelaTextarea` (`maxLength` shows a counter) |
| Pick one of many (> 5, or long labels) | `HelaSelect` (`optionDescription` when options need explaining) |
| Pick a date | `HelaDatePickerModal`, or `HelaCalendar` inline |
| Pick one of 2 to 5, visible | `HelaRadioGroup`, or `HelaSegmentedControl` for view modes and periods |
| On/off setting that applies immediately | `HelaSwitch` |
| Agree / multi-select | `HelaCheckbox` |
| Switch between views of the same thing | `HelaTabs` |
| Row overflow actions | `HelaMenu` in a `Box` with a `HelaIconButton(HelaIcons.More)`, or `HelaBottomSheet` on phones for more than 4 actions |
| Confirm a destructive or important action | `HelaModal` |
| Persistent inline message | `HelaNote` |
| Transient result of an action | `remember { HelaToastState() }`, `state.show(…)`, and a `HelaToastHost(state)` in the root `Box` |
| Container | `HelaCard` (+ `HelaCardFooter`) |
| Status | `HelaStatusDot` (deployment-like states) or `HelaBadge` (labels, counts) |
| Command, URL, ID to copy | `HelaSnippet` |
| Loading a known layout | `HelaSkeleton` shaped like the content |
| Loading in place (button, inline) | `loading = true` on the button, `HelaSpinner`, `HelaLoadingDots` |
| Progress / usage | `HelaProgress` (bar), `HelaGauge` (ring, auto-colored by threshold) |
| Nothing to show | `HelaEmptyState` with one action |
| Expandable FAQ/details | `HelaCollapse` (+ `HelaDivider` between) |
| People | `HelaAvatar`, `HelaAvatarGroup` |
| Keyboard shortcut hint | `HelaKbd` |

## Building a screen: workflow

1. Read `references/screen-recipes.md` for the closest recipe and start from it.
2. Lay out with `Column`/`Row` + `Arrangement.spacedBy(HelaSpacing.*)`, not ad-hoc `Spacer`s
   everywhere. Use a 16dp (`s4`) screen gutter, 24 to 32dp (`s6`–`s8`) between sections, and 8 to
   12dp (`s2`–`s3`) inside a group.
3. Respect insets: put `windowInsetsPadding(WindowInsets.safeDrawing)` *before* `verticalScroll`.
4. Design every state, not just the happy path: loading (skeleton), empty, error (`HelaNote` + retry),
   and content. See the `UiState` recipe.
5. Write copy per the best-practices guide: sentence case, short, verbs on buttons.
6. Add a `@HelaPreviews` preview per state, fed with fake data (no ViewModel in previews: hoist state
   and preview the stateless composable).
7. Build: `./gradlew :app:compileDebugKotlin`. If a device or emulator is available, check light and dark.

## Troubleshooting

| Symptom | Fix |
|---|---|
| `Could not find com.github.Sam-muigai:Hela` | `maven("https://jitpack.io")` missing from `settings.gradle.kts`, or added under `pluginManagement` instead of `dependencyResolutionManagement` |
| Unresolved `com.samkt.hela…` imports | Dependency added to the wrong module; it must be in the module that contains the UI code |
| Always light colors, ignores dark mode, Roboto instead of Geist | Composable rendered outside `HelaTheme` (often a preview missing `HelaPreviewSurface`); the locals fall back to light defaults silently |
| Ripples instead of the flat press tint | A Material `MaterialTheme` wraps content inside `HelaTheme` and resets `LocalIndication`; remove it |
| Status-bar icons invisible after theme toggle | Missing the `DisposableEffect` + `enableEdgeToEdge` block from Setup |
| Compose version conflicts | Hela pins Compose via BOM 2026.02.01; use the same or a newer BOM in the app |

## Review checklist

- [ ] No Material components or `Icons.*` in feature code. No hard-coded colors, sp or radius values.
- [ ] At most one `Primary` button visible. Destructive actions use `Error` and are confirmed.
- [ ] Loading, empty and error states exist and have previews.
- [ ] Looks right in light **and** dark (check the dark preview, not just light).
- [ ] Icon-only controls have descriptions. Errors are announced (`HelaInput(error=…)` does this).
- [ ] Copy is sentence case, concise, and buttons say what they do ("Delete project", not "OK").
- [ ] Insets handled. Nothing hides under status/navigation bars. The toast host sits in the root `Box`.
