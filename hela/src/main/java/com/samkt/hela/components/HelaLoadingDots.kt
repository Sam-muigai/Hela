package com.samkt.hela.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.theme.LocalHelaContentColor
import com.samkt.hela.tokens.HelaShapes
import kotlin.math.abs

/** Three pulsing dots, for inline "thinking" states such as "Loading…" text or a chat reply. */
@Composable
fun HelaLoadingDots(
    modifier: Modifier = Modifier,
    dotSize: Dp = 4.dp,
    color: Color = Color.Unspecified,
) {
    val tint = when {
        color.isSpecified -> color
        LocalHelaContentColor.current.isSpecified -> LocalHelaContentColor.current
        else -> HelaTheme.colors.textSecondary
    }
    val phase by rememberInfiniteTransition(label = "dots").animateFloat(
        0f, 3f, infiniteRepeatable(tween(1_200, easing = LinearEasing), RepeatMode.Restart), label = "phase",
    )
    Row(
        modifier.semantics { contentDescription = "Loading" },
        horizontalArrangement = Arrangement.spacedBy(dotSize),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(3) { i ->
            // Distance from the travelling peak, wrapped around the three dots.
            val d = abs(((phase - i) % 3f + 3f) % 3f - 0.5f).coerceAtMost(1.5f)
            val alpha = (1f - d / 1.5f).coerceIn(0.2f, 1f)
            Box(Modifier.size(dotSize).alpha(alpha).background(tint, HelaShapes.full))
        }
    }
}

@HelaPreviews
@Composable
private fun HelaLoadingDotsPreview() = HelaPreviewSurface {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        HelaText("Thinking")
        HelaLoadingDots()
    }
    HelaLoadingDots(dotSize = 8.dp, color = HelaTheme.colors.blue.s700)
}
