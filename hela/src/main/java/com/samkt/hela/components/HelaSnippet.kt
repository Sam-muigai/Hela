package com.samkt.hela.components

import android.content.ClipData
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Geist snippet: a one-line command in mono with a copy button. The icon turns into a check for
 * two seconds after copying.
 *
 * @param prompt prefix shown before the text but not copied (Geist shows "$").
 * @param inverted dark-on-light swap, for snippets on marketing-style surfaces.
 */
@Composable
fun HelaSnippet(
    text: String,
    modifier: Modifier = Modifier,
    prompt: String? = "$",
    inverted: Boolean = false,
) {
    val c = HelaTheme.colors
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    var copied by remember { mutableStateOf(false) }
    LaunchedEffect(copied) {
        if (copied) {
            delay(2_000)
            copied = false
        }
    }
    val background = if (inverted) c.inverseSurface else c.background100
    val content = if (inverted) c.onInverseSurface else c.textPrimary

    Row(
        modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(background, HelaShapes.small)
            .border(HelaBorder.hairline, if (inverted) background else c.border, HelaShapes.small)
            .padding(start = HelaSpacing.s4, end = HelaSpacing.s1),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2),
    ) {
        Row(Modifier.weight(1f).horizontalScroll(rememberScrollState())) {
            val line = if (prompt != null) "$prompt $text" else text
            HelaText(line, style = HelaTheme.typography.mono13, color = content, maxLines = 1)
        }
        HelaIconButton(
            icon = if (copied) HelaIcons.Check else HelaIcons.Copy,
            contentDescription = if (copied) "Copied" else "Copy to clipboard",
            onClick = {
                scope.launch {
                    clipboard.setClipEntry(ClipEntry(ClipData.newPlainText("snippet", text)))
                    copied = true
                }
            },
            variant = HelaButtonVariant.Tertiary,
            size = HelaButtonSize.Small,
        )
    }
}

/** Keyboard key, e.g. `HelaKbd("⌘")`, `HelaKbd("K")`. */
@Composable
fun HelaKbd(key: String, modifier: Modifier = Modifier) {
    val c = HelaTheme.colors
    val shape = RoundedCornerShape(4.dp)
    Box(
        modifier
            .defaultMinSize(minWidth = 20.dp)
            .height(20.dp)
            .background(c.background200, shape)
            .border(HelaBorder.hairline, c.border, shape)
            .padding(horizontal = 5.dp),
        contentAlignment = Alignment.Center,
    ) {
        HelaText(key, style = HelaTheme.typography.mono13.copy(lineHeight = HelaTheme.typography.caption.lineHeight), color = c.textSecondary)
    }
}

@HelaPreviews
@Composable
private fun HelaSnippetPreview() = HelaPreviewSurface {
    HelaSnippet("npm i -g vercel")
    HelaSnippet("vercel deploy --prod", inverted = true)
    HelaSnippet("https://acme.vercel.app", prompt = null)
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
        HelaKbd("⌘")
        HelaKbd("K")
        HelaText("  to search", color = HelaTheme.colors.textSecondary)
    }
}
