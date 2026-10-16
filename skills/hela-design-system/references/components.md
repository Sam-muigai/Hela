# Hela components: API reference

All components are in `com.samkt.hela.components`. Every one is stateless (hoist the
state) and takes `modifier` as its first optional parameter. Imports you'll almost always need:

```kotlin
import com.samkt.hela.components.*
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaSpacing
import com.samkt.hela.tokens.HelaShapes
```

## Contents
- Text & icons: HelaText, HelaIcon / HelaIcons
- Actions: HelaButton, HelaIconButton
- Form: HelaInput, HelaTextarea, HelaSelect, HelaCheckbox, HelaRadio / HelaRadioGroup, HelaSwitch, HelaSegmentedControl
- Navigation: HelaTabs, HelaMenu, HelaCollapse
- Overlays: HelaModal, HelaBottomSheet, HelaToast
- Feedback: HelaNote, HelaSpinner, HelaLoadingDots, HelaSkeleton, HelaProgress, HelaGauge
- Display: HelaCard, HelaBadge, HelaStatusDot, HelaAvatar, HelaSnippet, HelaKbd, HelaEmptyState, HelaDivider
- Previews: HelaPreviews, HelaPreviewSurface, HelaPreviewLabel

---

## Text & icons

### HelaText
```kotlin
HelaText(text, modifier, style = LocalHelaTextStyle.current, color = Color.Unspecified,
         textAlign = null, maxLines = Int.MAX_VALUE, overflow = TextOverflow.Ellipsis)
```
- The default style is the current `LocalHelaTextStyle` (`compact`, 14/20, unless a parent such as a
  button changed it). The default color is the current `LocalHelaContentColor`.
- Pass `color` only for a semantic reason: `HelaTheme.colors.textSecondary` for supporting text,
  `.error` for errors.
- Headings: add `Modifier.semantics { heading() }` for screen and section titles.
```kotlin
HelaText("Deployments", Modifier.semantics { heading() }, style = HelaTheme.typography.heading24)
HelaText("Updated 2m ago", style = HelaTheme.typography.label13, color = HelaTheme.colors.textSecondary)
```

### HelaIcon / HelaIcons
```kotlin
HelaIcon(icon: ImageVector, contentDescription: String?, modifier, size = 16.dp, tint = Color.Unspecified)
```
- Stroke icons on a 24-unit grid, tinted with the current content color.
- `contentDescription = null` when adjacent text already says it (button prefix, menu item).
- Available: `Check, Minus, Plus, Close, ChevronDown, ChevronUp, ChevronLeft, ChevronRight, ArrowRight,
  Search, Info, Warning, Success, Error, Copy, Eye, User, Home, More, Inbox, Trash, Settings`.
- To add an icon, add `val Name by lazy { strokeIcon("Name", "<svg path d>") }` to `HelaIcons` (use a
  24×24 stroke-style SVG path, e.g. from Lucide or Geist icons) and list it in `HelaIcons.all`.
- Sizes: 16dp inline and in Small/Medium buttons, 20dp in Large buttons and emphasis spots.

## Actions

### HelaButton
```kotlin
HelaButton(text, onClick, modifier, variant = Primary, size = Medium, enabled = true,
           loading = false, pill = false, prefix = null, suffix = null)
HelaButton(onClick, modifier, …, interactionSource = null) { /* RowScope content */ }
```
| Variant | Use |
|---|---|
| `Primary` | The one main action on the screen. Black on light, white on dark. |
| `Secondary` | Everyday actions. Surface + hairline border. |
| `Tertiary` | Low emphasis: toolbars, rows, "Cancel" next to many actions. No border. |
| `Error` | Destructive actions (delete, revoke). Confirm them with `HelaModal`. |

| Size | Height | Use |
|---|---|---|
| `Small` | 32dp | Dense rows, inside cards/notes/toasts |
| `Medium` | 40dp | Default |
| `Large` | 48dp | Primary mobile CTAs, full-width form submits |

