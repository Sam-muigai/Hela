---
name: hela-design-system
description: How to build Android UI with Hela, this project's Vercel/Geist-style Jetpack Compose design system, and how to make apps that look and feel like Vercel. Use whenever writing or changing any Compose UI in this repo (screens, composables, previews, theming, colors, typography, spacing, icons, dark mode), adding or modifying a Hela component, reviewing UI code, or when the user asks for a "Vercel-like", "Geist-like", minimal, monochrome or developer-tool style app.
---

# Hela design system

Hela is a Jetpack Compose port of Vercel's Geist design system, shipped as its own Android
library module `:hela` (package `com.samkt.hela`, Maven `com.samkt:hela`). The `:app` module is the
sample app that consumes it.

```
hela/                              the library module
  build.gradle.kts                 com.android.library + maven-publish (version lives here)
  src/main/res/font/               Geist Sans + Geist Mono (bundled with the library)
  src/main/java/com/samkt/hela/
  tokens/      HelaColors, GeneratedColors (Light/DarkHelaColors), HelaTypography, HelaDimens
               (HelaSpacing, HelaRadius, HelaShapes, HelaBorder, HelaMotion), HelaTokenPreviews
  theme/       HelaTheme, LocalHelaColors, LocalHelaTypography, LocalHelaContentColor, LocalHelaTextStyle
  foundation/  HelaIndication (flat press tint, replaces ripple), helaFocusRing, HelaPreviews
  components/  one file per component, each with its own @HelaPreviews
app/src/main/java/com/samkt/vercel/ui/gallery/HelaGallery.kt
                                   sample: every component on one screen (the app's start screen)
```

Library code goes in `hela/`. App screens go in `app/`. The app depends on it with
`implementation(project(":hela"))`, and other projects use `implementation("com.samkt:hela:<version>")`.
Anything Hela-specific (components, tokens, icons) belongs in the library, never in an app module.

Read the reference that matches the task. Don't load them all up front:

| Task | Read |
|---|---|
| Using a component: signatures, variants, gotchas | `references/components.md` |
| Colors, type, spacing, radius, motion: which token for which job | `references/tokens.md` |
| Building a whole screen: list, settings, form, detail, states | `references/screen-recipes.md` |
| Product and UX judgment, the "Vercel feel", copy, states, a11y | `references/vercel-best-practices.md` |

## Non-negotiable rules

1. **Hela components only.** Never use Material `Button`, `Text`, `TextField`, `Card`, `Switch`,
   `Checkbox`, `AlertDialog`, `Scaffold`'s defaults or `Icons.*` in feature code. Use `HelaButton`,
   `HelaText`, `HelaInput`, `HelaCard` and so on. Material3 appears only inside Hela, where `HelaTheme`
   maps tokens onto `MaterialTheme` and `HelaBottomSheet` wraps `ModalBottomSheet`. If a Hela
   component is missing, add it to Hela (see "Adding a component") rather than reaching for Material.
2. **Tokens, never literals.** Colors come from `HelaTheme.colors`, text styles from
   `HelaTheme.typography`, spacing from `HelaSpacing`, corners from `HelaShapes`, and durations from
   `HelaMotion`. No `Color(0xFF…)`, `Color.Gray`, `14.sp` or `RoundedCornerShape(10.dp)` in feature
   code. Odd one-off dp values (1dp hairlines, a 2dp indicator) belong inside components, not screens.
3. **Prefer semantic roles to raw steps.** Use `textPrimary`, `textSecondary`, `border`, `surface`,
   `error` and similar before `gray.s900` or `red.s700`. Reach for a raw scale step only when no role
   fits, and pick it by the step's job (see tokens.md).
4. **Wrap the app (and every preview) in `HelaTheme`.** Previews use
   `@HelaPreviews` + `HelaPreviewSurface { }`, which renders light and dark side by side.
5. **Every component has a preview. Every screen has one too.** Previews show all variants and states
   (default, disabled, loading, error, empty) and must look right in both themes.
6. **Monochrome first.** Black and white with gray hierarchy. Color is reserved for meaning: blue
   for info/links/focus, red for errors and destructive actions, amber for warnings, green for
   success. Use one `Primary` button per screen.
7. **Hairlines, not shadows.** Separate surfaces with `HelaTheme.colors.border` 1dp borders or
   spacing. Only floating layers (menu, toast, modal, sheet) get shadows, and they already have them.
8. **Accessibility is part of done.** Icon-only buttons need a `contentDescription`. Never carry state
   by color alone (Hela pairs color with an icon or dot). Touch targets should be ≥ 48dp on primary
   mobile actions.

## Setup (already done in `MainActivity`)

