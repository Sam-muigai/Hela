package com.samkt.hela.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewLabel
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.foundation.helaFocusRing
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.theme.LocalHelaContentColor
import com.samkt.hela.theme.LocalHelaTextStyle
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaColors
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaTypography

enum class HelaButtonVariant {
    /** Monochrome: black on light, white on dark. One per screen. */
    Primary,

    /** Surface fill with a hairline border. The everyday button. */
    Secondary,

    /** No fill, no border until pressed. For low-emphasis actions in toolbars and rows. */
    Tertiary,

    /** Destructive actions only. */
    Error,
}

enum class HelaButtonSize(
    internal val height: Dp,
    internal val horizontalPadding: Dp,
    internal val cornerRadius: Dp,
    internal val iconSize: Dp,
) {
    Small(height = 32.dp, horizontalPadding = 12.dp, cornerRadius = 6.dp, iconSize = 16.dp),
    Medium(height = 40.dp, horizontalPadding = 16.dp, cornerRadius = 6.dp, iconSize = 16.dp),
    Large(height = 48.dp, horizontalPadding = 20.dp, cornerRadius = 8.dp, iconSize = 20.dp),
    ;

    internal fun textStyle(t: HelaTypography): TextStyle = if (this == Large) t.label16 else t.label14
}

@Immutable
private data class ButtonColors(val background: Color, val content: Color, val border: Color)

private fun buttonColors(
    variant: HelaButtonVariant,
    c: HelaColors,
    interactive: Boolean,
    pressed: Boolean,
    hovered: Boolean,
): ButtonColors {
    if (!interactive) {
        return if (variant == HelaButtonVariant.Tertiary) {
            ButtonColors(Color.Transparent, c.textDisabled, Color.Transparent)
        } else {
            ButtonColors(c.gray.s100, c.textDisabled, c.border)
        }
    }
    val overlay = when {
        pressed -> c.grayAlpha.s300
        hovered -> c.grayAlpha.s200
        else -> Color.Transparent
    }
    return when (variant) {
        HelaButtonVariant.Primary -> ButtonColors(
            background = when {
                pressed -> c.gray.s800
                hovered -> c.gray.s900
                else -> c.inverseSurface
            },
            content = c.onInverseSurface,
            border = Color.Transparent,
        )
        HelaButtonVariant.Secondary -> ButtonColors(
            background = overlay.compositeOver(c.surface),
            content = c.textPrimary,
            border = c.border,
        )
        HelaButtonVariant.Tertiary -> ButtonColors(
            background = overlay,
            content = c.textPrimary,
            border = Color.Transparent,
        )
        HelaButtonVariant.Error -> ButtonColors(
            background = if (pressed || hovered) c.red.s800 else c.red.s700,
            content = Color.White,
            border = Color.Transparent,
        )
    }
}

/**
 * Geist-style button.
 *
 * @param loading shows the spinner in place of [prefix] and blocks clicks, keeping the label so
 * the width doesn't jump.
 * @param pill fully rounded ends (Geist's `shape="rounded"`).
 */
@Composable
fun HelaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: HelaButtonVariant = HelaButtonVariant.Primary,
    size: HelaButtonSize = HelaButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    pill: Boolean = false,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) = HelaButtonLayout(
    onClick, modifier, variant, size, enabled, loading, pill, prefix, suffix, interactionSource,
    iconOnly = false, content = content,
)

/**
 * Square, icon-only button (Geist's `svgOnly`). [contentDescription] is required because there is
 * no visible label.
 */
@Composable
fun HelaIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: HelaButtonVariant = HelaButtonVariant.Secondary,
    size: HelaButtonSize = HelaButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    pill: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
) = HelaButtonLayout(
    onClick, modifier, variant, size, enabled, loading, pill, prefix = null, suffix = null,
    interactionSource = interactionSource, iconOnly = true,
) {
    if (!loading) HelaIcon(icon, contentDescription, size = size.iconSize)
}

