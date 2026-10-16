package com.samkt.hela.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.samkt.hela.foundation.HelaPreviewLabel
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.theme.LocalHelaContentColor
import com.samkt.hela.theme.LocalHelaTextStyle

/**
 * Text without Material. Defaults to the current [LocalHelaTextStyle] (the `compact` role, 14/20,
 * unless a parent such as a button changes it) in the current content color.
 * Pass `color` only for a semantic reason, for example `HelaTheme.colors.textSecondary`.
 */
@Composable
fun HelaText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalHelaTextStyle.current,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    val resolved = when {
        color.isSpecified -> color
        style.color.isSpecified -> style.color
        else -> LocalHelaContentColor.current
    }
    BasicText(
        text = text,
        modifier = modifier,
        style = style.copy(color = resolved, textAlign = textAlign ?: style.textAlign),
        maxLines = maxLines,
        overflow = overflow,
    )
}

@HelaPreviews
@Composable
private fun HelaTextScalePreview() = HelaPreviewSurface {
    val t = HelaTheme.typography
    listOf(
        "display" to t.display, "title" to t.title, "heading32" to t.heading32,
        "heading24" to t.heading24, "heading20" to t.heading20, "heading16" to t.heading16,
        "lede" to t.lede, "body" to t.body, "compact" to t.compact, "label16" to t.label16,
        "label14" to t.label14, "label13" to t.label13, "caption" to t.caption,
        "mono14" to t.mono14, "mono13" to t.mono13,
    ).forEach { (name, style) ->
        HelaPreviewLabel(name)
        HelaText("Ship faster", style = style, maxLines = 1)
    }
}

@HelaPreviews
@Composable
private fun HelaTextColorsPreview() = HelaPreviewSurface {
    val c = HelaTheme.colors
    HelaText("Primary text")
    HelaText("Secondary text", color = c.textSecondary)
    HelaText("Disabled text", color = c.textDisabled)
    HelaText("Success", color = c.success)
    HelaText("Warning", color = c.warning)
    HelaText("Error", color = c.error)
    HelaText("Info", color = c.info)
    HelaText(
        "Long text truncates with an ellipsis when maxLines is reached, which keeps rows tidy.",
        maxLines = 1,
    )
}
