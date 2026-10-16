package com.samkt.hela.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewLabel
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

/**
 * Geist tabs: text labels on a hairline, the selected one underlined in primary text color.
 * Scrolls horizontally when the labels don't fit.
 */
@Composable
fun HelaTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val c = HelaTheme.colors
    Box(modifier.fillMaxWidth()) {
        Box(Modifier.align(Alignment.BottomStart).fillMaxWidth().height(HelaBorder.hairline).background(c.border))
        Row(
            Modifier
                .horizontalScroll(rememberScrollState())
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s6),
        ) {
            tabs.forEachIndexed { index, title ->
                val selected = index == selectedIndex
                val color by animateColorAsState(
                    if (selected) c.textPrimary else c.textSecondary,
                    tween(HelaMotion.FAST_MS), label = "tab",
                )
                val indicator = if (selected) c.textPrimary else Color.Transparent
                Box(
                    Modifier
                        .selectable(selected, role = Role.Tab, onClick = { onSelect(index) })
                        .drawBehind {
                            val stroke = 2.dp.toPx()
                            drawLine(
                                indicator,
                                Offset(0f, size.height - stroke / 2),
                                Offset(size.width, size.height - stroke / 2),
                                stroke,
                            )
                        }
                        .padding(vertical = HelaSpacing.s3),
                ) {
                    HelaText(title, style = HelaTheme.typography.label14, color = color, maxLines = 1)
                }
            }
        }
    }
}

/**
 * Geist's `Switch`: a segmented control for picking one of a few named options (e.g. "Monthly /
 * Yearly", view modes). For on/off use [HelaSwitch].
 *
 * @param fillWidth stretch to the parent's width with equal segments.
 */
@Composable
fun HelaSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    fillWidth: Boolean = false,
    height: Dp = 40.dp,
) {
    val c = HelaTheme.colors
    val inner = RoundedCornerShape(4.dp)
    Row(
        modifier
            .then(if (fillWidth) Modifier.fillMaxWidth() else Modifier)
            .height(height)
            .background(c.gray.s100, HelaShapes.small)
            .border(HelaBorder.hairline, c.border, HelaShapes.small)
            .padding(3.dp)
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            val bg by animateColorAsState(
                if (selected) c.background100 else Color.Transparent,
                tween(HelaMotion.FAST_MS), label = "seg",
            )
            Box(
                Modifier
                    .then(if (fillWidth) Modifier.weight(1f) else Modifier)
                    .fillMaxHeight()
                    .background(bg, inner)
                    .border(HelaBorder.hairline, if (selected) c.border else Color.Transparent, inner)
                    .selectable(selected, role = Role.RadioButton, onClick = { onSelect(index) })
                    .padding(horizontal = HelaSpacing.s3),
                contentAlignment = Alignment.Center,
            ) {
                HelaText(
                    label,
                    style = HelaTheme.typography.label14,
                    color = if (selected) c.textPrimary else c.textSecondary,
                    maxLines = 1,
                )
            }
        }
    }
}

@HelaPreviews
@Composable
private fun HelaTabsPreview() = HelaPreviewSurface {
    var tab by remember { mutableIntStateOf(0) }
    HelaTabs(listOf("Overview", "Deployments", "Analytics", "Logs", "Settings"), tab, { tab = it })
}

@HelaPreviews
@Composable
private fun HelaSegmentedControlPreview() = HelaPreviewSurface {
    var a by remember { mutableIntStateOf(0) }
    var b by remember { mutableIntStateOf(1) }
    HelaPreviewLabel("Hug content")
    HelaSegmentedControl(listOf("Source", "Output"), a, { a = it })
    HelaPreviewLabel("Fill width")
    HelaSegmentedControl(listOf("Daily", "Weekly", "Monthly"), b, { b = it }, fillWidth = true)
}
