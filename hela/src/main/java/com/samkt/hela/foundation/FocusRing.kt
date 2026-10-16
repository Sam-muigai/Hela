package com.samkt.hela.foundation

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.unit.Dp
import com.samkt.hela.tokens.HelaBorder

/**
 * Geist's crisp focus ring: a solid stroke drawn *outside* the shape with a small gap.
 * Apply it before any `clip` so the ring isn't clipped away.
 */
fun Modifier.helaFocusRing(
    visible: Boolean,
    shape: Shape,
    color: Color,
    width: Dp = HelaBorder.focusRingWidth,
    offset: Dp = HelaBorder.focusRingOffset,
): Modifier = drawWithContent {
    drawContent()
    if (!visible) return@drawWithContent
    val strokePx = width.toPx()
    val inflate = offset.toPx() + strokePx / 2f
    val outline = shape.createOutline(
        Size(size.width + inflate * 2, size.height + inflate * 2),
        layoutDirection,
        this,
    )
    translate(-inflate, -inflate) {
        drawOutline(outline, color = color, style = Stroke(strokePx))
    }
}
