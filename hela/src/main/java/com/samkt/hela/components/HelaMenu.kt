package com.samkt.hela.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.theme.LocalHelaContentColor
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing

/**
 * Dropdown menu anchored to its parent. Place it inside a `Box` next to the trigger:
 *
 * ```
 * Box {
 *     HelaIconButton(HelaIcons.More, "More", onClick = { open = true })
 *     HelaMenu(open, onDismissRequest = { open = false }) { HelaMenuItem("Rename", onClick = …) }
 * }
 * ```
 * Opens below the anchor and flips above when there's no room.
 */
@Composable
fun HelaMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val visible = remember { MutableTransitionState(false) }
    visible.targetState = expanded
    if (!visible.currentState && !visible.targetState) return

    val gap = with(LocalDensity.current) { HelaSpacing.s1.roundToPx() }
    val inset = with(LocalDensity.current) { MenuShadowInset.roundToPx() }
    Popup(
        popupPositionProvider = remember(gap, inset) { DropdownPositionProvider(gap, inset) },
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true),
    ) {
        AnimatedVisibility(
            visibleState = visible,
            enter = fadeIn(tween(HelaMotion.FAST_MS)) +
                scaleIn(tween(HelaMotion.FAST_MS, easing = HelaMotion.easeOut), initialScale = 0.96f),
            exit = fadeOut(tween(HelaMotion.FAST_MS)),
        ) {
            HelaMenuPanel(modifier, content)
        }
    }
}

/** Transparent margin around the panel so its shadow isn't clipped by the popup window. */
internal val MenuShadowInset = HelaSpacing.s1

/** The menu's surface without the popup. Used by [HelaMenu] and handy for previews. */
@Composable
fun HelaMenuPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val c = HelaTheme.colors
    Column(
        modifier
            .padding(MenuShadowInset) // room for the shadow; the popup window would clip it
            .shadow(8.dp, HelaShapes.default, ambientColor = c.grayAlpha.s400, spotColor = c.grayAlpha.s500)
            .clip(HelaShapes.default)
            .background(c.background100)
            .border(HelaBorder.hairline, c.border, HelaShapes.default)
            .widthIn(min = 180.dp, max = 320.dp)
            .width(IntrinsicSize.Max)
            .verticalScroll(rememberScrollState())
            .padding(HelaSpacing.s1),
        content = content,
    )
}

/**
 * A menu row. [destructive] colors it red; [selected] shows a trailing check. [description] adds a
 * line of secondary text under [text], wrapping as needed.
 */
@Composable
fun HelaMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    destructive: Boolean = false,
    selected: Boolean = false,
    trailing: (@Composable () -> Unit)? = null,
    description: String? = null,
) {
    val c = HelaTheme.colors
    val color = when {
        !enabled -> c.textDisabled
        destructive -> c.error
        else -> c.textPrimary
    }
    Row(
        modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .clip(HelaShapes.small)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = HelaSpacing.s2, vertical = if (description != null) HelaSpacing.s2 else 0.dp),
        horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalHelaContentColor provides color) {
            if (icon != null) HelaIcon(icon, contentDescription = null)
            if (description == null) {
                HelaText(text, Modifier.weight(1f), maxLines = 1)
            } else {
                Column(Modifier.weight(1f)) {
                    HelaText(text, maxLines = 1)
                    HelaText(
                        description,
                        style = HelaTheme.typography.label13,
                        color = if (enabled) c.textSecondary else c.textDisabled,
                    )
                }
            }
            when {
                trailing != null -> trailing()
                selected -> HelaIcon(HelaIcons.Check, contentDescription = "Selected")
                else -> Box(Modifier.size(0.dp))
            }
        }
    }
}

/** A hairline between groups of menu items. */
@Composable
fun HelaMenuSeparator() {
    HelaDivider(Modifier.padding(vertical = HelaSpacing.s1))
}

/** Below the anchor, start-aligned, flipped above when it doesn't fit. [insetPx] cancels the shadow margin. */
internal class DropdownPositionProvider(
    private val gapPx: Int,
    private val insetPx: Int,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val maxX = (windowSize.width - popupContentSize.width).coerceAtLeast(0)
        val x = when (layoutDirection) {
            LayoutDirection.Ltr -> anchorBounds.left - insetPx
            LayoutDirection.Rtl -> anchorBounds.right - popupContentSize.width + insetPx
        }.coerceIn(0, maxX)
        val below = anchorBounds.bottom + gapPx - insetPx
        val y = if (below + popupContentSize.height <= windowSize.height) {
            below
        } else {
            (anchorBounds.top - gapPx - popupContentSize.height + insetPx).coerceAtLeast(0)
        }
        return IntOffset(x, y)
    }
}

@HelaPreviews
@Composable
private fun HelaMenuPreview() = HelaPreviewSurface {
    HelaMenuPanel {
        HelaMenuItem("Visit", onClick = {}, icon = HelaIcons.ArrowRight)
        HelaMenuItem("Copy URL", onClick = {}, icon = HelaIcons.Copy, trailing = { HelaKbd("⌘C") })
        HelaMenuItem("Settings", onClick = {}, icon = HelaIcons.Settings, selected = true)
        HelaMenuItem("Redeploy", onClick = {}, enabled = false)
        HelaMenuItem("Promote", onClick = {}, description = "Serve this deployment on the production domain")
        HelaMenuSeparator()
        HelaMenuItem("Delete", onClick = {}, icon = HelaIcons.Trash, destructive = true)
    }
}