- `loading = true` shows a spinner in place of `prefix`, blocks clicks and keeps the width. Use it for
  async submits instead of swapping the label.
- `prefix`/`suffix` are usually `{ HelaIcon(HelaIcons.Plus, null) }`. `pill = true` gives fully rounded ends.
- Full width: `modifier = Modifier.fillMaxWidth()` (content stays centered).

### HelaIconButton
```kotlin
HelaIconButton(icon, contentDescription: String, onClick, modifier, variant = Secondary,
               size = Medium, enabled = true, loading = false, pill = false)
```
A square button. `contentDescription` is required. Use the `Tertiary` variant for toolbar and row actions.

## Form

### HelaInput
```kotlin
HelaInput(value, onValueChange, modifier, label = null, placeholder = null, helper = null,
          error = null, size = HelaInputSize.Medium, enabled = true, readOnly = false,
          prefix = null, suffix = null, keyboardOptions, keyboardActions, visualTransformation,
          interactionSource = null)
```
- `error != null` turns the border red, shows the message with a dot, and announces it to TalkBack.
  `error` replaces `helper` while present.
- Sizes: Small 32, Medium 40, Large 48 (Large uses `body` 16sp text).
- Prefix/suffix get `textSecondary` as content color: icons, units (`HelaText("KES", style = mono13)`),
  a show-password toggle.
- Password: `visualTransformation = PasswordVisualTransformation()` +
  `keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)`.
- Validate on submit or on blur, not on every keystroke. Clear the error when the user edits.

### HelaTextarea
```kotlin
HelaTextarea(value, onValueChange, modifier, label = null, placeholder = null, helper = null,
             error = null, enabled = true, minLines = 3, maxLines = 8, maxLength = null)
```
`maxLength` shows a mono `n/max` counter and blocks input past it.

### HelaSelect
```kotlin
HelaSelect(options: List<T>, selected: T?, onSelect: (T) -> Unit, modifier, label = null,
           placeholder = "Select…", error = null, size = HelaInputSize.Medium, enabled = true,
           optionLabel: (T) -> String = { it.toString() })
```
An input-styled trigger that opens a `HelaMenu` as wide as the trigger, with a check on the selected
option. Use `optionLabel` for enums and data classes.

### HelaCheckbox
```kotlin
HelaCheckbox(checked, onCheckedChange, modifier, label = null, indeterminate = false, enabled = true)
```
The whole row (box + label) is the touch target. `indeterminate` is for a "select all" parent.

### HelaRadio / HelaRadioGroup
```kotlin
HelaRadioGroup(options: List<T>, selected: T?, onSelect, modifier, enabled = true, optionLabel)
HelaRadio(selected, onClick, modifier, label = null, enabled = true)
```
The group is vertical with 12dp spacing and has `selectableGroup()` semantics.

### HelaSwitch
```kotlin
HelaSwitch(checked, onCheckedChange, modifier, label = null, enabled = true)
```
Geist calls this `Toggle`. Use it for settings that take effect immediately. If the change needs a
Save button, use a checkbox.

### HelaSegmentedControl
```kotlin
HelaSegmentedControl(options: List<String>, selectedIndex, onSelect, modifier, fillWidth = false, height = 40.dp)
```
Geist calls this `Switch`. Use it for 2 to 4 short, mutually exclusive view options (Monthly/Yearly,
Source/Output). `fillWidth = true` makes segments equal width.

## Navigation

### HelaTabs
```kotlin
HelaTabs(tabs: List<String>, selectedIndex, onSelect, modifier)
```
Labels on a full-width hairline, with the selected one underlined. It scrolls horizontally when the
labels don't fit. Put the tab content below; use `HelaSegmentedControl` instead for small toggles.

