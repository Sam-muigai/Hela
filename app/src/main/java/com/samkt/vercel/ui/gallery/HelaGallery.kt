package com.samkt.vercel.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.samkt.hela.components.HelaAvatarGroup
import com.samkt.hela.components.HelaBadge
import com.samkt.hela.components.HelaBadgeVariant
import com.samkt.hela.components.HelaBottomSheet
import com.samkt.hela.components.HelaButton
import com.samkt.hela.components.HelaButtonSize
import com.samkt.hela.components.HelaButtonVariant
import com.samkt.hela.components.HelaCard
import com.samkt.hela.components.HelaCheckbox
import com.samkt.hela.components.HelaCollapse
import com.samkt.hela.components.HelaDivider
import com.samkt.hela.components.HelaEmptyState
import com.samkt.hela.components.HelaGauge
import com.samkt.hela.components.HelaIcon
import com.samkt.hela.components.HelaIconButton
import com.samkt.hela.components.HelaIcons
import com.samkt.hela.components.HelaInput
import com.samkt.hela.components.HelaKbd
import com.samkt.hela.components.HelaLoadingDots
import com.samkt.hela.components.HelaMenu
import com.samkt.hela.components.HelaMenuItem
import com.samkt.hela.components.HelaMenuSeparator
import com.samkt.hela.components.HelaModal
import com.samkt.hela.components.HelaNote
import com.samkt.hela.components.HelaNoteType
import com.samkt.hela.components.HelaProgress
import com.samkt.hela.components.HelaRadioGroup
import com.samkt.hela.components.HelaSegmentedControl
import com.samkt.hela.components.HelaSelect
import com.samkt.hela.components.HelaSkeleton
import com.samkt.hela.components.HelaSnippet
import com.samkt.hela.components.HelaSpinner
import com.samkt.hela.components.HelaStatus
import com.samkt.hela.components.HelaStatusDot
import com.samkt.hela.components.HelaSwitch
import com.samkt.hela.components.HelaTabs
import com.samkt.hela.components.HelaText
import com.samkt.hela.components.HelaTextarea
import com.samkt.hela.components.HelaToastData
import com.samkt.hela.components.HelaToastHost
import com.samkt.hela.components.HelaToastState
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.unit.dp

/** Every Hela component on one scrolling screen, with a light/dark switch. A living style guide. */
@Composable
fun HelaGallery(darkTheme: Boolean, onDarkThemeChange: (Boolean) -> Unit) {
    val toast = remember { HelaToastState() }
    Box(
        Modifier
            .fillMaxSize()
            .background(HelaTheme.colors.background100),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .padding(HelaSpacing.s4),
            verticalArrangement = Arrangement.spacedBy(HelaSpacing.s8),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                HelaText("Design system", Modifier.weight(1f), style = HelaTheme.typography.heading24)
                HelaSwitch(darkTheme, onDarkThemeChange, label = "Dark")
            }
            ButtonsSection()
            InputsSection()
            SelectionSection()
            NavigationSection()
            FeedbackSection(toast)
            DisplaySection()
            LoadingSection()
            Spacer(Modifier.height(HelaSpacing.s16))
        }
        HelaToastHost(toast, Modifier.align(Alignment.BottomCenter).windowInsetsPadding(WindowInsets.safeDrawing))
    }
}

@Composable
private fun Section(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(HelaSpacing.s3)) {
        HelaText(title, Modifier.semantics { heading() }, style = HelaTheme.typography.heading16)
        HelaDivider()
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ButtonsSection() = Section("Buttons") {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2), verticalArrangement = Arrangement.spacedBy(HelaSpacing.s2)) {
        HelaButtonVariant.entries.forEach { HelaButton(it.name, onClick = {}, variant = it) }
        HelaButton("Loading", onClick = {}, loading = true)
        HelaButton("Disabled", onClick = {}, enabled = false)
        HelaIconButton(HelaIcons.Settings, "Settings", onClick = {})
    }
    Row(horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2), verticalAlignment = Alignment.CenterVertically) {
        HelaButtonSize.entries.forEach { HelaButton(it.name, onClick = {}, size = it, variant = HelaButtonVariant.Secondary) }
    }
}

@Composable
private fun InputsSection() = Section("Inputs") {
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var region by remember { mutableStateOf<String?>(null) }
    HelaInput(name, { name = it }, label = "Project name", placeholder = "my-app", prefix = { HelaIcon(HelaIcons.Search, null) })
    HelaInput("bad slug", {}, label = "Slug", error = "Slugs can't contain spaces.")
    HelaSelect(listOf("iad1", "fra1", "hnd1", "cpt1"), region, { region = it }, label = "Region")
    HelaTextarea(bio, { bio = it }, label = "Description", placeholder = "Tell us about it", maxLength = 140)
}

@Composable
private fun SelectionSection() = Section("Selection") {
    var a by remember { mutableStateOf(true) }
    var b by remember { mutableStateOf(false) }
    var on by remember { mutableStateOf(true) }
    var plan by remember { mutableStateOf("Pro") }
    var period by remember { mutableIntStateOf(0) }
    HelaCheckbox(a, { a = it }, label = "Send me product updates")
    HelaCheckbox(b, { b = it }, label = "Unchecked")
    HelaSwitch(on, { on = it }, label = "Preview deployments")
    HelaRadioGroup(listOf("Hobby", "Pro", "Enterprise"), plan, { plan = it })
    HelaSegmentedControl(listOf("Monthly", "Yearly"), period, { period = it }, fillWidth = true)
}

