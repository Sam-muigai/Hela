package com.samkt.hela.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * A 10-step Geist-style scale. Each step has a fixed job:
 *
 * - 100 default background, 200 hover background, 300 active background
 * - 400 default border, 500 hover border, 600 active border
 * - 700 high-contrast background, 800 hover high-contrast background
 * - 900 secondary text and icons, 1000 primary text and icons
 */
@Immutable
data class HelaScale(
    val s100: Color,
    val s200: Color,
    val s300: Color,
    val s400: Color,
    val s500: Color,
    val s600: Color,
    val s700: Color,
    val s800: Color,
    val s900: Color,
    val s1000: Color,
)

@Immutable
data class HelaColors(
    val background100: Color,
    val background200: Color,
    val gray: HelaScale,
    /** Use only when the surface underneath must show through (overlays, borders on tinted surfaces). */
    val grayAlpha: HelaScale,
    val blue: HelaScale,
    val red: HelaScale,
    val amber: HelaScale,
    val green: HelaScale,
    val isDark: Boolean,
) {
    // Semantic roles. Components should read these, not raw steps, wherever a role exists.
    val surface: Color get() = background100
    val surfaceSubtle: Color get() = background200
    val textPrimary: Color get() = gray.s1000
    val textSecondary: Color get() = gray.s900
    val textDisabled: Color get() = gray.s700
    val border: Color get() = grayAlpha.s400
    val borderHover: Color get() = grayAlpha.s500
    val borderActive: Color get() = grayAlpha.s600
    val focusRing: Color get() = if (isDark) blue.s900 else blue.s700
    val success: Color get() = green.s900
    val warning: Color get() = amber.s900
    val error: Color get() = red.s900
    val info: Color get() = blue.s900

    /** Monochrome primary: black on light, white on dark. */
    val inverseSurface: Color get() = gray.s1000
    val onInverseSurface: Color get() = background100
}
