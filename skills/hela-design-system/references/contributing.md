# Contributing to Hela

Only for work inside the Hela repo (github.com/Sam-muigai/Hela). App developers using Hela as a
dependency don't need this: if a component is missing, build it in your app from Hela tokens and
components, or open an issue/PR on the Hela repo.

## Repo layout

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
skills/hela-design-system/         this skill (symlinked into .claude/skills/)
```

The sample app depends on the library with `implementation(project(":hela"))`. Anything
Hela-specific (components, tokens, icons) belongs in `hela/`, never in `app/`.

## Adding or changing a component

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
   - Material3 is allowed only inside Hela (`HelaTheme` maps tokens onto `MaterialTheme`,
     `HelaBottomSheet` wraps `ModalBottomSheet`). Never expose Material types in public signatures.
4. Add `@HelaPreviews` previews covering every variant, size and state.
5. Add it to the sample gallery `app/.../ui/gallery/HelaGallery.kt` in the right section.
6. Document it in `references/components.md` of this skill.
7. Compile `./gradlew :hela:assembleRelease :app:assembleDebug`. Fix all warnings you introduced.

## Releasing

Hela is published through JitPack from git tags.

1. Versioning: a new component or parameter is a minor bump. A renamed or removed parameter, or a
   visual change to an existing component, is a breaking change.
2. Bump `version` in `hela/build.gradle.kts`, add an entry to `hela/CHANGELOG.md`, and update the
   version in `README.md` and in the Install section of this skill's `SKILL.md`.
3. Check the build locally: `./gradlew :hela:publishToMavenLocal`.
4. Commit, tag with the bare version (`git tag 0.2.0`, no `v` prefix), and push the commit and tag.
5. Request `https://jitpack.io/com/github/Sam-muigai/Hela/<version>/Hela-<version>.pom` to trigger
   the build, then check `build.log` in the same folder.
