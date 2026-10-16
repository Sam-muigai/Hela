package com.samkt.hela.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.foundation.helaFocusRing
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

/** A single radio. Usually used through [HelaRadioGroup]. */
@Composable
fun HelaRadio(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val c = HelaTheme.colors
    val dot by animateDpAsState(if (selected) 8.dp else 0.dp, tween(HelaMotion.FAST_MS), label = "dot")
    val ink = if (enabled) c.textPrimary else c.textDisabled

    Row(
        modifier = modifier.selectable(
            selected = selected,
            interactionSource = source,
            indication = null,
            enabled = enabled,
            role = Role.RadioButton,
            onClick = onClick,
        ),
        horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .helaFocusRing(visible = focused, shape = HelaShapes.full, color = c.focusRing)
                .size(16.dp)
                .background(if (enabled) c.surface else c.gray.s100, HelaShapes.full)
                .border(HelaBorder.hairline, if (selected) ink else c.gray.s700, HelaShapes.full),
            contentAlignment = Alignment.Center,
        ) {
            if (dot > 0.dp) Box(Modifier.size(dot).background(ink, HelaShapes.full))
        }
        if (label != null) HelaText(label, color = ink)
    }
}

/** A vertical list of radios with one selected value. */
@Composable
fun <T> HelaRadioGroup(
    options: List<T>,
    selected: T?,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    optionLabel: (T) -> String = { it.toString() },
) {
    Column(modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(HelaSpacing.s3)) {
        options.forEach { option ->
            HelaRadio(
                selected = option == selected,
                onClick = { onSelect(option) },
                label = optionLabel(option),
                enabled = enabled,
            )
        }
    }
}

@HelaPreviews
@Composable
private fun HelaRadioPreview() = HelaPreviewSurface {
    var plan by remember { mutableStateOf("Pro") }
    HelaRadioGroup(listOf("Hobby", "Pro", "Enterprise"), plan, { plan = it })
    HelaRadio(selected = true, onClick = {}, label = "Disabled selected", enabled = false)
    HelaRadio(selected = false, onClick = {}, label = "Disabled", enabled = false)
}
