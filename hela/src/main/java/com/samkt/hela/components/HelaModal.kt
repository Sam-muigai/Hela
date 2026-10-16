package com.samkt.hela.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

/**
 * Geist modal: title, optional description and body, and a footer strip of [actions]
 * (cancel at the start, the confirming action at the end).
 */
@Composable
fun HelaModal(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    dismissOnClickOutside: Boolean = true,
    actions: @Composable RowScope.() -> Unit,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = dismissOnClickOutside,
        ),
    ) {
        HelaModalPanel(
            title = title,
            modifier = modifier.padding(HelaSpacing.s4),
            description = description,
            actions = actions,
            content = content,
        )
    }
}

/** The modal's surface without the dialog window. Used by [HelaModal] and handy for previews. */
@Composable
fun HelaModalPanel(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    actions: @Composable RowScope.() -> Unit,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val c = HelaTheme.colors
    Column(
        modifier
            .widthIn(max = 480.dp)
            .fillMaxWidth()
            .shadow(24.dp, HelaShapes.large, ambientColor = c.grayAlpha.s500, spotColor = c.grayAlpha.s600)
            .clip(HelaShapes.large)
            .background(c.background100)
            .border(HelaBorder.hairline, c.border, HelaShapes.large),
    ) {
        Column(Modifier.padding(HelaSpacing.s6)) {
            HelaText(title, Modifier.semantics { heading() }, style = HelaTheme.typography.heading24)
            if (description != null) {
                Spacer(Modifier.height(HelaSpacing.s2))
                HelaText(description, color = c.textSecondary)
            }
            if (content != null) {
                Spacer(Modifier.height(HelaSpacing.s4))
                content()
            }
        }
        HelaDivider()
        Row(
            Modifier
                .fillMaxWidth()
                .background(c.background200)
                .padding(HelaSpacing.s4),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
    }
}

@HelaPreviews
@Composable
private fun HelaModalPreview() = HelaPreviewSurface {
    HelaModalPanel(
        title = "Delete project",
        description = "This permanently deletes acme-web and its deployments.",
        actions = {
            HelaButton("Cancel", onClick = {}, variant = HelaButtonVariant.Secondary)
            HelaButton("Delete", onClick = {}, variant = HelaButtonVariant.Error)
        },
    ) {
        HelaInput("", {}, label = "Type acme-web to confirm", placeholder = "acme-web")
    }
}
