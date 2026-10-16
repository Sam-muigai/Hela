package com.samkt.hela.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.samkt.hela.foundation.HelaIndication
import com.samkt.hela.tokens.DarkHelaColors
import com.samkt.hela.tokens.HelaColors
import com.samkt.hela.tokens.HelaTypography
import com.samkt.hela.tokens.LightHelaColors

val LocalHelaColors = staticCompositionLocalOf { LightHelaColors }
val LocalHelaTypography = staticCompositionLocalOf { HelaTypography() }

/** The color text and icons pick up when no color is passed. Components set it for their content. */
val LocalHelaContentColor = staticCompositionLocalOf { Color.Unspecified }

/** The style [com.samkt.hela.components.HelaText] uses when none is passed. Buttons and inputs set it for their content. */
val LocalHelaTextStyle = staticCompositionLocalOf { HelaTypography().compact }

/**
 * Root of the design system. Wrap each app (and each preview) in it.
 *
 * It provides the Hela tokens, replaces the Material ripple with [HelaIndication], and maps the
 * tokens onto [MaterialTheme] so any Material component you haven't replaced yet still looks close.
 */
@Composable
fun HelaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    typography: HelaTypography = HelaTheme.defaultTypography,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkHelaColors else LightHelaColors
    val selection = remember(colors) {
        TextSelectionColors(handleColor = colors.blue.s700, backgroundColor = colors.blue.s700.copy(alpha = 0.3f))
    }

    MaterialTheme(
        colorScheme = colors.toMaterialColorScheme(),
        typography = typography.toMaterialTypography(),
    ) {
        CompositionLocalProvider(
            LocalHelaColors provides colors,
            LocalHelaTypography provides typography,
            LocalHelaContentColor provides colors.textPrimary,
            LocalHelaTextStyle provides typography.compact,
            LocalIndication provides HelaIndication,
            LocalTextSelectionColors provides selection,
            content = content,
        )
    }
}

object HelaTheme {
    internal val defaultTypography = HelaTypography()

    val colors: HelaColors
        @Composable @ReadOnlyComposable get() = LocalHelaColors.current

    val typography: HelaTypography
        @Composable @ReadOnlyComposable get() = LocalHelaTypography.current
}

private fun HelaColors.toMaterialColorScheme() = if (isDark) {
    darkColorScheme(
        primary = inverseSurface, onPrimary = onInverseSurface,
        secondary = gray.s200, onSecondary = textPrimary,
        background = background100, onBackground = textPrimary,
        surface = background100, onSurface = textPrimary,
        surfaceVariant = background200, onSurfaceVariant = textSecondary,
        surfaceContainer = background200, surfaceContainerHigh = gray.s100,
        outline = gray.s400, outlineVariant = gray.s300,
        error = red.s700, onError = Color.White,
        surfaceTint = Color.Transparent,
    )
} else {
    lightColorScheme(
        primary = inverseSurface, onPrimary = onInverseSurface,
        secondary = gray.s200, onSecondary = textPrimary,
        background = background100, onBackground = textPrimary,
        surface = background100, onSurface = textPrimary,
        surfaceVariant = background200, onSurfaceVariant = textSecondary,
        surfaceContainer = background200, surfaceContainerHigh = gray.s100,
        outline = gray.s400, outlineVariant = gray.s300,
        error = red.s700, onError = Color.White,
        surfaceTint = Color.Transparent,
    )
}

private fun HelaTypography.toMaterialTypography() = Typography(
    displayLarge = display, displayMedium = title, displaySmall = heading32,
    headlineLarge = heading32, headlineMedium = heading24, headlineSmall = heading20,
    titleLarge = heading20, titleMedium = heading16, titleSmall = label14,
    bodyLarge = body, bodyMedium = compact, bodySmall = label13,
    labelLarge = label14, labelMedium = label13, labelSmall = caption,
)