@Composable
private fun HelaButtonLayout(
    onClick: () -> Unit,
    modifier: Modifier,
    variant: HelaButtonVariant,
    size: HelaButtonSize,
    enabled: Boolean,
    loading: Boolean,
    pill: Boolean,
    prefix: (@Composable () -> Unit)?,
    suffix: (@Composable () -> Unit)?,
    interactionSource: MutableInteractionSource?,
    iconOnly: Boolean,
    content: @Composable RowScope.() -> Unit,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val hovered by source.collectIsHoveredAsState()
    val focused by source.collectIsFocusedAsState()

    val c = HelaTheme.colors
    val interactive = enabled && !loading
    val target = buttonColors(variant, c, interactive, pressed, hovered)
    val spec = tween<Color>(HelaMotion.FAST_MS)
    val background by animateColorAsState(target.background, spec, label = "bg")
    val contentColor by animateColorAsState(target.content, spec, label = "fg")
    val border by animateColorAsState(target.border, spec, label = "border")

    val shape = if (pill) HelaShapes.full else RoundedCornerShape(size.cornerRadius)

    Row(
        modifier = modifier
            .helaFocusRing(visible = focused, shape = shape, color = c.focusRing)
            .height(size.height)
            .then(if (iconOnly) Modifier.width(size.height) else Modifier)
            .clip(shape)
            .background(background)
            .border(HelaBorder.hairline, border, shape)
            .clickable(
                interactionSource = source,
                indication = null,
                enabled = interactive,
                role = Role.Button,
                onClick = onClick,
            )
            .then(if (loading) Modifier.semantics { stateDescription = "Loading" } else Modifier)
            .padding(horizontal = if (iconOnly) 0.dp else size.horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(
            LocalHelaContentColor provides contentColor,
            LocalHelaTextStyle provides size.textStyle(HelaTheme.typography),
        ) {
            if (loading) HelaSpinner(size = size.iconSize) else prefix?.invoke()
            content()
            if (!loading) suffix?.invoke()
        }
    }
}

/** Convenience overload for a text-only button. */
@Composable
fun HelaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: HelaButtonVariant = HelaButtonVariant.Primary,
    size: HelaButtonSize = HelaButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    pill: Boolean = false,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
) {
    HelaButton(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        size = size,
        enabled = enabled,
        loading = loading,
        pill = pill,
        prefix = prefix,
        suffix = suffix,
    ) {
        HelaText(text, maxLines = 1)
    }
}

@HelaPreviews
@Composable
private fun HelaButtonVariantsPreview() = HelaPreviewSurface {
    HelaButtonVariant.entries.forEach { variant ->
        HelaPreviewLabel(variant.name)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            HelaButton("Deploy", onClick = {}, variant = variant)
            HelaButton("Disabled", onClick = {}, variant = variant, enabled = false)
            HelaButton("Saving", onClick = {}, variant = variant, loading = true)
        }
    }
}

@HelaPreviews
@Composable
private fun HelaButtonSizesPreview() = HelaPreviewSurface {
    HelaPreviewLabel("Sizes")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        HelaButtonSize.entries.forEach { HelaButton(it.name, onClick = {}, size = it) }
    }
    HelaPreviewLabel("Prefix / suffix, pill")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        HelaButton(
            "New",
            onClick = {},
            variant = HelaButtonVariant.Secondary,
            prefix = { HelaIcon(HelaIcons.Plus, contentDescription = null) },
        )
        HelaButton(
            "Continue",
            onClick = {},
            pill = true,
            suffix = { HelaIcon(HelaIcons.ArrowRight, contentDescription = null) },
        )
    }
    HelaPreviewLabel("Icon only")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        HelaButtonSize.entries.forEach { HelaIconButton(HelaIcons.Settings, "Settings", onClick = {}, size = it) }
        HelaIconButton(HelaIcons.Plus, "Add", onClick = {}, variant = HelaButtonVariant.Primary, pill = true)
        HelaIconButton(HelaIcons.More, "More", onClick = {}, variant = HelaButtonVariant.Tertiary)
    }
}
