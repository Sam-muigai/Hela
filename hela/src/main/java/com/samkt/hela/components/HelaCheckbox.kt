package com.samkt.hela.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.foundation.helaFocusRing
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaSpacing

private val BoxShape = RoundedCornerShape(4.dp)

/**
 * Geist checkbox: a 16dp square, monochrome when checked. The whole row (box + [label]) is the
 * touch target. [indeterminate] shows a dash, for "some children selected".
 */
@Composable
fun HelaCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    indeterminate: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val c = HelaTheme.colors
    val on = checked || indeterminate
    val state = when {
        indeterminate -> ToggleableState.Indeterminate
        checked -> ToggleableState.On
        else -> ToggleableState.Off
    }
    val fill by animateColorAsState(
        when {
            !enabled && on -> c.gray.s600
            !enabled -> c.gray.s100
            on -> c.inverseSurface
            else -> c.surface
        },
        tween(HelaMotion.FAST_MS), label = "fill",
    )
    val border = when {
        on -> Color.Transparent
        !enabled -> c.gray.s400
        else -> c.gray.s700
    }

    Row(
        modifier = modifier.triStateToggleable(
            state = state,
            interactionSource = source,
            indication = null,
            enabled = enabled,
            role = Role.Checkbox,
            onClick = { onCheckedChange(!checked) },
        ),
        horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .helaFocusRing(visible = focused, shape = BoxShape, color = c.focusRing)
                .size(16.dp)
                .background(fill, BoxShape)
                .border(HelaBorder.hairline, border, BoxShape),
            contentAlignment = Alignment.Center,
        ) {
            if (on) {
                HelaIcon(
                    if (indeterminate) HelaIcons.Minus else HelaIcons.Check,
                    contentDescription = null,
                    size = 12.dp,
                    tint = c.onInverseSurface,
                )
            }
        }
        if (label != null) {
            HelaText(label, color = if (enabled) c.textPrimary else c.textDisabled)
        }
    }
}

@HelaPreviews
@Composable
private fun HelaCheckboxPreview() = HelaPreviewSurface {
    var a by remember { mutableStateOf(true) }
    HelaCheckbox(a, { a = it }, label = "Checked (tap me)")
    HelaCheckbox(false, {}, label = "Unchecked")
    HelaCheckbox(false, {}, label = "Indeterminate", indeterminate = true)
    HelaCheckbox(true, {}, label = "Disabled checked", enabled = false)
    HelaCheckbox(false, {}, label = "Disabled", enabled = false)
}
