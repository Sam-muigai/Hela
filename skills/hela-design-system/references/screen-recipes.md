# Screen recipes

Start from the closest recipe. They all follow the same skeleton: a root `Box` (for the toast host),
insets before scroll, a 16dp gutter, sections spaced 24 to 32dp, and a state type with a preview per
state. All code here compiles against the current Hela API.

## Contents
1. Screen skeleton + UiState
2. Resource list (Vercel "Deployments" list)
3. Settings page (cards with footer Save)
4. Form with validation and async submit
5. Detail screen with tabs and overflow menu
6. Destructive confirmation
7. Previews for every state

---

## 1. Screen skeleton + UiState

```kotlin
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Error(val message: String) : UiState<Nothing>
    data class Content<T>(val data: T) : UiState<T>
}

@Composable
fun HelaScreen(
    title: String,
    modifier: Modifier = Modifier,
    toast: HelaToastState = remember { HelaToastState() },
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier.fillMaxSize().background(HelaTheme.colors.background100)) {
        Column(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing) // before scroll, so content doesn't slide under bars
                .verticalScroll(rememberScrollState())
                .padding(HelaSpacing.s4),
            verticalArrangement = Arrangement.spacedBy(HelaSpacing.s6),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                HelaText(title, Modifier.weight(1f).semantics { heading() }, style = HelaTheme.typography.heading24)
                Row(horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2), content = actions)
            }
            content()
        }
        HelaToastHost(toast, Modifier.align(Alignment.BottomCenter).windowInsetsPadding(WindowInsets.safeDrawing))
    }
}
```
For long or unbounded lists, swap the `verticalScroll` Column for a `LazyColumn` with
`contentPadding = PaddingValues(HelaSpacing.s4)` and `verticalArrangement = Arrangement.spacedBy(…)`.

## 2. Resource list (Vercel "Deployments" list)

The pattern is a bordered container of rows separated by hairlines. Each row has a status dot, a mono
identifier, metadata in `textSecondary`, and an overflow menu.

```kotlin
data class Deployment(val id: String, val branch: String, val status: HelaStatus, val age: String)

@Composable
fun DeploymentList(state: UiState<List<Deployment>>, onRetry: () -> Unit, onOpen: (Deployment) -> Unit) {
    when (state) {
        UiState.Loading -> HelaCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(0.dp)) {
            repeat(3) { i ->
                if (i > 0) HelaDivider()
                Row(Modifier.padding(HelaSpacing.s4), horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s3)) {
                    HelaSkeleton(Modifier.size(10.dp), shape = HelaShapes.full)
                    Column(verticalArrangement = Arrangement.spacedBy(HelaSpacing.s2)) {
                        HelaSkeleton(Modifier.size(width = 140.dp, height = 14.dp))
                        HelaSkeleton(Modifier.size(width = 90.dp, height = 12.dp))
                    }
                }
            }
        }
        is UiState.Error -> HelaNote(
            state.message,
            type = HelaNoteType.Error,
            action = { HelaButton("Retry", onClick = onRetry, size = HelaButtonSize.Small, variant = HelaButtonVariant.Secondary) },
        )
        is UiState.Content -> if (state.data.isEmpty()) {
            HelaCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(0.dp)) {
                HelaEmptyState("No deployments yet", description = "Push to your Git repository to create one.")
            }
        } else {
            HelaCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(0.dp)) {
                state.data.forEachIndexed { i, d ->
                    if (i > 0) HelaDivider()
                    DeploymentRow(d, onClick = { onOpen(d) })
                }
            }
        }
    }
}

@Composable
private fun DeploymentRow(d: Deployment, onClick: () -> Unit) {
    var menu by remember { mutableStateOf(false) }
    Row(
        Modifier.fillMaxWidth().heightIn(min = 56.dp).clickable(onClick = onClick) // HelaIndication = flat tint
            .padding(start = HelaSpacing.s4, end = HelaSpacing.s2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s3),
    ) {
        HelaStatusDot(d.status)
        Column(Modifier.weight(1f).padding(vertical = HelaSpacing.s3)) {
            HelaText(d.id, style = HelaTheme.typography.mono13, maxLines = 1)
            HelaText("${d.branch} · ${d.age}", style = HelaTheme.typography.label13,
                color = HelaTheme.colors.textSecondary, maxLines = 1)
        }
        Box {
            HelaIconButton(HelaIcons.More, "More actions for ${d.id}", onClick = { menu = true },
                variant = HelaButtonVariant.Tertiary, size = HelaButtonSize.Small)
            HelaMenu(menu, onDismissRequest = { menu = false }) {
                HelaMenuItem("Visit", onClick = { menu = false }, icon = HelaIcons.ArrowRight)
                HelaMenuItem("Copy URL", onClick = { menu = false }, icon = HelaIcons.Copy)
                HelaMenuSeparator()
                HelaMenuItem("Cancel deployment", onClick = { menu = false }, destructive = true,
                    enabled = d.status == HelaStatus.Building)
            }
        }
    }
}
```

