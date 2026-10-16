package com.samkt.hela.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

/** Geist empty state: a framed icon, a title, one line of guidance and an optional action. */
@Composable
fun HelaEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector = HelaIcons.Inbox,
    action: (@Composable () -> Unit)? = null,
) {
    val c = HelaTheme.colors
    Column(
        modifier.fillMaxWidth().padding(vertical = HelaSpacing.s10, horizontal = HelaSpacing.s6),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.size(48.dp).border(HelaBorder.hairline, c.border, HelaShapes.default),
            contentAlignment = Alignment.Center,
        ) {
            HelaIcon(icon, contentDescription = null, size = 20.dp, tint = c.textSecondary)
        }
        Spacer(Modifier.height(HelaSpacing.s4))
        HelaText(title, style = HelaTheme.typography.heading16, textAlign = TextAlign.Center)
        if (description != null) {
            Spacer(Modifier.height(HelaSpacing.s1))
            HelaText(description, color = c.textSecondary, textAlign = TextAlign.Center)
        }
        if (action != null) {
            Spacer(Modifier.height(HelaSpacing.s5))
            action()
        }
    }
}

@HelaPreviews
@Composable
private fun HelaEmptyStatePreview() = HelaPreviewSurface {
    HelaCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) {
        HelaEmptyState(
            title = "No deployments yet",
            description = "Push to your connected Git repository to create one.",
            action = {
                HelaButton(
                    "New project",
                    onClick = {},
                    size = HelaButtonSize.Small,
                    prefix = { HelaIcon(HelaIcons.Plus, null) },
                )
            },
        )
    }
    HelaEmptyState(title = "No results", description = "Try a different search.", icon = HelaIcons.Search)
}
