package com.samkt.hela.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.theme.LocalHelaContentColor

private const val BARS = 12

/** The 12-bar Geist spinner. Picks up the current content color by default. */
@Composable
fun HelaSpinner(
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    color: Color = Color.Unspecified,
) {
    val tint = when {
        color.isSpecified -> color
        LocalHelaContentColor.current.isSpecified -> LocalHelaContentColor.current
        else -> HelaTheme.colors.textSecondary
    }
    val phase by rememberInfiniteTransition(label = "spinner").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1_000, easing = LinearEasing), RepeatMode.Restart),
        label = "phase",
    )
    Canvas(
        modifier
            .size(size)
            .semantics { contentDescription = "Loading" },
    ) {
        val barLength = this.size.minDimension * 0.24f
        val barThickness = this.size.minDimension * 0.08f
        val radius = this.size.minDimension / 2f
        for (i in 0 until BARS) {
            // The newest bar is opaque; older bars fade out behind it.
            val age = (phase - i / BARS.toFloat()).mod(1f)
            val alpha = (1f - age).coerceAtLeast(0.15f)
            rotate(degrees = i * (360f / BARS)) {
                drawRoundRect(
                    color = tint,
                    alpha = alpha,
                    topLeft = Offset(center.x + radius - barLength, center.y - barThickness / 2f),
                    size = Size(barLength, barThickness),
                    cornerRadius = CornerRadius(barThickness / 2f),
                )
            }
        }
    }
}

@HelaPreviews
@Composable
private fun HelaSpinnerPreview() = HelaPreviewSurface {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
        HelaSpinner(size = 12.dp)
        HelaSpinner(size = 16.dp)
        HelaSpinner()
        HelaSpinner(size = 32.dp)
        HelaSpinner(color = HelaTheme.colors.blue.s700)
    }
}
