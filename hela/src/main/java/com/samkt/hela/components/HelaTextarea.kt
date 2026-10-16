package com.samkt.hela.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.foundation.helaFocusRing
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

/**
 * Multi-line counterpart of [HelaInput], with the same label, focus ring and error treatment.
 * [maxLength] shows a counter and stops input past the limit.
 *
 * [keyboardOptions] defaults to sentence capitalization, since multi-line fields usually hold prose.
 * Pass `KeyboardOptions.Default` for code, addresses or other free-form text.
 */
@Composable
fun HelaTextarea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    error: String? = null,
    enabled: Boolean = true,
    minLines: Int = 3,
    maxLines: Int = 8,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource? = null,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val c = HelaTheme.colors
    val t = HelaTheme.typography
    val isError = error != null
    val border by animateColorAsState(
        when {
            !enabled -> c.border
            isError -> c.red.s700
            focused -> c.borderActive
            else -> c.border
        },
        tween(HelaMotion.FAST_MS), label = "border",
    )
    val textStyle = t.compact.copy(color = if (enabled) c.textPrimary else c.textDisabled)

    Column(modifier) {
        if (label != null) {
            HelaText(label, style = t.label13, color = c.textSecondary)
            Spacer(Modifier.height(HelaSpacing.s2))
        }
        BasicTextField(
            value = value,
            onValueChange = { if (maxLength == null || it.length <= maxLength) onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .then(if (isError) Modifier.semantics { this.error(error!!) } else Modifier),
            enabled = enabled,
            textStyle = textStyle,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = source,
            cursorBrush = SolidColor(c.textPrimary),
            decorationBox = { inner ->
                Box(
                    Modifier
                        .helaFocusRing(
                            visible = focused && enabled,
                            shape = HelaShapes.small,
                            color = if (isError) c.red.s400 else c.grayAlpha.s400,
                            width = 3.dp,
                            offset = 0.dp,
                        )
                        .background(if (enabled) c.surface else c.surfaceSubtle, HelaShapes.small)
                        .border(HelaBorder.hairline, border, HelaShapes.small)
                        .padding(horizontal = HelaSpacing.s3, vertical = 10.dp),
                ) {
                    if (value.isEmpty() && placeholder != null) {
                        HelaText(placeholder, style = textStyle, color = c.gray.s700)
                    }
                    inner()
                }
            },
        )
        val footnote = error ?: helper
        if (footnote != null || maxLength != null) {
            Spacer(Modifier.height(HelaSpacing.s2))
            Row {
                HelaText(
                    footnote.orEmpty(),
                    Modifier.weight(1f),
                    style = t.label13,
                    color = if (isError) c.error else c.textSecondary,
                )
                if (maxLength != null) {
                    HelaText("${value.length}/$maxLength", style = t.mono13, color = c.textSecondary)
                }
            }
        }
    }
}

@HelaPreviews
@Composable
private fun HelaTextareaPreview() = HelaPreviewSurface {
    var text by remember { mutableStateOf("") }
    HelaTextarea(text, { text = it }, label = "Feedback", placeholder = "What could be better?", maxLength = 280)
    HelaTextarea("Too short", {}, label = "Description", error = "Write at least 20 characters.", minLines = 2)
    HelaTextarea("Locked", {}, label = "Disabled", enabled = false, minLines = 2)
}