### HelaMenu, HelaMenuItem, HelaMenuSeparator, HelaMenuPanel
```kotlin
Box {                                   // the Box is the anchor
    HelaIconButton(HelaIcons.More, "More actions", onClick = { open = true }, variant = HelaButtonVariant.Tertiary)
    HelaMenu(expanded = open, onDismissRequest = { open = false }) {
        HelaMenuItem("Rename", onClick = { open = false; rename() }, icon = HelaIcons.Settings)
        HelaMenuItem("Copy URL", onClick = { … }, icon = HelaIcons.Copy, trailing = { HelaKbd("⌘C") })
        HelaMenuSeparator()
        HelaMenuItem("Delete", onClick = { … }, icon = HelaIcons.Trash, destructive = true)
    }
}
```
- It opens below the anchor, start-aligned, and flips above when there's no room. It is 180 to 320dp wide.
- `HelaMenuItem(text, onClick, modifier, icon = null, enabled = true, destructive = false, selected = false, trailing = null)`.
- Close the menu yourself in each `onClick`.
- Put destructive items last, after a separator.
- `HelaMenuPanel { … }` is the surface alone, for previews.

### HelaCollapse
```kotlin
HelaCollapse(title, expanded, onExpandedChange, modifier) { content }
```
For an accordion that allows one open item at a time, hoist an `openIndex` and put a `HelaDivider()` between items.

## Overlays

### HelaModal / HelaModalPanel
```kotlin
if (confirm) HelaModal(
    onDismissRequest = { confirm = false },
    title = "Delete project",
    description = "This permanently deletes acme-web and all its deployments.",
    actions = {                                   // RowScope, SpaceBetween
        HelaButton("Cancel", onClick = { confirm = false }, variant = HelaButtonVariant.Secondary)
        HelaButton("Delete project", onClick = ::delete, variant = HelaButtonVariant.Error, loading = deleting)
    },
) { /* optional body: e.g. "type the name to confirm" HelaInput */ }
```
Cancel goes at the start and the confirming action at the end. Use `dismissOnClickOutside = false`
while a request is in flight.

### HelaBottomSheet / HelaBottomSheetPanel
```kotlin
if (open) HelaBottomSheet(onDismissRequest = { open = false }, title = "acme-web", description = "Production · Ready") {
    HelaMenuItem("Visit", onClick = { dismiss() }, icon = HelaIcons.ArrowRight)   // dismiss() animates out
}
```
- The content lambda has `HelaBottomSheetScope` as its receiver. Call `dismiss()` to slide the sheet
  out, which then calls `onDismissRequest`.
- `skipPartiallyExpanded = true` (default) suits short sheets. Set it false for tall, scrolling content.
- Handles drag, scrim tap, back and navigation-bar insets. `HelaBottomSheetPanel` is the surface for previews.
- On phones, prefer a sheet over a menu when there are more than 4 actions or the actions need explanation.

### HelaToast / HelaToastState / HelaToastHost
```kotlin
val toast = remember { HelaToastState() }
Box(Modifier.fillMaxSize()) {
    ScreenContent(onSaved = { toast.show("Saved", HelaNoteType.Success) })
    HelaToastHost(toast, Modifier.align(Alignment.BottomCenter).windowInsetsPadding(WindowInsets.safeDrawing))
}
toast.show(HelaToastData("Deployment ready", HelaNoteType.Success, actionLabel = "Visit", onAction = ::open))
```
- One toast at a time. A new one replaces the current one. It auto-dismisses after `durationMs`
  (default 4000ms).
- Use toasts for results of actions the user just took. Use `HelaNote` for anything that must persist.
- `HelaToast(message, …)` is the surface alone, for previews.

## Feedback

### HelaNote
```kotlin
HelaNote(text, modifier, type = HelaNoteType.Default, fill = true, action = null)
```
- Types: `Default, Info, Success, Warning, Error`. Each has an icon, so it reads without color.
- `fill = false` gives an outline only, which is quieter inside cards.
- `action = { HelaButton("Retry", onClick = …, size = HelaButtonSize.Small) }`.
- Error notes are announced (polite live region).

