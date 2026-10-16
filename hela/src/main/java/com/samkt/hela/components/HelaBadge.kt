package com.samkt.hela.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewLabel
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.theme.LocalHelaContentColor
import com.samkt.hela.tokens.HelaColors
import com.samkt.hela.tokens.HelaScale
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaTypography

enum class HelaBadgeVariant { Gray, Blue, Red, Amber, Green, Inverted }

enum class HelaBadgeSize(internal val height: Dp, internal val horizontalPadding: Dp, internal val iconSize: Dp) {
    Small(height = 20.dp, horizontalPadding = 6.dp, iconSize = 12.dp),
    Medium(height = 24.dp, horizontalPadding = 10.dp, iconSize = 14.dp),
    Large(height = 32.dp, horizontalPadding = 12.dp, iconSize = 16.dp),
    ;

    internal fun textStyle(t: HelaTypography): TextStyle = when (this) {
        Small -> t.caption
        Medium -> t.label13
        Large -> t.label14
    }
}

private fun HelaColors.scale(variant: HelaBadgeVariant): HelaScale? = when (variant) {
    HelaBadgeVariant.Gray -> gray
    HelaBadgeVariant.Blue -> blue
    HelaBadgeVariant.Red -> red
    HelaBadgeVariant.Amber -> amber
    HelaBadgeVariant.Green -> green
    HelaBadgeVariant.Inverted -> null
}

/**
 * Geist badge: a non-interactive pill for status, counts and tags.
 *
 * @param subtle tinted background with dark text (Geist's `-subtle` variants). Prefer it in dense
 * lists; keep solid badges for the one thing that must stand out.
 */
@Composable
fun HelaBadge(
    text: String,
    modifier: Modifier = Modifier,
    variant: HelaBadgeVariant = HelaBadgeVariant.Gray,
    subtle: Boolean = false,
    size: HelaBadgeSize = HelaBadgeSize.Medium,
    icon: ImageVector? = null,
) {
    val c = HelaTheme.colors
    val scale = c.scale(variant)
    val (background, content) = when {
        scale == null -> c.inverseSurface to c.onInverseSurface
        subtle -> scale.s200 to scale.s1000
        variant == HelaBadgeVariant.Gray -> scale.s700 to Color.White
        variant == HelaBadgeVariant.Amber -> scale.s700 to Color.Black
        else -> scale.s700 to Color.White
    }
    Row(
        modifier = modifier
            .height(size.height)
            .background(background, HelaShapes.full)
            .padding(horizontal = size.horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalHelaContentColor provides content) {
            if (icon != null) HelaIcon(icon, contentDescription = null, size = size.iconSize)
            HelaText(text, style = size.textStyle(HelaTheme.typography), maxLines = 1)
        }
    }
}

@HelaPreviews
@Composable
private fun HelaBadgePreview() = HelaPreviewSurface {
    HelaPreviewLabel("Solid")
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        HelaBadgeVariant.entries.forEach { HelaBadge(it.name, variant = it, size = HelaBadgeSize.Small) }
    }
    HelaPreviewLabel("Subtle")
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        HelaBadgeVariant.entries.forEach { HelaBadge(it.name, variant = it, subtle = true, size = HelaBadgeSize.Small) }
    }
    HelaPreviewLabel("Sizes, with icon")
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        HelaBadgeSize.entries.forEach {
            HelaBadge("Ready", variant = HelaBadgeVariant.Green, subtle = true, size = it, icon = HelaIcons.Check)
        }
    }
}
