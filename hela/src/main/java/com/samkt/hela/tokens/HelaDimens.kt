package com.samkt.hela.tokens

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/** 4dp grid. Name = multiple of 4 (s4 = 16dp), matching Geist's --space-N. */
object HelaSpacing {
    val s1 = 4.dp
    val s2 = 8.dp
    val s3 = 12.dp
    val s4 = 16.dp
    val s5 = 20.dp
    val s6 = 24.dp
    val s8 = 32.dp
    val s10 = 40.dp
    val s12 = 48.dp
    val s16 = 64.dp
}

object HelaRadius {
    val small = 6.dp   // buttons, inputs, badges
    val default = 8.dp // menus, popovers
    val large = 12.dp  // cards, modals, sheets
}

object HelaShapes {
    val small = RoundedCornerShape(HelaRadius.small)
    val default = RoundedCornerShape(HelaRadius.default)
    val large = RoundedCornerShape(HelaRadius.large)
    val full = RoundedCornerShape(percent = 50)
}

object HelaBorder {
    val hairline = 1.dp
    val focusRingWidth = 2.dp
    val focusRingOffset = 2.dp
}

/** Short and calm. Nothing springy. */
object HelaMotion {
    const val FAST_MS = 150
    const val DEFAULT_MS = 200
    const val SLOW_MS = 300
    val easeOut = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)
    val easeInOut = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
}
