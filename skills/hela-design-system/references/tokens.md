# Hela tokens

Read them in composables through `HelaTheme.colors` / `HelaTheme.typography`. Spacing, shapes,
borders and motion are plain objects: `HelaSpacing`, `HelaShapes`, `HelaRadius`, `HelaBorder`, `HelaMotion`
(package `com.samkt.hela.tokens`).

## Colors

The values are in `tokens/GeneratedColors.kt` (`LightHelaColors`, `DarkHelaColors`), generated from
Vercel's published Geist CSS. Don't hand-edit them.

### Semantic roles: use these first
| Role | Maps to | Use for |
|---|---|---|
| `surface` | background100 | Page, card and input backgrounds |
| `surfaceSubtle` | background200 | Card footers, disabled inputs, secondary areas |
| `textPrimary` | gray.s1000 | Body text, headings, primary icons |
| `textSecondary` | gray.s900 | Supporting text, labels, metadata, secondary icons |
| `textDisabled` | gray.s700 | Disabled text and placeholders |
| `border` | grayAlpha.s400 | Default hairlines: cards, inputs, dividers |
| `borderHover` | grayAlpha.s500 | Hovered borders |
| `borderActive` | grayAlpha.s600 | Focused/pressed borders |
| `focusRing` | blue.s700 (light) / s900 (dark) | Keyboard focus ring |
| `success` / `warning` / `error` / `info` | green/amber/red/blue .s900 | Text and icons that carry meaning |
| `inverseSurface` / `onInverseSurface` | gray.s1000 / background100 | Monochrome primary: Primary button, Inverted badge, checked checkbox |
| `isDark` | | Rarely needed. Branch on it only for things tokens can't express (scrim opacity). |

### Scales: when no role fits
Every scale (`gray`, `grayAlpha`, `blue`, `red`, `amber`, `green`) has steps `s100`…`s1000`, and
**each step has a fixed job** (Geist's rule). Pick the step by its job, never by how it looks:

| Step | Job |
|---|---|
| s100 | Default component background (e.g. a filled note's tint) |
| s200 | Hover background |
| s300 | Active/pressed background |
| s400 | Default border |
| s500 | Hover border |
| s600 | Active border |
| s700 | High-contrast background (solid badges, error button, status dots, progress fills) |
| s800 | Hover state of s700 |
| s900 | Secondary text/icons, and accent text on tinted backgrounds |
| s1000 | Primary text/icons, and darkest accent text |

- `grayAlpha` is translucent. Use it when the surface underneath must show through: borders over tinted
  areas, hover/press overlays (`grayAlpha.s200`/`s300` composited over `surface`), shadows.
- Tinted container pattern: `scale.s100` background + `scale.s400` border + `scale.s900` text/icon.
  This is what `HelaNote` does.
- Never mix a light-theme hex into dark mode. Tokens swap automatically, and hard-coded colors break dark mode.

### Color meaning (keep it consistent app-wide)
- **Blue**: info, links, focus, "in progress" accents. **Red**: errors, destructive, over limit.
  **Amber**: warnings, building, pending. **Green**: success, ready, healthy.
- Everything else is grayscale. If you're adding color "to make it pop", use type weight, size or
  spacing instead.

## Typography

The font is Geist Sans, with Geist Mono for code. Headings carry **negative tracking**, which is a
big part of the Vercel look, so never reset `letterSpacing` at call sites.

| Role | Size/line | Weight | Use |
|---|---|---|---|
| `display` | 48/56 | SemiBold | Marketing hero only |
| `title` | 40/48 | SemiBold | Marketing/onboarding titles |
| `heading32` | 32/40 | SemiBold | Large page titles (tablet) |
| `heading24` | 24/32 | SemiBold | Screen titles on phones, modal titles |
| `heading20` | 20/26 | SemiBold | Sheet titles, prominent section titles |
| `heading16` | 16/24 | SemiBold | Section titles, card titles, list row titles |
| `lede` | 18/28 | Regular | Intro paragraph under a title |
| `body` | 16/24 | Regular | Long-form reading text |
| `compact` | 14/20 | Regular | **Default UI text**: rows, descriptions, inputs |
| `label16` | 16/24 | Medium | Large buttons |
| `label14` | 14/20 | Medium | Buttons, tabs, emphasized labels, status labels |
| `label13` | 13/18 | Regular | Field labels, helper/error text, metadata |
| `caption` | 12/16 | Regular | Timestamps, fine print, small badges |
| `mono14` / `mono13` | 14/20, 13/18 | Regular | Code, commands, IDs, hashes, counters, numbers in tables |

Guidance:
- A phone screen usually needs only **three or four roles**: `heading24` (title), `heading16`
  (section), `compact` (content), `label13`/`caption` + `textSecondary` (metadata).
- Build hierarchy with weight and color (`textPrimary` vs `textSecondary`) before size.
- Show IDs, commit SHAs, URLs in code, env var names, durations and counts in tables in mono.
- To tweak one property: `HelaTheme.typography.label14.copy(fontSize = …)`. This is rare, and only
  inside components.

## Spacing (4dp grid)

`HelaSpacing.sN` = N × 4dp: `s1`=4, `s2`=8, `s3`=12, `s4`=16, `s5`=20, `s6`=24, `s8`=32, `s10`=40, `s12`=48, `s16`=64.

| Context | Token |
|---|---|
| Icon ↔ label, badge internals | `s1`–`s2` |
| Items inside a group (form fields, list items) | `s3`–`s4` |
| Screen horizontal gutter | `s4` |
| Card padding | `s6` (the `HelaCard` default) |
| Between sections | `s6`–`s8` |
| Empty-state vertical padding, big breathing room | `s10`–`s16` |

Prefer `Arrangement.spacedBy(HelaSpacing.sX)` on the parent over `Spacer`s between every child.

## Radius & shapes

| Token | dp | Use |
|---|---|---|
| `HelaShapes.small` | 6 | Buttons, inputs, notes, badges' container corners, snippets |
| `HelaShapes.default` | 8 | Menus, popovers, toasts, icon frames |
| `HelaShapes.large` | 12 | Cards, modals, sheets (top corners) |
| `HelaShapes.full` | pill/circle | Avatars, badges, switches, pill buttons, status dots |

Nested corners: the inner radius is the outer radius minus the padding (e.g. the segmented control
uses 6 outside and 4 inside).

## Borders

`HelaBorder.hairline` = 1dp (all borders and dividers). `focusRingWidth` = 2dp, `focusRingOffset` = 2dp
(used by `Modifier.helaFocusRing`). Inputs use a 3dp soft ring with no offset instead.

## Motion

`HelaMotion.FAST_MS` = 150 (color/hover/press), `DEFAULT_MS` = 200 (toggles, expand, toasts),
`SLOW_MS` = 300 (progress fills). Easing: `HelaMotion.easeOut` for things entering or settling,
`easeInOut` for movement between two states.
- Short and calm, with nothing springy or bouncy. Animate opacity, color and small translations/scale
  (0.96 → 1), not big movements.
- Exits are faster than entrances.

## Shadows (elevation)

Only floating layers have shadows: menu (8dp), toast (16dp), modal (24dp), sheet (system). Their
ambient/spot colors are `grayAlpha.s400`–`s600`, so they stay subtle in dark mode. Cards and inline
surfaces never get a shadow. Use a border.
