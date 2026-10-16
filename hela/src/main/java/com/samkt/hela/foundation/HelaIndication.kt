package com.samkt.hela.foundation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.invalidateDraw
import com.samkt.hela.theme.LocalHelaColors
import com.samkt.hela.tokens.HelaMotion
import kotlinx.coroutines.launch

/**
 * Replaces the Material ripple. Pressed and hovered states are a flat tint of the primary text
 * color (a Geist "background step"), not a spreading wave.
 *
 * Hela components draw their own state colors and pass `indication = null`; this is the default for
 * anything else made clickable inside [com.samkt.hela.theme.HelaTheme] (list rows, cards, custom tiles).
 */
object HelaIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        HelaIndicationNode(interactionSource)

    override fun equals(other: Any?): Boolean = other === this
    override fun hashCode(): Int = System.identityHashCode(this)
}

private const val HOVER_ALPHA = 0.04f
private const val PRESS_ALPHA = 0.08f

private class HelaIndicationNode(
    private val interactionSource: InteractionSource,
) : Modifier.Node(), DrawModifierNode, CompositionLocalConsumerModifierNode {

    private val overlayAlpha = Animatable(0f)

    override fun onAttach() {
        coroutineScope.launch {
            val presses = mutableListOf<PressInteraction.Press>()
            val hovers = mutableListOf<HoverInteraction.Enter>()
            interactionSource.interactions.collect { interaction: Interaction ->
                when (interaction) {
                    is PressInteraction.Press -> presses += interaction
                    is PressInteraction.Release -> presses -= interaction.press
                    is PressInteraction.Cancel -> presses -= interaction.press
                    is HoverInteraction.Enter -> hovers += interaction
                    is HoverInteraction.Exit -> hovers -= interaction.enter
                }
                val target = when {
                    presses.isNotEmpty() -> PRESS_ALPHA
                    hovers.isNotEmpty() -> HOVER_ALPHA
                    else -> 0f
                }
                launch {
                    overlayAlpha.animateTo(target, tween(HelaMotion.FAST_MS)) { invalidateDraw() }
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        val alpha = overlayAlpha.value
        if (alpha > 0f) {
            val tint = currentValueOf(LocalHelaColors).textPrimary
            drawRect(color = tint, alpha = alpha)
        }
    }
}
