package com.samkt.hela.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samkt.hela.foundation.HelaPreviewLabel
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaShapes

/**
 * Circular avatar. Shows [painter] when given, otherwise up to two initials from [name], otherwise
 * a person icon. [name] is also the accessibility label.
 */
@Composable
fun HelaAvatar(
    name: String?,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    size: Dp = 32.dp,
) {
    val c = HelaTheme.colors
    Box(
        modifier = modifier
            .size(size)
            .clip(HelaShapes.full)
            .background(c.gray.s100)
            .border(HelaBorder.hairline, c.border, HelaShapes.full)
            .semantics { if (name != null) contentDescription = name },
        contentAlignment = Alignment.Center,
    ) {
        val initials = name?.initials()
        when {
            painter != null -> Image(painter, null, Modifier.size(size), contentScale = ContentScale.Crop)
            !initials.isNullOrEmpty() -> HelaText(
                initials,
                style = HelaTheme.typography.label14.copy(fontSize = (size.value * 0.38f).sp),
                color = c.textSecondary,
                maxLines = 1,
            )
            else -> HelaIcon(HelaIcons.User, null, size = size * 0.5f, tint = c.textSecondary)
        }
    }
}

private fun String.initials(): String =
    split(' ').filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }

/** Overlapping stack of avatars. Anything past [max] collapses into a "+N" chip. */
@Composable
fun HelaAvatarGroup(
    names: List<String>,
    modifier: Modifier = Modifier,
    max: Int = 4,
    size: Dp = 32.dp,
) {
    val c = HelaTheme.colors
    val overflow = names.size - max
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(-size * 0.25f)) {
        names.take(max).forEach { name ->
            // A ring in the page background separates overlapping avatars.
            HelaAvatar(name, Modifier.border(2.dp, c.background100, HelaShapes.full), size = size)
        }
        if (overflow > 0) {
            Box(
                Modifier
                    .size(size)
                    .border(2.dp, c.background100, HelaShapes.full)
                    .background(c.gray.s200, HelaShapes.full),
                contentAlignment = Alignment.Center,
            ) {
                HelaText("+$overflow", style = HelaTheme.typography.caption, color = c.textSecondary)
            }
        }
    }
}

@HelaPreviews
@Composable
private fun HelaAvatarPreview() = HelaPreviewSurface {
    HelaPreviewLabel("Sizes")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        listOf(20.dp, 24.dp, 32.dp, 40.dp, 64.dp).forEach { HelaAvatar("Sam Muigai", size = it) }
    }
    HelaPreviewLabel("Fallback")
    HelaAvatar(name = null)
    HelaPreviewLabel("Group")
    HelaAvatarGroup(listOf("Ada Lovelace", "Grace Hopper", "Alan Turing", "Linus T", "Margaret H", "Ken T"))
}
