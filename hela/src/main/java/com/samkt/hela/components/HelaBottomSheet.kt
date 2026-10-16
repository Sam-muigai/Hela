package com.samkt.hela.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaRadius
import com.samkt.hela.tokens.HelaSpacing
import kotlinx.coroutines.launch

private val SheetShape = RoundedCornerShape(topStart = HelaRadius.large, topEnd = HelaRadius.large)

/**
 * Modal bottom sheet: a drag handle, optional [title] and [description], then [content].
 * Drag down, tap the scrim or press back to dismiss.
 *
 * Show it with `if (open) HelaBottomSheet(onDismissRequest = { open = false }) { … }`. To close it
 * from inside with the slide-down animation, call [HelaBottomSheetScope.dismiss].
 *
 * @param skipPartiallyExpanded open straight to full height (no half-open stop), which suits
 * short action sheets. Set false for tall, scrolling content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelaBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    skipPartiallyExpanded: Boolean = true,
    content: @Composable HelaBottomSheetScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    val c = HelaTheme.colors
    val scope = rememberCoroutineScope()
    val sheetScope = HelaBottomSheetScope {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) onDismissRequest()
        }
    }
    // The Material sheet only supplies the window, gestures and scrim. The panel draws the
    // surface itself: a border on the Material sheet's modifier would ignore its slide offset.
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = SheetShape,
        containerColor = Color.Transparent,
        contentColor = c.textPrimary,
        tonalElevation = 0.dp,
        scrimColor = Color.Black.copy(alpha = if (c.isDark) 0.6f else 0.4f),
        dragHandle = null,
        contentWindowInsets = { WindowInsets(0) },
    ) {
        HelaBottomSheetPanel(Modifier, title, description) { sheetScope.content() }
    }
}

/** Lets sheet content close its own sheet with the slide-down animation. */
fun interface HelaBottomSheetScope {
    fun dismiss()
}

/** The sheet's surface without the modal window or gestures. Handy for previews. */
@Composable
fun HelaBottomSheetPanel(
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val c = HelaTheme.colors
    Column(
        modifier
            .fillMaxWidth()
            .background(c.background100, SheetShape)
            .border(HelaBorder.hairline, c.border, SheetShape)
            .navigationBarsPadding(),
    ) {
        DragHandle()
        SheetBody(title, description, content)
    }
}

@Composable
private fun DragHandle() {
    Box(Modifier.fillMaxWidth().padding(vertical = HelaSpacing.s3), contentAlignment = Alignment.Center) {
        Box(Modifier.size(width = 36.dp, height = 4.dp).background(HelaTheme.colors.gray.s400, RoundedCornerShape(2.dp)))
    }
}

@Composable
private fun SheetBody(
    title: String?,
    description: String?,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(Modifier.fillMaxWidth().padding(start = HelaSpacing.s4, end = HelaSpacing.s4, bottom = HelaSpacing.s6)) {
        if (title != null) {
            HelaText(title, Modifier.semantics { heading() }, style = HelaTheme.typography.heading20)
        }
        if (description != null) {
            Spacer(Modifier.height(HelaSpacing.s1))
            HelaText(description, color = HelaTheme.colors.textSecondary)
        }
        if (title != null || description != null) Spacer(Modifier.height(HelaSpacing.s4))
        content()
    }
}

@HelaPreviews
@Composable
private fun HelaBottomSheetActionsPreview() = HelaPreviewSurface {
    HelaBottomSheetPanel(title = "acme-web", description = "Production · Ready") {
        HelaMenuItem("Visit", onClick = {}, icon = HelaIcons.ArrowRight)
        HelaMenuItem("Copy URL", onClick = {}, icon = HelaIcons.Copy)
        HelaMenuItem("Settings", onClick = {}, icon = HelaIcons.Settings)
        HelaMenuSeparator()
        HelaMenuItem("Delete", onClick = {}, icon = HelaIcons.Trash, destructive = true)
    }
}

@HelaPreviews
@Composable
private fun HelaBottomSheetFormPreview() = HelaPreviewSurface {
    HelaBottomSheetPanel(title = "Add domain") {
        HelaInput("", {}, label = "Domain", placeholder = "acme.com")
        Spacer(Modifier.height(HelaSpacing.s4))
        HelaButton("Add", onClick = {}, modifier = Modifier.fillMaxWidth())
    }
}
