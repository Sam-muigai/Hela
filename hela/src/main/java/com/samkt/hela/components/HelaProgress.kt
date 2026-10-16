package com.samkt.hela.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samkt.hela.foundation.HelaPreviewLabel
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

/**
 * Determinate progress bar. [value] is 0..1. Defaults to primary text color; pass [color] for a
 * semantic meaning (e.g. `HelaTheme.colors.red.s700` for usage over a limit).
 */
@Composable
fun HelaProgress(
    value: Float,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    height: Dp = 8.dp,
) {
    val c = HelaTheme.colors
    val clamped = value.coerceIn(0f, 1f)
    val animated by animateFloatAsState(clamped, tween(HelaMotion.SLOW_MS, easing = HelaMotion.easeOut), label = "progress")
    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .background(c.gray.s200, HelaShapes.full)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo(clamped, 0f..1f) },
    ) {
        Box(
            Modifier
                .fillMaxWidth(animated)
                .fillMaxHeight()
                .background(if (color.isSpecified) color else c.textPrimary, HelaShapes.full),
        )
    }
}

/**
 * Geist gauge: a ring showing a 0..100 [value], colored by threshold (green below 50, amber
 * below 80, red above) unless [color] is given.
 */
@Composable
fun HelaGauge(
    value: Int,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    showValue: Boolean = true,
    color: Color = Color.Unspecified,
) {
    val c = HelaTheme.colors
    val v = value.coerceIn(0, 100)
    val animated by animateFloatAsState(v / 100f, tween(HelaMotion.SLOW_MS, easing = HelaMotion.easeOut), label = "gauge")
    val arc = when {
        color.isSpecified -> color
        v < 50 -> c.green.s700
        v < 80 -> c.amber.s700
        else -> c.red.s700
    }
    val track = c.gray.s200
    Box(
        modifier
            .size(size)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo(v.toFloat(), 0f..100f) },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = this.size.minDimension * 0.1f
            val inset = stroke / 2
            val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
            drawArc(track, 0f, 360f, false, Offset(inset, inset), arcSize, style = Stroke(stroke))
            if (animated > 0f) {
                drawArc(
                    arc, -90f, 360f * animated, false, Offset(inset, inset), arcSize,
                    style = Stroke(stroke, cap = StrokeCap.Round),
                )
            }
        }
        if (showValue) {
            HelaText(
                "$v",
                style = HelaTheme.typography.label14.copy(fontSize = (size.value * 0.3f).sp),
                maxLines = 1,
            )
        }
    }
}

@HelaPreviews
@Composable
private fun HelaProgressPreview() = HelaPreviewSurface {
    HelaPreviewLabel("Progress")
    HelaProgress(0f)
    HelaProgress(0.35f)
    HelaProgress(0.7f, color = HelaTheme.colors.blue.s700)
    HelaProgress(1f, color = HelaTheme.colors.red.s700, height = 4.dp)
    HelaPreviewLabel("Gauge")
    Row(horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s4), verticalAlignment = Alignment.CenterVertically) {
        HelaGauge(12)
        HelaGauge(64)
        HelaGauge(92)
        HelaGauge(40, size = 24.dp, showValue = false)
        HelaGauge(75, size = 72.dp, color = HelaTheme.colors.blue.s700)
    }
}
