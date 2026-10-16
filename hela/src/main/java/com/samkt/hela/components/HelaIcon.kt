package com.samkt.hela.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.theme.LocalHelaContentColor
import com.samkt.hela.tokens.HelaSpacing

/**
 * Geist-style icon: a 24-unit stroke drawing tinted with the current content color.
 * Pass `contentDescription = null` when the icon sits next to text that already says the same.
 */
@Composable
fun HelaIcon(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    tint: Color = Color.Unspecified,
) {
    val resolved = when {
        tint.isSpecified -> tint
        LocalHelaContentColor.current.isSpecified -> LocalHelaContentColor.current
        else -> HelaTheme.colors.textPrimary
    }
    Image(
        painter = rememberVectorPainter(icon),
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        colorFilter = ColorFilter.tint(resolved),
    )
}

private fun strokeIcon(name: String, pathData: String): ImageVector =
    ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).addPath(
        pathData = addPathNodes(pathData),
        fill = null,
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
    ).build()

/** The icons the design system itself needs, plus a few common ones. Add more here as needed. */
object HelaIcons {
    val Check by lazy { strokeIcon("Check", "M20 6 9 17l-5-5") }
    val Minus by lazy { strokeIcon("Minus", "M5 12h14") }
    val Plus by lazy { strokeIcon("Plus", "M12 5v14M5 12h14") }
    val Close by lazy { strokeIcon("Close", "M18 6 6 18M6 6l12 12") }
    val ChevronDown by lazy { strokeIcon("ChevronDown", "m6 9 6 6 6-6") }
    val ChevronUp by lazy { strokeIcon("ChevronUp", "m18 15-6-6-6 6") }
    val ChevronLeft by lazy { strokeIcon("ChevronLeft", "m15 18-6-6 6-6") }
    val ChevronRight by lazy { strokeIcon("ChevronRight", "m9 18 6-6-6-6") }
    val ArrowRight by lazy { strokeIcon("ArrowRight", "M5 12h14M12 5l7 7-7 7") }
    val Search by lazy { strokeIcon("Search", "M11 19a8 8 0 1 0 0-16 8 8 0 0 0 0 16zM21 21l-4.35-4.35") }
    val Info by lazy {
        strokeIcon("Info", "M12 22a10 10 0 1 0 0-20 10 10 0 0 0 0 20zM12 16v-4M12 8h.01")
    }
    val Warning by lazy {
        strokeIcon(
            "Warning",
            "M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0zM12 9v4M12 17h.01",
        )
    }
    val Success by lazy {
        strokeIcon("Success", "M12 22a10 10 0 1 0 0-20 10 10 0 0 0 0 20zM8 12l3 3 5-6")
    }
    val Error by lazy {
        strokeIcon("Error", "M12 22a10 10 0 1 0 0-20 10 10 0 0 0 0 20zM15 9l-6 6M9 9l6 6")
    }
    val Copy by lazy {
        strokeIcon(
            "Copy",
            "M20 9h-9a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2h9a2 2 0 0 0 2-2v-9a2 2 0 0 0-2-2zM5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1",
        )
    }
    val Eye by lazy {
        strokeIcon("Eye", "M2 12s3.5-7 10-7 10 7 10 7-3.5 7-10 7S2 12 2 12zM12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6z")
    }
    val User by lazy {
        strokeIcon("User", "M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2M12 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8z")
    }
    val Home by lazy { strokeIcon("Home", "M3 10.5 12 3l9 7.5V21h-6v-7H9v7H3z") }
    val More by lazy {
        strokeIcon("More", "M5 12h.01M12 12h.01M19 12h.01")
    }
    val Inbox by lazy {
        strokeIcon(
            "Inbox",
            "M22 12h-6l-2 3h-4l-2-3H2M5.45 5.11 2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z",
        )
    }
    val Trash by lazy {
        strokeIcon("Trash", "M3 6h18M8 6V4h8v2M19 6l-1 14H6L5 6M10 11v6M14 11v6")
    }
    val Settings by lazy {
        strokeIcon(
            "Settings",
            "M12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6zM19.4 15a1.7 1.7 0 0 0 .3 1.8l.1.1a2 2 0 1 1-2.8 2.8l-.1-.1a1.7 1.7 0 0 0-1.8-.3 1.7 1.7 0 0 0-1 1.5V21a2 2 0 1 1-4 0v-.1a1.7 1.7 0 0 0-1.1-1.5 1.7 1.7 0 0 0-1.8.3l-.1.1a2 2 0 1 1-2.8-2.8l.1-.1a1.7 1.7 0 0 0 .3-1.8 1.7 1.7 0 0 0-1.5-1H3a2 2 0 1 1 0-4h.1a1.7 1.7 0 0 0 1.5-1.1 1.7 1.7 0 0 0-.3-1.8l-.1-.1a2 2 0 1 1 2.8-2.8l.1.1a1.7 1.7 0 0 0 1.8.3H9a1.7 1.7 0 0 0 1-1.5V3a2 2 0 1 1 4 0v.1a1.7 1.7 0 0 0 1 1.5 1.7 1.7 0 0 0 1.8-.3l.1-.1a2 2 0 1 1 2.8 2.8l-.1.1a1.7 1.7 0 0 0-.3 1.8V9a1.7 1.7 0 0 0 1.5 1H21a2 2 0 1 1 0 4h-.1a1.7 1.7 0 0 0-1.5 1z",
        )
    }

    internal val all: List<ImageVector>
        get() = listOf(
            Check, Minus, Plus, Close, ChevronDown, ChevronUp, ChevronLeft, ChevronRight, ArrowRight,
            Search, Info, Warning, Success, Error, Copy, Eye, User, Home, More, Inbox, Trash, Settings,
        )
}

@OptIn(ExperimentalLayoutApi::class)
@HelaPreviews
@Composable
private fun HelaIconPreview() = HelaPreviewSurface {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(HelaSpacing.s4),
        verticalArrangement = Arrangement.spacedBy(HelaSpacing.s4),
    ) {
        HelaIcons.all.forEach { icon ->
            Column(Modifier.width(64.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                HelaIcon(icon, contentDescription = icon.name, size = 20.dp)
                HelaText(icon.name, style = HelaTheme.typography.caption, color = HelaTheme.colors.textSecondary, maxLines = 1)
            }
        }
    }
}
