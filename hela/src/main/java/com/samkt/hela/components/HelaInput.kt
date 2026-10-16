package com.samkt.hela.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.foundation.helaFocusRing
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.theme.LocalHelaContentColor
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

enum class HelaInputSize(internal val height: Dp, internal val horizontalPadding: Dp) {
    Small(height = 32.dp, horizontalPadding = 10.dp),
    Medium(height = 40.dp, horizontalPadding = 12.dp),
    Large(height = 48.dp, horizontalPadding = 14.dp),
}

/**
 * Geist-style single-line input with an optional label above and helper or error text below.
 *
 * Focus draws a darker border plus a soft 3dp ring; [error] turns both red and is announced to
 * TalkBack. [prefix] and [suffix] sit inside the field (icons, units such as "KES", a clear button).
 */
@Composable
fun HelaInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    error: String? = null,
    size: HelaInputSize = HelaInputSize.Medium,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val hovered by source.collectIsHoveredAsState()

    val c = HelaTheme.colors
    val t = HelaTheme.typography
    val isError = error != null

    val borderTarget = when {
        !enabled -> c.border
        isError -> c.red.s700
        focused -> c.borderActive
        hovered -> c.borderHover
        else -> c.border
    }
    val border by animateColorAsState(borderTarget, tween(HelaMotion.FAST_MS), label = "border")
    val ringColor = if (isError) c.red.s400 else c.grayAlpha.s400
    val textColor = if (enabled) c.textPrimary else c.textDisabled
    val textStyle = (if (size == HelaInputSize.Large) t.body else t.compact).copy(color = textColor)

    Column(modifier) {
        if (label != null) {
            HelaText(label, style = t.label13, color = c.textSecondary)
            Spacer(Modifier.height(HelaSpacing.s2))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .then(if (isError) Modifier.semantics { this.error(error!!) } else Modifier),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            interactionSource = source,
            cursorBrush = SolidColor(c.textPrimary),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .helaFocusRing(
                            visible = focused && enabled,
                            shape = HelaShapes.small,
                            color = ringColor,
                            width = 3.dp,
                            offset = 0.dp,
                        )
                        .height(size.height)
                        .background(if (enabled) c.surface else c.surfaceSubtle, HelaShapes.small)
                        .border(HelaBorder.hairline, border, HelaShapes.small)
                        .padding(horizontal = size.horizontalPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2),
                ) {
                    CompositionLocalProvider(LocalHelaContentColor provides c.textSecondary) {
                        prefix?.invoke()
                    }
                    Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty() && placeholder != null) {
                            HelaText(
                                placeholder,
                                style = textStyle,
                                color = c.gray.s700,
                                maxLines = 1
                            )
                        }
                        innerTextField()
                    }
                    CompositionLocalProvider(LocalHelaContentColor provides c.textSecondary) {
                        suffix?.invoke()
                    }
                }
            },
        )

        val footnote = error ?: helper
        if (footnote != null) {
            Spacer(Modifier.height(HelaSpacing.s2))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isError) {
                    ErrorDot(c.error)
                    Spacer(Modifier.width(6.dp))
                }
                HelaText(
                    footnote,
                    style = t.label13,
                    color = if (isError) c.error else c.textSecondary,
                )
            }
        }
    }
}

/** A small filled circle so the error state isn't carried by color alone. */
@Composable
private fun ErrorDot(color: Color) {
    Box(Modifier.size(6.dp).background(color, HelaShapes.full))
}

@HelaPreviews
@Composable
private fun HelaInputStatesPreview() = HelaPreviewSurface {
    var value by remember { mutableStateOf("") }
    HelaInput(value, { value = it }, label = "Project name", placeholder = "my-app", helper = "Lowercase, no spaces.")
    HelaInput("acme.com", {}, label = "Domain", prefix = { HelaIcon(HelaIcons.Search, null) })
    HelaInput("hunter2", {}, label = "Password", visualTransformation = PasswordVisualTransformation(),
        suffix = { HelaIcon(HelaIcons.Eye, "Show password") })
    HelaInput("my app", {}, label = "Slug", error = "Slugs can't contain spaces.")
    HelaInput("Read only", {}, label = "Disabled", enabled = false)
}

@HelaPreviews
@Composable
private fun HelaInputSizesPreview() = HelaPreviewSurface {
    HelaInputSize.entries.forEach { HelaInput("", {}, placeholder = it.name, size = it) }
    HelaInput("1,500", {}, prefix = { HelaText("KES", style = HelaTheme.typography.mono13) })
}
