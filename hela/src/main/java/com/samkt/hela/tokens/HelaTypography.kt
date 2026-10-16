package com.samkt.hela.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.samkt.hela.R

val GeistSans = FontFamily(
    Font(R.font.geist_regular, FontWeight.Normal),
    Font(R.font.geist_medium, FontWeight.Medium),
    Font(R.font.geist_semibold, FontWeight.SemiBold),
    Font(R.font.geist_bold, FontWeight.Bold),
)

val GeistMono = FontFamily(
    Font(R.font.geistmono_regular, FontWeight.Normal),
    Font(R.font.geistmono_medium, FontWeight.Medium),
    Font(R.font.geistmono_semibold, FontWeight.SemiBold),
)

// Web-like line boxes: center glyphs in the line height and don't add extra top/bottom space.
private val WebLineHeight = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None,
)

private fun style(
    family: FontFamily,
    size: Int,
    lineHeight: Int,
    weight: FontWeight,
    tracking: TextUnit = 0.em,
) = TextStyle(
    fontFamily = family,
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
    fontWeight = weight,
    letterSpacing = tracking,
    lineHeightStyle = WebLineHeight,
)

/**
 * Geist type roles. Headings carry negative tracking; that tight tracking is a large part
 * of the Vercel look, so don't reset it in call sites.
 */
@Immutable
data class HelaTypography(
    val display: TextStyle = style(GeistSans, 48, 56, FontWeight.SemiBold, (-0.06).em),
    val title: TextStyle = style(GeistSans, 40, 48, FontWeight.SemiBold, (-0.06).em),
    val heading32: TextStyle = style(GeistSans, 32, 40, FontWeight.SemiBold, (-0.05).em),
    val heading24: TextStyle = style(GeistSans, 24, 32, FontWeight.SemiBold, (-0.04).em),
    val heading20: TextStyle = style(GeistSans, 20, 26, FontWeight.SemiBold, (-0.02).em),
    val heading16: TextStyle = style(GeistSans, 16, 24, FontWeight.SemiBold, (-0.02).em),
    val lede: TextStyle = style(GeistSans, 18, 28, FontWeight.Normal),
    val body: TextStyle = style(GeistSans, 16, 24, FontWeight.Normal),
    val compact: TextStyle = style(GeistSans, 14, 20, FontWeight.Normal),
    val label16: TextStyle = style(GeistSans, 16, 24, FontWeight.Medium),
    val label14: TextStyle = style(GeistSans, 14, 20, FontWeight.Medium),
    val label13: TextStyle = style(GeistSans, 13, 18, FontWeight.Normal),
    val caption: TextStyle = style(GeistSans, 12, 16, FontWeight.Normal),
    val mono14: TextStyle = style(GeistMono, 14, 20, FontWeight.Normal),
    val mono13: TextStyle = style(GeistMono, 13, 18, FontWeight.Normal),
)
