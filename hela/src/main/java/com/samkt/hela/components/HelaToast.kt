package com.samkt.hela.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.theme.LocalHelaContentColor
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing
import kotlinx.coroutines.delay

/** One toast. Types reuse [HelaNoteType] so a toast and a note for the same event look related. */
data class HelaToastData(
    val message: String,
    val type: HelaNoteType = HelaNoteType.Default,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
    val durationMs: Long = 4_000,
    internal val id: Long = System.nanoTime(),
)

/** Holds the toast on screen. Create with `remember { HelaToastState() }` and pass to [HelaToastHost]. */
@Stable
class HelaToastState {
    var current: HelaToastData? by mutableStateOf(null)
        private set

    /** Shows [toast], replacing any visible one. */
    fun show(toast: HelaToastData) {
        current = toast
    }

    fun show(message: String, type: HelaNoteType = HelaNoteType.Default) = show(HelaToastData(message, type))

    fun dismiss() {
        current = null
    }
}

/**
 * Renders [state]'s toast at the bottom of the parent and auto-dismisses it. Put it last in the
 * screen's root `Box` so it draws above content.
 */
@Composable
fun HelaToastHost(state: HelaToastState, modifier: Modifier = Modifier) {
    val toast = state.current
    LaunchedEffect(toast?.id) {
        if (toast != null) {
            delay(toast.durationMs)
            if (state.current?.id == toast.id) state.dismiss()
        }
    }
    Box(modifier.fillMaxWidth().padding(HelaSpacing.s4), contentAlignment = Alignment.BottomCenter) {
        AnimatedContent(
            targetState = toast,
            transitionSpec = {
                (slideInVertically(tween(HelaMotion.DEFAULT_MS, easing = HelaMotion.easeOut)) { it / 2 } +
                    fadeIn(tween(HelaMotion.DEFAULT_MS))) togetherWith fadeOut(tween(HelaMotion.FAST_MS))
            },
            label = "toast",
        ) { data ->
            if (data != null) {
                HelaToast(
                    data.message,
                    type = data.type,
                    actionLabel = data.actionLabel,
                    onAction = {
                        data.onAction?.invoke()
                        state.dismiss()
                    },
                )
            }
        }
    }
}

/** The toast surface on its own. Usually shown through [HelaToastHost]. */
@Composable
fun HelaToast(
    message: String,
    modifier: Modifier = Modifier,
    type: HelaNoteType = HelaNoteType.Default,
    actionLabel: String? = null,
    onAction: () -> Unit = {},
) {
    val c = HelaTheme.colors
    val iconTint = when (type) {
        HelaNoteType.Default -> c.textSecondary
        HelaNoteType.Info -> c.info
        HelaNoteType.Success -> c.success
        HelaNoteType.Warning -> c.warning
        HelaNoteType.Error -> c.error
    }
    Row(
        modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .shadow(16.dp, HelaShapes.default, ambientColor = c.grayAlpha.s500, spotColor = c.grayAlpha.s600)
            .background(c.background100, HelaShapes.default)
            .border(HelaBorder.hairline, c.border, HelaShapes.default)
            .padding(start = HelaSpacing.s4, end = HelaSpacing.s2, top = HelaSpacing.s2, bottom = HelaSpacing.s2)
            .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (type != HelaNoteType.Default) HelaIcon(type.icon(), contentDescription = type.name, tint = iconTint)
        CompositionLocalProvider(LocalHelaContentColor provides c.textPrimary) {
            HelaText(message, Modifier.weight(1f).padding(vertical = HelaSpacing.s1))
        }
        if (actionLabel != null) {
            HelaButton(actionLabel, onClick = onAction, variant = HelaButtonVariant.Secondary, size = HelaButtonSize.Small)
        }
    }
}

@HelaPreviews
@Composable
private fun HelaToastPreview() = HelaPreviewSurface {
    HelaToast("Copied to clipboard")
    HelaToast("Deployment ready", type = HelaNoteType.Success, actionLabel = "Visit")
    HelaToast("Build failed: missing env var DATABASE_URL", type = HelaNoteType.Error, actionLabel = "Logs")
    HelaToast("Domain verification pending", type = HelaNoteType.Warning)
}

@HelaPreviews
@Composable
private fun HelaToastHostPreview() = HelaPreviewSurface {
    val state = remember { HelaToastState().apply { show("Shown through the host", HelaNoteType.Info) } }
    HelaButton("Show toast", onClick = { state.show("Saved", HelaNoteType.Success) }, variant = HelaButtonVariant.Secondary)
    HelaToastHost(state)
}
