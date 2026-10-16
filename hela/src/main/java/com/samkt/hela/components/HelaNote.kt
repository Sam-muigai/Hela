package com.samkt.hela.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.theme.LocalHelaContentColor
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaColors
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

enum class HelaNoteType { Default, Info, Success, Warning, Error }

private data class NoteColors(val background: Color, val border: Color, val content: Color)

private fun HelaColors.noteColors(type: HelaNoteType, fill: Boolean): NoteColors {
    val scale = when (type) {
        HelaNoteType.Default -> null
        HelaNoteType.Info -> blue
        HelaNoteType.Success -> green
        HelaNoteType.Warning -> amber
        HelaNoteType.Error -> red
    }
    return if (scale == null) {
        NoteColors(if (fill) gray.s100 else Color.Transparent, border, textPrimary)
    } else {
        NoteColors(if (fill) scale.s100 else Color.Transparent, scale.s400, scale.s900)
    }
}

internal fun HelaNoteType.icon(): ImageVector = when (this) {
    HelaNoteType.Default, HelaNoteType.Info -> HelaIcons.Info
    HelaNoteType.Success -> HelaIcons.Success
    HelaNoteType.Warning -> HelaIcons.Warning
    HelaNoteType.Error -> HelaIcons.Error
}

/**
 * Geist note: an inline, persistent message (for transient ones use the toast). Color is always
 * backed by an icon so the type reads without color. [action] sits at the end, e.g. a small button.
 *
 * @param fill tinted background. Off = outline only, for quieter notes inside cards.
 */
@Composable
fun HelaNote(
    text: String,
    modifier: Modifier = Modifier,
    type: HelaNoteType = HelaNoteType.Default,
    fill: Boolean = true,
    action: (@Composable () -> Unit)? = null,
) {
    val colors = HelaTheme.colors.noteColors(type, fill)
    Row(
        modifier
            .fillMaxWidth()
            .background(colors.background, HelaShapes.small)
            .border(HelaBorder.hairline, colors.border, HelaShapes.small)
            .padding(horizontal = HelaSpacing.s3, vertical = 10.dp)
            .semantics { if (type == HelaNoteType.Error) liveRegion = LiveRegionMode.Polite },
        horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalHelaContentColor provides colors.content) {
            HelaIcon(type.icon(), contentDescription = type.name.takeIf { type != HelaNoteType.Default })
            Box(Modifier.weight(1f)) { HelaText(text) }
            action?.invoke()
        }
    }
}

@HelaPreviews
@Composable
private fun HelaNotePreview() = HelaPreviewSurface {
    HelaNoteType.entries.forEach { HelaNote("This is a ${it.name.lowercase()} note.", type = it) }
    HelaNote("Outline only, no fill.", type = HelaNoteType.Info, fill = false)
    HelaNote(
        "Your trial ends in 3 days.",
        type = HelaNoteType.Warning,
        action = { HelaButton("Upgrade", onClick = {}, size = HelaButtonSize.Small) },
    )
}