## 3. Settings page (cards with footer Save)

This is Vercel's settings pattern. Each setting gets its own card: a title, one sentence of
explanation, the control, and a footer with a hint on the left and Save on the right. Save is enabled
only when the value changed.

```kotlin
@Composable
fun SettingCard(
    title: String,
    description: String,
    hint: String,
    canSave: Boolean,
    saving: Boolean,
    onSave: () -> Unit,
    control: @Composable () -> Unit,
) {
    HelaCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(0.dp)) {
        Column(Modifier.padding(HelaSpacing.s6), verticalArrangement = Arrangement.spacedBy(HelaSpacing.s3)) {
            HelaText(title, style = HelaTheme.typography.heading16)
            HelaText(description, color = HelaTheme.colors.textSecondary)
            control()
        }
        HelaCardFooter {
            HelaText(hint, Modifier.weight(1f), style = HelaTheme.typography.label13, color = HelaTheme.colors.textSecondary)
            HelaButton("Save", onClick = onSave, size = HelaButtonSize.Small, enabled = canSave, loading = saving)
        }
    }
}
```
`HelaCardFooter` is a `Row`, so `Modifier.weight(1f)` works directly inside it.

Usage:
```kotlin
var name by remember { mutableStateOf(saved) }
SettingCard(
    title = "Project name",
    description = "Used to identify your project on the dashboard and in URLs.",
    hint = "Please use 48 characters at most.",
    canSave = name != saved && name.isNotBlank(),
    saving = saving,
    onSave = { save(name) },
) { HelaInput(name, { name = it }) }
```
Toggles that apply instantly (`HelaSwitch`) don't need a Save button. Confirm success with a toast.

## 4. Form with validation and async submit

```kotlin
@Composable
fun NewProjectForm(onCreate: suspend (String, String) -> Result<Unit>) {
    var name by remember { mutableStateOf("") }
    var region by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var submitError by remember { mutableStateOf<String?>(null) }
    var submitting by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(verticalArrangement = Arrangement.spacedBy(HelaSpacing.s4)) {
        submitError?.let { HelaNote(it, type = HelaNoteType.Error) }
        HelaInput(
            name, { name = it; nameError = null },              // clear the error as soon as they edit
            label = "Project name", placeholder = "my-app",
            helper = "Lowercase letters, numbers and dashes.", error = nameError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        HelaSelect(listOf("iad1", "fra1", "hnd1"), region, { region = it }, label = "Region")
        HelaButton(
            "Create project",
            modifier = Modifier.fillMaxWidth(),
            size = HelaButtonSize.Large,
            loading = submitting,
            enabled = region != null,
            onClick = {
                nameError = when {
                    name.isBlank() -> "Enter a project name."
                    !name.matches(Regex("[a-z0-9-]+")) -> "Use lowercase letters, numbers and dashes only."
                    else -> null
                }
                if (nameError == null) scope.launch {
                    submitting = true
                    submitError = onCreate(name, region!!).exceptionOrNull()?.message
                    submitting = false
                }
            },
        )
    }
}
```
Rules:
- Validate on submit, show errors per field, and keep the user's input.
- Field-level errors go in `error =`. Server or global errors go in a `HelaNote` at the top.
- Disable submit only for missing required choices. Don't disable it as a way to hide validation
  messages. The button shows `loading` while in flight.
- Free text that is prose (notes, descriptions) goes in `HelaTextarea`, which capitalizes sentences
  by default. Pass `KeyboardOptions.Default` for addresses, codes and IDs.

### Date field

Hela has no date text field. Show the date on a field-like trigger and pick it in a modal:

