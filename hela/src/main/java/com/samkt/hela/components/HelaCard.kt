package com.samkt.hela.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

/**
 * Bordered surface (Geist's "material base"). No shadow: separation comes from the hairline.
 * Passing [onClick] makes the whole card tappable with the flat Hela hover/press tint.
 */
@Composable
fun HelaCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(HelaSpacing.s6),
    content: @Composable ColumnScope.() -> Unit,
) {
    val c = HelaTheme.colors
    Column(
        modifier
            .clip(HelaShapes.large)
            .background(c.background100)
            .border(HelaBorder.hairline, c.border, HelaShapes.large)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(contentPadding),
        content = content,
    )
}

/** A card footer strip: subtle background, top hairline. Use with `contentPadding = PaddingValues(0.dp)`. */
@Composable
fun HelaCardFooter(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val c = HelaTheme.colors
    Column(modifier.fillMaxWidth()) {
        HelaDivider()
        Row(
            Modifier
                .fillMaxWidth()
                .background(c.background200)
                .padding(horizontal = HelaSpacing.s6, vertical = HelaSpacing.s3),
            horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@HelaPreviews
@Composable
private fun HelaCardPreview() = HelaPreviewSurface {
    val t = HelaTheme.typography
    HelaCard(Modifier.fillMaxWidth()) {
        HelaText("acme-web", style = t.heading16)
        Spacer(Modifier.height(4.dp))
        HelaText("acme-web.vercel.app", color = HelaTheme.colors.textSecondary)
        Spacer(Modifier.height(HelaSpacing.s3))
        Row(horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2), verticalAlignment = Alignment.CenterVertically) {
            HelaStatusDot(HelaStatus.Ready, showLabel = true)
            HelaText("2m ago", style = t.label13, color = HelaTheme.colors.textSecondary)
        }
    }
    HelaCard(Modifier.fillMaxWidth(), onClick = {}) {
        HelaText("Clickable card", style = t.heading16)
        HelaText("Press to see the flat tint.", color = HelaTheme.colors.textSecondary)
    }
    HelaCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(0.dp)) {
        Column(Modifier.padding(HelaSpacing.s6)) {
            HelaText("Team name", style = t.heading16)
            HelaText("Used across the dashboard.", color = HelaTheme.colors.textSecondary)
        }
        HelaCardFooter {
            HelaButton("Save", onClick = {}, size = HelaButtonSize.Small)
        }
    }
}
