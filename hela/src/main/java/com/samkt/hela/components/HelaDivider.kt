package com.samkt.hela.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewLabel
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder

/** 1dp hairline in the default border color. Prefer spacing over dividers where spacing is enough. */
@Composable
fun HelaDivider(modifier: Modifier = Modifier, vertical: Boolean = false) {
    val size = if (vertical) Modifier.fillMaxHeight().width(HelaBorder.hairline)
    else Modifier.fillMaxWidth().height(HelaBorder.hairline)
    Box(modifier.then(size).background(HelaTheme.colors.border))
}

@HelaPreviews
@Composable
private fun HelaDividerPreview() = HelaPreviewSurface {
    HelaPreviewLabel("Horizontal")
    HelaDivider()
    HelaPreviewLabel("Vertical")
    Row(Modifier.height(20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        HelaText("Docs")
        HelaDivider(vertical = true)
        HelaText("Guides")
        HelaDivider(vertical = true)
        HelaText("Help")
    }
}
