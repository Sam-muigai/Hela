package com.samkt.hela.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaShapes

/** Deployment-style states, as in the Vercel dashboard. */
enum class HelaStatus(val label: String) {
    Queued("Queued"),
    Building("Building"),
    Ready("Ready"),
    Error("Error"),
    Canceled("Canceled"),
}

/** A 10dp colored dot for a [HelaStatus], optionally followed by its label. */
@Composable
fun HelaStatusDot(
    status: HelaStatus,
    modifier: Modifier = Modifier,
    showLabel: Boolean = false,
) {
    val c = HelaTheme.colors
    val color = when (status) {
        HelaStatus.Queued -> c.gray.s600
        HelaStatus.Building -> c.amber.s700
        HelaStatus.Ready -> c.green.s700
        HelaStatus.Error -> c.red.s700
        HelaStatus.Canceled -> c.gray.s400
    }
    Row(
        modifier.semantics(mergeDescendants = true) { if (!showLabel) contentDescription = status.label },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(10.dp).background(color, HelaShapes.full))
        if (showLabel) HelaText(status.label, style = HelaTheme.typography.label14)
    }
}

@HelaPreviews
@Composable
private fun HelaStatusDotPreview() = HelaPreviewSurface {
    HelaStatus.entries.forEach { HelaStatusDot(it, showLabel = true) }
}
