package com.samkt.hela.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

/**
 * Loading placeholder with Geist's slow left-to-right shimmer. Size it with [modifier] to match
 * the content it stands in for, so nothing jumps when the real content arrives.
 */
@Composable
fun HelaSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = HelaShapes.small,
) {
    val c = HelaTheme.colors
    val base = c.gray.s100
    val highlight = c.gray.s200
    val progress by rememberInfiniteTransition(label = "skeleton").animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(1_500, easing = LinearEasing), RepeatMode.Restart),
        label = "shimmer",
    )
    Box(
        modifier
            .clip(shape)
            .semantics { contentDescription = "Loading" }
            .drawBehind {
                val x = size.width * progress
                drawRect(
                    Brush.linearGradient(
                        colors = listOf(base, highlight, base),
                        start = Offset(x - size.width / 2, 0f),
                        end = Offset(x + size.width / 2, 0f),
                    ),
                )
            },
    )
}

@HelaPreviews
@Composable
private fun HelaSkeletonPreview() = HelaPreviewSurface {
    Row(horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s3), verticalAlignment = Alignment.CenterVertically) {
        HelaSkeleton(Modifier.size(40.dp), shape = HelaShapes.full)
        Column(verticalArrangement = Arrangement.spacedBy(HelaSpacing.s2)) {
            HelaSkeleton(Modifier.fillMaxWidth(0.5f).height(14.dp))
            HelaSkeleton(Modifier.fillMaxWidth(0.8f).height(12.dp))
        }
    }
    HelaSkeleton(Modifier.fillMaxWidth().height(120.dp), shape = HelaShapes.large)
    HelaSkeleton(Modifier.size(width = 96.dp, height = 40.dp))
}