```kotlin
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

`HelaTheme` provides the tokens and sets `LocalHelaContentColor` to `textPrimary` and
`LocalHelaTextStyle` to `compact` (14/20). It also installs `HelaIndication` as the default
`LocalIndication`, so a plain `Modifier.clickable {}` gets the flat Geist press tint, not a ripple.

## Choosing a component

| Need | Use |
|---|---|
| Action | `HelaButton` (Primary once per screen, Secondary everyday, Tertiary in rows/toolbars, Error destructive). `HelaIconButton` for icon-only |
| Text | `HelaText` with a typography role |
| Single-line field | `HelaInput` (label, helper, error, prefix/suffix) |
| Multi-line field | `HelaTextarea` (`maxLength` shows a counter) |
| Pick one of many (> 5, or long labels) | `HelaSelect` |
| Pick one of 2 to 5, visible | `HelaRadioGroup`, or `HelaSegmentedControl` for view modes and periods |
| On/off setting that applies immediately | `HelaSwitch` |
| Agree / multi-select | `HelaCheckbox` |
| Switch between views of the same thing | `HelaTabs` |
| Row overflow actions | `HelaMenu` in a `Box` with a `HelaIconButton(HelaIcons.More)`, or `HelaBottomSheet` on phones for more than 4 actions |
| Confirm a destructive or important action | `HelaModal` |
| Persistent inline message | `HelaNote` |
| Transient result of an action | `HelaToastState.show(…)` + `HelaToastHost` |
| Container | `HelaCard` (+ `HelaCardFooter`) |
| Status | `HelaStatusDot` (deployment-like states) or `HelaBadge` (labels, counts) |
| Command, URL, ID to copy | `HelaSnippet` |
| Loading a known layout | `HelaSkeleton` shaped like the content |
| Loading in place (button, inline) | `loading = true` on the button, `HelaSpinner`, `HelaLoadingDots` |
| Progress / usage | `HelaProgress` (bar), `HelaGauge` (ring, auto-colored by threshold) |
| Nothing to show | `HelaEmptyState` with one action |
| Expandable FAQ/details | `HelaCollapse` (+ `HelaDivider` between) |
| People | `HelaAvatar`, `HelaAvatarGroup` |

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
7. Build: `./gradlew :app:compileDebugKotlin` (this also compiles `:hela`). If a device or emulator is available, check light and dark.

## Adding or changing a Hela component

1. One file per component: `hela/src/main/java/com/samkt/hela/components/Hela<Name>.kt`, package
   `com.samkt.hela.components`. If it needs a new library, add it to `hela/build.gradle.kts`: use
   `api(...)` if its types appear in Hela's public signatures, otherwise `implementation(...)`.
2. API shape, matching the existing components:
   - Stateless: value plus `on…Change` callback, `modifier: Modifier = Modifier` as the first optional param.
   - Variants and sizes are `enum class Hela<Name>Variant` / `Hela<Name>Size`, with dimensions as
     `internal val` on the enum.
   - Slots are nullable `(@Composable () -> Unit)?` named `prefix`, `suffix`, `action`, `trailing` or `content`.
   - Accept `interactionSource: MutableInteractionSource? = null` if interactive.
   - A KDoc on the public function says when to use it and when to use something else.
3. Implementation rules:
   - Read colors via `HelaTheme.colors` inside the composable. Animate state colors with
     `animateColorAsState(target, tween(HelaMotion.FAST_MS))`.
   - Interactive components pass `indication = null` and draw their own hover/press/disabled colors.
     Show focus with `Modifier.helaFocusRing(visible = focused, shape, color = c.focusRing)` applied
     **before** `clip`.
   - Set content color/style for children through `CompositionLocalProvider(LocalHelaContentColor
     provides …, LocalHelaTextStyle provides …)` so `HelaText` and `HelaIcon` inside pick them up.
   - Semantics: `role` on clickables/toggleables, `stateDescription`/`error`/`liveRegion` where
     relevant, `contentDescription` for non-text visuals.
   - Popups/dialogs: also expose a `Hela<Name>Panel` with the visual only, so previews can render it
     (see `HelaMenuPanel`, `HelaModalPanel`, `HelaBottomSheetPanel`).
4. Add `@HelaPreviews` previews covering every variant, size and state.
5. Add it to the sample gallery `app/.../ui/gallery/HelaGallery.kt` in the right section.
6. Document it in `references/components.md` of this skill.
7. Compile `./gradlew :hela:assembleRelease :app:assembleDebug`. Fix all warnings you introduced.
8. Treat it as a release: a new component or parameter is a minor version bump. A renamed or removed
   parameter, or a visual change to an existing component, is a breaking change. Bump `version` in
   `hela/build.gradle.kts` and note it in `hela/CHANGELOG.md`.

## Review checklist

- [ ] No Material components or `Icons.*` in feature code. No hard-coded colors, sp or radius values.
- [ ] At most one `Primary` button visible. Destructive actions use `Error` and are confirmed.
- [ ] Loading, empty and error states exist and have previews.
- [ ] Looks right in light **and** dark (check the dark preview, not just light).
- [ ] Icon-only controls have descriptions. Errors are announced (`HelaInput(error=…)` does this).
- [ ] Copy is sentence case, concise, and buttons say what they do ("Delete project", not "OK").
- [ ] Insets handled. Nothing hides under status/navigation bars. The toast host sits in the root `Box`.
