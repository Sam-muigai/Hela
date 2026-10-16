package com.samkt.hela.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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

private val TrackWidth = 36.dp
private val TrackHeight = 20.dp
private val Thumb = 16.dp
private val Inset = 2.dp

/**
 * On/off switch (Geist's `Toggle`). Monochrome when on. For choosing between two or more named
 * options use [HelaSegmentedControl] instead.
 */
@Composable
fun HelaSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val c = HelaTheme.colors
    val spec = tween<Color>(HelaMotion.DEFAULT_MS)
    val track by animateColorAsState(
        when {
            !enabled -> c.gray.s100
            checked -> c.inverseSurface
            else -> c.gray.s200
        },
        spec, label = "track",
    )
    val thumbX by animateDpAsState(
        if (checked) TrackWidth - Thumb - Inset * 2 else 0.dp,
        tween(HelaMotion.DEFAULT_MS, easing = HelaMotion.easeOut), label = "thumb",
    )
    val thumbColor = if (checked && enabled) c.onInverseSurface else if (c.isDark) c.gray.s1000 else Color.White

    Row(
        modifier = modifier.toggleable(
            value = checked,
            interactionSource = source,
            indication = null,
            enabled = enabled,
            role = Role.Switch,
            onValueChange = onCheckedChange,
        ),
        horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .helaFocusRing(visible = focused, shape = HelaShapes.full, color = c.focusRing)
                .size(TrackWidth, TrackHeight)
                .background(track, HelaShapes.full)
                .border(HelaBorder.hairline, if (checked) Color.Transparent else c.border, HelaShapes.full)
                .padding(Inset),
            contentAlignment = Alignment.CenterStart,
        ) {
            Box(
                Modifier
                    .offset(x = thumbX)
                    .size(Thumb)
                    .shadow(1.dp, HelaShapes.full)
                    .background(thumbColor, HelaShapes.full),
            )
        }
        if (label != null) HelaText(label, color = if (enabled) c.textPrimary else c.textDisabled)
    }
}

@HelaPreviews
@Composable
private fun HelaSwitchPreview() = HelaPreviewSurface {
    var on by remember { mutableStateOf(true) }
    HelaSwitch(on, { on = it }, label = "Preview deployments")
    HelaSwitch(false, {}, label = "Off")
    HelaSwitch(true, {}, label = "Disabled on", enabled = false)
    HelaSwitch(false, {}, label = "Disabled off", enabled = false)
}
