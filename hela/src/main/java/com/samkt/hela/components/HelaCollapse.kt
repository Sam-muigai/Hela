package com.samkt.hela.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaSpacing

/**
 * Geist collapse: a titled row that expands to reveal [content]. Stack several with [HelaDivider]s
 * between them for an accordion; hoist [expanded] to allow only one open at a time.
 */
@Composable
fun HelaCollapse(
    title: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val rotation by animateFloatAsState(if (expanded) 180f else 0f, tween(HelaMotion.DEFAULT_MS), label = "chevron")
    Column(modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable(role = Role.Button, onClick = { onExpandedChange(!expanded) })
                .semantics { stateDescription = if (expanded) "Expanded" else "Collapsed" }
                .padding(vertical = HelaSpacing.s4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HelaText(title, Modifier.weight(1f), style = HelaTheme.typography.heading16)
            HelaIcon(HelaIcons.ChevronDown, contentDescription = null, modifier = Modifier.rotate(rotation))
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(tween(HelaMotion.DEFAULT_MS, easing = HelaMotion.easeOut)) + fadeIn(),
            exit = shrinkVertically(tween(HelaMotion.FAST_MS)) + fadeOut(),
        ) {
            Column(Modifier.padding(bottom = HelaSpacing.s4)) { content() }
        }
    }
}

@HelaPreviews
@Composable
private fun HelaCollapsePreview() = HelaPreviewSurface {
    var open by remember { mutableIntStateOf(0) }
    listOf(
        "What is a preview deployment?" to "Every push to a non-production branch gets its own URL.",
        "Can I use a custom domain?" to "Yes. Add it in project settings and point your DNS at us.",
        "How is usage billed?" to "Per team, monthly, based on what you use.",
    ).forEachIndexed { i, (q, a) ->
        if (i > 0) HelaDivider()
        HelaCollapse(q, expanded = open == i, onExpandedChange = { open = if (it) i else -1 }) {
            HelaText(a, color = HelaTheme.colors.textSecondary)
        }
    }
}
