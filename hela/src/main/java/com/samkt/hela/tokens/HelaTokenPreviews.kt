package com.samkt.hela.tokens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.samkt.hela.components.HelaText
import com.samkt.hela.foundation.HelaPreviewLabel
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme

private fun HelaScale.steps() = listOf(s100, s200, s300, s400, s500, s600, s700, s800, s900, s1000)

@Composable
private fun ScaleRow(name: String, scale: HelaScale) {
    HelaPreviewLabel(name)
    Row(Modifier.fillMaxWidth().height(28.dp)) {
        scale.steps().forEach { Box(Modifier.weight(1f).height(28.dp).background(it)) }
    }
}

@HelaPreviews
@Composable
private fun HelaColorScalesPreview() = HelaPreviewSurface {
    val c = HelaTheme.colors
    HelaPreviewLabel("100 → 1000")
    ScaleRow("gray", c.gray)
    ScaleRow("grayAlpha", c.grayAlpha)
    ScaleRow("blue", c.blue)
    ScaleRow("red", c.red)
    ScaleRow("amber", c.amber)
    ScaleRow("green", c.green)
}

@HelaPreviews
@Composable
private fun HelaSemanticColorsPreview() = HelaPreviewSurface {
    val c = HelaTheme.colors
    listOf(
        "surface" to c.surface, "surfaceSubtle" to c.surfaceSubtle, "textPrimary" to c.textPrimary,
        "textSecondary" to c.textSecondary, "textDisabled" to c.textDisabled, "border" to c.border,
        "borderHover" to c.borderHover, "borderActive" to c.borderActive, "focusRing" to c.focusRing,
        "success" to c.success, "warning" to c.warning, "error" to c.error, "info" to c.info,
        "inverseSurface" to c.inverseSurface,
    ).forEach { (name, color) -> Swatch(name, color) }
}

@Composable
private fun Swatch(name: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(24.dp).background(color, HelaShapes.small).border(HelaBorder.hairline, HelaTheme.colors.border, HelaShapes.small))
        HelaText(name, style = HelaTheme.typography.mono13)
    }
}

@HelaPreviews
@Composable
private fun HelaSpacingRadiusPreview() = HelaPreviewSurface {
    val c = HelaTheme.colors
    HelaPreviewLabel("Spacing")
    listOf(
        "s1" to HelaSpacing.s1, "s2" to HelaSpacing.s2, "s3" to HelaSpacing.s3, "s4" to HelaSpacing.s4,
        "s5" to HelaSpacing.s5, "s6" to HelaSpacing.s6, "s8" to HelaSpacing.s8, "s10" to HelaSpacing.s10,
        "s12" to HelaSpacing.s12, "s16" to HelaSpacing.s16,
    ).forEach { (name, dp) ->
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            HelaText(name, Modifier.width(32.dp), style = HelaTheme.typography.mono13, color = c.textSecondary)
            Box(Modifier.width(dp).height(12.dp).background(c.blue.s700))
        }
    }
    HelaPreviewLabel("Radius: small / default / large / full")
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        listOf(HelaShapes.small, HelaShapes.default, HelaShapes.large, HelaShapes.full).forEach {
            Box(Modifier.size(48.dp).background(c.gray.s100, it).border(HelaBorder.hairline, c.borderActive, it))
        }
    }
}
