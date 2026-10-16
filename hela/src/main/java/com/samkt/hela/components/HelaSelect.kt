package com.samkt.hela.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.foundation.helaFocusRing
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

/**
 * Geist select: an input-styled trigger that opens a [HelaMenu] of options, the same width as the
 * trigger. Sizes follow [HelaInputSize].
 *
 * [optionDescription] adds a line of secondary text under an option in the menu, for choices
 * that need explaining ("Tax is charged on the discounted price"). Return null to skip one.
 */
@Composable
fun <T> HelaSelect(
    options: List<T>,
    selected: T?,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Select…",
    error: String? = null,
    size: HelaInputSize = HelaInputSize.Medium,
    enabled: Boolean = true,
    optionLabel: (T) -> String = { it.toString() },
    optionDescription: (T) -> String? = { null },
) {
    val c = HelaTheme.colors
    val t = HelaTheme.typography
    var open by remember { mutableStateOf(false) }
    var triggerWidth by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val source = remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val isError = error != null
    val border by animateColorAsState(
        when {
            !enabled -> c.border
            isError -> c.red.s700
            open || focused -> c.borderActive
            else -> c.border
        },
        tween(HelaMotion.FAST_MS), label = "border",
    )
    val chevron by animateFloatAsState(if (open) 180f else 0f, tween(HelaMotion.FAST_MS), label = "chevron")

    Column(modifier) {
        if (label != null) {
            HelaText(label, style = t.label13, color = c.textSecondary)
            Spacer(Modifier.height(HelaSpacing.s2))
        }
        Box {
            Row(
                Modifier
                    .fillMaxWidth()
                    .onSizeChanged { triggerWidth = it.width }
                    .helaFocusRing(visible = focused, shape = HelaShapes.small, color = c.focusRing)
                    .height(size.height)
                    .background(if (enabled) c.surface else c.surfaceSubtle, HelaShapes.small)
                    .border(HelaBorder.hairline, border, HelaShapes.small)
                    .clickable(source, indication = null, enabled = enabled, role = Role.DropdownList) { open = true }
                    .semantics { stateDescription = if (open) "Expanded" else "Collapsed" }
                    .padding(horizontal = size.horizontalPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2),
            ) {
                HelaText(
                    selected?.let(optionLabel) ?: placeholder,
                    Modifier.weight(1f),
                    style = t.compact,
                    color = when {
                        !enabled -> c.textDisabled
                        selected == null -> c.gray.s700
                        else -> c.textPrimary
                    },
                    maxLines = 1,
                )
                HelaIcon(HelaIcons.ChevronDown, null, Modifier.rotate(chevron), tint = c.textSecondary)
            }
            HelaMenu(
                expanded = open,
                onDismissRequest = { open = false },
                modifier = Modifier.width(with(density) { triggerWidth.toDp() } + MenuShadowInset * 2),
            ) {
                options.forEach { option ->
                    HelaMenuItem(
                        optionLabel(option),
                        onClick = {
                            onSelect(option)
                            open = false
                        },
                        selected = option == selected,
                        description = optionDescription(option),
                    )
                }
            }
        }
        if (error != null) {
            Spacer(Modifier.height(HelaSpacing.s2))
            HelaText(error, style = t.label13, color = c.error)
        }
    }
}

@HelaPreviews
@Composable
private fun HelaSelectPreview() = HelaPreviewSurface {
    var region by remember { mutableStateOf<String?>("Frankfurt (fra1)") }
    val regions = listOf("Washington, D.C. (iad1)", "Frankfurt (fra1)", "Tokyo (hnd1)", "Cape Town (cpt1)")
    HelaSelect(regions, region, { region = it }, label = "Region")
    HelaSelect(regions, null, {}, label = "Empty", placeholder = "Choose a region")
    HelaSelect(regions, null, {}, label = "Error", error = "Pick a region.")
    HelaSelect(regions, regions[0], {}, label = "Disabled", enabled = false, size = HelaInputSize.Small)
}

@HelaPreviews
@Composable
private fun HelaSelectDescriptionsPreview() = HelaPreviewSurface {
    HelaMenuPanel {
        HelaMenuItem("After discount", onClick = {}, selected = true, description = "Tax is charged on the discounted price")
        HelaMenuItem("Before discount", onClick = {}, description = "Tax is charged on the full price")
    }
}
