package com.samkt.hela.foundation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.samkt.hela.components.HelaText
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaSpacing

/** Renders a preview twice: light and dark. Put it on every component preview. */
@Preview(name = "Light", showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
@Preview(
    name = "Dark",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 360,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
annotation class HelaPreviews

/** Wraps preview content in [HelaTheme] on the theme background with standard padding. */
@Composable
fun HelaPreviewSurface(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    HelaTheme(darkTheme = isSystemInDarkTheme()) {
        Column(
            modifier = modifier
                .background(HelaTheme.colors.background100)
                .padding(HelaSpacing.s4),
            verticalArrangement = Arrangement.spacedBy(HelaSpacing.s3),
            content = content,
        )
    }
}

/** A small caption that labels a group of states inside a preview. */
@Composable
fun HelaPreviewLabel(text: String) {
    HelaText(text, style = HelaTheme.typography.mono13, color = HelaTheme.colors.textSecondary)
}