### HelaSpinner / HelaLoadingDots
```kotlin
HelaSpinner(modifier, size = 20.dp, color = Color.Unspecified)        // 12-bar Geist spinner
HelaLoadingDots(modifier, dotSize = 4.dp, color = Color.Unspecified)  // "thinking…"
```
Both take the current content color.

### HelaSkeleton
```kotlin
HelaSkeleton(modifier.size(…), shape = HelaShapes.small)
```
Size it exactly like the content it replaces, so there's no layout shift. Use `HelaShapes.full` for
avatars and `.large` for cards.

### HelaProgress / HelaGauge
```kotlin
HelaProgress(value: Float /*0..1*/, modifier, color = Color.Unspecified, height = 8.dp)
HelaGauge(value: Int /*0..100*/, modifier, size = 48.dp, showValue = true, color = Color.Unspecified)
```
The bar defaults to `textPrimary`. Pass `red.s700` when over a limit. The gauge colors itself
automatically: green below 50, amber below 80, red otherwise.

## Display

### HelaCard / HelaCardFooter
```kotlin
HelaCard(modifier, onClick = null, contentPadding = PaddingValues(HelaSpacing.s6)) { ColumnScope }
HelaCard(contentPadding = PaddingValues(0.dp)) {
    Column(Modifier.padding(HelaSpacing.s6)) { … }
    HelaCardFooter { HelaButton("Save", onClick = …, size = HelaButtonSize.Small) }  // end-aligned
}
```
A bordered card with no shadow and a 12dp radius. `onClick` makes the whole card tappable with the
flat tint. The footer is Vercel's settings-card pattern: put the explanation on the left and Save on
the right.

### HelaBadge
```kotlin
HelaBadge(text, modifier, variant = HelaBadgeVariant.Gray, subtle = false, size = HelaBadgeSize.Medium, icon = null)
```
- Variants: `Gray, Blue, Red, Amber, Green, Inverted`. Sizes: Small 20, Medium 24, Large 32.
- Prefer `subtle = true` in lists. Keep solid badges for the single thing that must pop.

### HelaStatusDot
```kotlin
HelaStatusDot(status: HelaStatus, modifier, showLabel = false)
```
- `HelaStatus`: `Queued` (gray), `Building` (amber), `Ready` (green), `Error` (red), `Canceled` (light gray).
- Without a label it still has a content description.

### HelaAvatar / HelaAvatarGroup
```kotlin
HelaAvatar(name: String?, modifier, painter = null, size = 32.dp)  // painter → initials → user icon
HelaAvatarGroup(names: List<String>, modifier, max = 4, size = 32.dp)  // overlap + "+N"
```

### HelaSnippet / HelaKbd
```kotlin
HelaSnippet(text, modifier, prompt = "$", inverted = false)   // mono, copy button → check for 2s
HelaKbd("⌘"); HelaKbd("K")
```
The `prompt` is shown but not copied. Use `prompt = null` for URLs and IDs.

### HelaEmptyState
```kotlin
HelaEmptyState(title, modifier, description = null, icon = HelaIcons.Inbox, action = null)
```
Give it one clear next step as the `action`. Inside a card, use `contentPadding = PaddingValues(0.dp)`.

### HelaDivider
```kotlin
HelaDivider(modifier, vertical = false)
```
A 1dp `border` color line. Prefer spacing, and use a divider only when spacing alone is ambiguous.

## Previews

```kotlin
@HelaPreviews                       // Light + Dark, 360dp wide
@Composable
private fun SettingsScreenPreview() = HelaPreviewSurface {   // HelaTheme + background + 16dp padding + 12dp spacing
    HelaPreviewLabel("Error")       // small mono caption to label a state
    SettingsContent(state = SettingsUiState.Error("Network unreachable"), onRetry = {})
}
```
- For a full screen with its own background and padding, skip `HelaPreviewSurface` and use
  `HelaTheme(darkTheme = isSystemInDarkTheme()) { Screen(…) }`.
- Previews are `private`, named `<Thing><State>Preview`, and use fake data. Never use a ViewModel.