```kotlin
var dueDate by rememberSaveable { mutableStateOf<Long?>(null) }   // UTC-midnight millis
var picking by remember { mutableStateOf(false) }

Column(verticalArrangement = Arrangement.spacedBy(HelaSpacing.s2)) {
    HelaText("Due date", style = HelaTheme.typography.label13, color = HelaTheme.colors.textSecondary)
    HelaButton(
        dueDate?.let(HelaDates::format) ?: "Pick a date",
        onClick = { picking = true },
        modifier = Modifier.fillMaxWidth(),
        variant = HelaButtonVariant.Secondary,
        suffix = { HelaIcon(HelaIcons.ChevronDown, null) },
    )
}
if (picking) HelaDatePickerModal(
    onDismissRequest = { picking = false },
    onConfirm = { dueDate = it; picking = false },
    title = "Due date",
    selectedDateMillis = dueDate,
    minDateMillis = HelaDates.todayUtcMillis(),          // no due dates in the past
)
```
- Keep the domain model in `LocalDate` if you use `java.time`, and convert at the UI edge:
  `LocalDate.ofEpochDay(millis / 86_400_000)` and `date.toEpochDay() * 86_400_000`.
- Bound the range with `minDateMillis`/`maxDateMillis` rather than validating after the fact.
- For a date chosen inside a sheet or on a page with room, use `HelaCalendar` inline instead of the modal.

## 5. Detail screen with tabs and overflow menu

```kotlin
@Composable
fun ProjectDetail(project: String, onDelete: () -> Unit) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var menu by remember { mutableStateOf(false) }
    var confirm by remember { mutableStateOf(false) }
    HelaScreen(project, actions = {
        HelaButton("Visit", onClick = {}, variant = HelaButtonVariant.Secondary, size = HelaButtonSize.Small)
        Box {
            HelaIconButton(HelaIcons.More, "More", onClick = { menu = true }, size = HelaButtonSize.Small)
            HelaMenu(menu, { menu = false }) {
                HelaMenuItem("Settings", onClick = { menu = false }, icon = HelaIcons.Settings)
                HelaMenuSeparator()
                HelaMenuItem("Delete project", onClick = { menu = false; confirm = true },
                    icon = HelaIcons.Trash, destructive = true)
            }
        }
    }) {
        HelaTabs(listOf("Overview", "Deployments", "Logs", "Settings"), tab, { tab = it })
        when (tab) {
            0 -> HelaText("Overview content")
            else -> HelaEmptyState("Nothing here yet")
        }
    }
    if (confirm) DeleteProjectModal(project, onDismiss = { confirm = false }, onConfirm = onDelete)
}
```

## 6. Destructive confirmation

Vercel's pattern: name the thing being deleted, say what will be lost, and for irreversible
high-impact actions make the user type the name.

```kotlin
@Composable
fun DeleteProjectModal(project: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    var typed by remember { mutableStateOf("") }
    HelaModal(
        onDismissRequest = onDismiss,
        title = "Delete project",
        description = "This permanently deletes $project, its deployments and domains. This can't be undone.",
        actions = {
            HelaButton("Cancel", onClick = onDismiss, variant = HelaButtonVariant.Secondary)
            HelaButton("Delete project", onClick = onConfirm, variant = HelaButtonVariant.Error, enabled = typed == project)
        },
    ) {
        HelaInput(typed, { typed = it }, label = "Type $project to confirm", placeholder = project)
    }
}
```

## 7. Previews for every state

Keep composables stateless (pass `state`) so each state previews with fake data:

```kotlin
private val fake = listOf(
    Deployment("acme-7f3k2", "main", HelaStatus.Ready, "2m ago"),
    Deployment("acme-a91bc", "feat/auth", HelaStatus.Building, "5m ago"),
    Deployment("acme-0c2de", "fix/typo", HelaStatus.Error, "1h ago"),
)

@HelaPreviews @Composable
private fun DeploymentListContentPreview() = HelaPreviewSurface { DeploymentList(UiState.Content(fake), {}, {}) }

@HelaPreviews @Composable
private fun DeploymentListLoadingPreview() = HelaPreviewSurface { DeploymentList(UiState.Loading, {}, {}) }

@HelaPreviews @Composable
private fun DeploymentListEmptyPreview() = HelaPreviewSurface { DeploymentList(UiState.Content(emptyList()), {}, {}) }

@HelaPreviews @Composable
private fun DeploymentListErrorPreview() = HelaPreviewSurface {
    DeploymentList(UiState.Error("Couldn't load deployments. Check your connection."), {}, {})
}
```