@Composable
private fun NavigationSection() = Section("Navigation") {
    var tab by remember { mutableIntStateOf(0) }
    var open by remember { mutableIntStateOf(-1) }
    var menu by remember { mutableStateOf(false) }
    HelaTabs(listOf("Overview", "Deployments", "Analytics", "Settings"), tab, { tab = it })
    Box {
        HelaButton(
            "Actions",
            onClick = { menu = true },
            variant = HelaButtonVariant.Secondary,
            suffix = { HelaIcon(HelaIcons.ChevronDown, null) },
        )
        HelaMenu(menu, onDismissRequest = { menu = false }) {
            HelaMenuItem("Visit", onClick = { menu = false }, icon = HelaIcons.ArrowRight)
            HelaMenuItem("Copy URL", onClick = { menu = false }, icon = HelaIcons.Copy)
            HelaMenuSeparator()
            HelaMenuItem("Delete", onClick = { menu = false }, icon = HelaIcons.Trash, destructive = true)
        }
    }
    listOf("What is a preview?" to "A unique URL for every push.", "Custom domains?" to "Yes, in project settings.")
        .forEachIndexed { i, (q, a) ->
            HelaCollapse(q, open == i, { open = if (it) i else -1 }) { HelaText(a, color = HelaTheme.colors.textSecondary) }
        }
}

@Composable
private fun FeedbackSection(toast: HelaToastState) = Section("Feedback") {
    var modal by remember { mutableStateOf(false) }
    var sheet by remember { mutableStateOf(false) }
    HelaNote("Inline info note.", type = HelaNoteType.Info)
    HelaNote("Something failed.", type = HelaNoteType.Error)
    Row(horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2)) {
        HelaButton("Show toast", variant = HelaButtonVariant.Secondary, onClick = {
            toast.show(HelaToastData("Deployment ready", HelaNoteType.Success, actionLabel = "Visit"))
        })
        HelaButton("Open modal", variant = HelaButtonVariant.Secondary, onClick = { modal = true })
        HelaButton("Open sheet", variant = HelaButtonVariant.Secondary, onClick = { sheet = true })
    }
    if (sheet) {
        HelaBottomSheet(onDismissRequest = { sheet = false }, title = "acme-web", description = "Production · Ready") {
            HelaMenuItem("Visit", onClick = { dismiss() }, icon = HelaIcons.ArrowRight)
            HelaMenuItem("Copy URL", onClick = {
                toast.show("Copied to clipboard")
                dismiss()
            }, icon = HelaIcons.Copy)
            HelaMenuSeparator()
            HelaMenuItem("Delete", onClick = { dismiss() }, icon = HelaIcons.Trash, destructive = true)
        }
    }
    if (modal) {
        HelaModal(
            onDismissRequest = { modal = false },
            title = "Delete project",
            description = "This can't be undone.",
            actions = {
                HelaButton("Cancel", onClick = { modal = false }, variant = HelaButtonVariant.Secondary)
                HelaButton("Delete", onClick = { modal = false }, variant = HelaButtonVariant.Error)
            },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DisplaySection() = Section("Display") {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2), verticalArrangement = Arrangement.spacedBy(HelaSpacing.s2)) {
        HelaBadgeVariant.entries.forEach { HelaBadge(it.name, variant = it, subtle = true) }
    }
    HelaAvatarGroup(listOf("Ada Lovelace", "Grace Hopper", "Alan Turing", "Linus T", "Ken T"))
    Row(horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s4)) {
        HelaStatus.entries.forEach { HelaStatusDot(it) }
    }
    HelaCard(Modifier.fillMaxWidth(), onClick = {}) {
        HelaText("acme-web", style = HelaTheme.typography.heading16)
        HelaText("acme-web.vercel.app", color = HelaTheme.colors.textSecondary)
    }
    HelaSnippet("npm i -g vercel")
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) { HelaKbd("⌘"); HelaKbd("K") }
    HelaEmptyState("No deployments yet", description = "Push to Git to create one.")
}

@Composable
private fun LoadingSection() = Section("Loading & progress") {
    var progress by remember { mutableFloatStateOf(0.4f) }
    Row(horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s4), verticalAlignment = Alignment.CenterVertically) {
        HelaSpinner()
        HelaLoadingDots()
        HelaGauge((progress * 100).toInt())
        HelaButton("+10%", onClick = { progress = (progress + 0.1f).let { if (it > 1.001f) 0f else it } }, size = HelaButtonSize.Small, variant = HelaButtonVariant.Secondary)
    }
    HelaProgress(progress)
    Row(horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s3), verticalAlignment = Alignment.CenterVertically) {
        HelaSkeleton(Modifier.size(40.dp), shape = HelaShapes.full)
        HelaSkeleton(Modifier.fillMaxWidth().height(16.dp))
    }
}

@HelaPreviews
@Composable
private fun HelaGalleryPreview() {
    val dark = isSystemInDarkTheme()
    HelaTheme(darkTheme = dark) { HelaGallery(dark, {}) }
}
