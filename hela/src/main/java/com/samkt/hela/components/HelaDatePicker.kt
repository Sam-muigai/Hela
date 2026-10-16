package com.samkt.hela.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.samkt.hela.foundation.HelaPreviewSurface
import com.samkt.hela.foundation.HelaPreviews
import com.samkt.hela.theme.HelaTheme
import com.samkt.hela.tokens.HelaBorder
import com.samkt.hela.tokens.HelaMotion
import com.samkt.hela.tokens.HelaShapes
import com.samkt.hela.tokens.HelaSpacing
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Inline month calendar for picking a single date. For a date in a dialog, use [HelaDatePickerModal].
 *
 * Dates are epoch millis at UTC midnight, the same convention as Material's `DatePicker`. Convert
 * with `LocalDate.ofEpochDay(millis / 86_400_000)` or [HelaDates.utcMillis]. Days outside
 * [minDateMillis]..[maxDateMillis] (inclusive) can't be picked. The week starts on the locale's first day.
 */
@Composable
fun HelaCalendar(
    selectedDateMillis: Long?,
    onDateSelect: (Long) -> Unit,
    modifier: Modifier = Modifier,
    minDateMillis: Long? = null,
    maxDateMillis: Long? = null,
    initialDisplayedMonthMillis: Long? = selectedDateMillis,
) {
    val locale = currentLocale()
    val today = remember { HelaDates.todayUtcMillis() }
    var month by rememberSaveable { mutableStateOf(monthIndex(initialDisplayedMonthMillis ?: today)) }
    val minDay = minDateMillis?.let(::startOfDay)
    val maxDay = maxDateMillis?.let(::startOfDay)
    val c = HelaTheme.colors
    val t = HelaTheme.typography

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(HelaSpacing.s2)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HelaText(
                remember(month, locale) { monthTitle(month, locale) },
                Modifier.weight(1f).semantics { heading() },
                style = t.label14,
            )
            HelaIconButton(
                HelaIcons.ChevronLeft, "Previous month", { month-- },
                variant = HelaButtonVariant.Tertiary, size = HelaButtonSize.Small,
                enabled = minDay == null || month > monthIndex(minDay),
            )
            HelaIconButton(
                HelaIcons.ChevronRight, "Next month", { month++ },
                variant = HelaButtonVariant.Tertiary, size = HelaButtonSize.Small,
                enabled = maxDay == null || month < monthIndex(maxDay),
            )
        }
        val firstDayOfWeek = remember(locale) { Calendar.getInstance(locale).firstDayOfWeek }
        val weekdays = remember(locale) { DateFormatSymbols.getInstance(locale).shortWeekdays }
        Row(Modifier.fillMaxWidth()) {
            repeat(7) { i ->
                val dayOfWeek = (firstDayOfWeek - 1 + i) % 7 + 1
                Box(Modifier.weight(1f).height(32.dp), contentAlignment = Alignment.Center) {
                    HelaText(weekdays[dayOfWeek].take(2), style = t.caption, color = c.textSecondary)
                }
            }
        }
        val days = remember(month, firstDayOfWeek) { monthGrid(month, firstDayOfWeek) }
        val dateFormat = remember(locale) { DateFormat.getDateInstance(DateFormat.FULL, locale).apply { timeZone = UTC } }
        days.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    Box(Modifier.weight(1f).height(40.dp), contentAlignment = Alignment.Center) {
                        if (day != null) {
                            DayCell(
                                day = day,
                                selected = selectedDateMillis?.let(::startOfDay) == day,
                                today = day == today,
                                enabled = (minDay == null || day >= minDay) && (maxDay == null || day <= maxDay),
                                label = dateFormat.format(day),
                                onClick = { onDateSelect(day) },
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * [HelaCalendar] in a [HelaModal] with Cancel and Select buttons. [onConfirm] gets the picked date
 * (UTC-midnight millis); Select stays disabled until a date is picked.
 *
 * Show it with `if (open) HelaDatePickerModal(onDismissRequest = { open = false }, onConfirm = { … })`.
 */
@Composable
fun HelaDatePickerModal(
    onDismissRequest: () -> Unit,
    onConfirm: (Long) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Select date",
    selectedDateMillis: Long? = null,
    minDateMillis: Long? = null,
    maxDateMillis: Long? = null,
    confirmLabel: String = "Select",
    dismissLabel: String = "Cancel",
) {
    var pending by rememberSaveable { mutableStateOf(selectedDateMillis) }
    HelaModal(
        onDismissRequest = onDismissRequest,
        title = title,
        modifier = modifier,
        description = pending?.let { HelaDates.format(it, currentLocale()) } ?: "No date selected",
        actions = {
            HelaButton(dismissLabel, onClick = onDismissRequest, variant = HelaButtonVariant.Secondary)
            HelaButton(confirmLabel, onClick = { pending?.let(onConfirm) }, enabled = pending != null)
        },
    ) {
        HelaCalendar(pending, { pending = it }, minDateMillis = minDateMillis, maxDateMillis = maxDateMillis)
    }
}

/** Helpers for the UTC-midnight millis that [HelaCalendar] and [HelaDatePickerModal] use. */
object HelaDates {
    /** UTC midnight of [year]-[month]-[day], with [month] 1-based (January = 1). */
    fun utcMillis(year: Int, month: Int, day: Int): Long =
        Calendar.getInstance(UTC).apply {
            clear()
            set(year, month - 1, day)
        }.timeInMillis

    /** Today in the device's time zone, as UTC-midnight millis. */
    fun todayUtcMillis(): Long {
        val local = Calendar.getInstance()
        return utcMillis(local[Calendar.YEAR], local[Calendar.MONTH] + 1, local[Calendar.DAY_OF_MONTH])
    }

    /** A medium date for display, e.g. "Oct 16, 2026" in English. */
    fun format(millis: Long, locale: Locale = Locale.getDefault()): String =
        DateFormat.getDateInstance(DateFormat.MEDIUM, locale).apply { timeZone = UTC }.format(millis)
}

@Composable
private fun DayCell(
    day: Long,
    selected: Boolean,
    today: Boolean,
    enabled: Boolean,
    label: String,
    onClick: () -> Unit,
) {
    val c = HelaTheme.colors
    val background by animateColorAsState(
        if (selected) c.inverseSurface else Color.Transparent,
        tween(HelaMotion.FAST_MS), label = "day",
    )
    Box(
        Modifier
            .size(36.dp)
            .background(background, HelaShapes.small)
            .then(if (today && !selected) Modifier.border(HelaBorder.hairline, c.borderActive, HelaShapes.small) else Modifier)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics {
                contentDescription = if (today) "Today, $label" else label
                this.selected = selected
            },
        contentAlignment = Alignment.Center,
    ) {
        HelaText(
            dayOfMonth(day).toString(),
            style = if (today || selected) HelaTheme.typography.label14 else HelaTheme.typography.compact,
            color = when {
                selected -> c.onInverseSurface
                !enabled -> c.textDisabled
                else -> c.textPrimary
            },
        )
    }
}

private val UTC: TimeZone = TimeZone.getTimeZone("UTC")
private const val DAY_MS = 86_400_000L

@Composable
private fun currentLocale(): Locale = LocalConfiguration.current.locales[0] ?: Locale.getDefault()

private fun startOfDay(millis: Long): Long = Math.floorDiv(millis, DAY_MS) * DAY_MS

private fun utcCalendar(millis: Long): Calendar = Calendar.getInstance(UTC).apply { timeInMillis = millis }

/** Months since year 0, so prev/next is just -1/+1. */
private fun monthIndex(millis: Long): Int = utcCalendar(millis).let { it[Calendar.YEAR] * 12 + it[Calendar.MONTH] }

private fun dayOfMonth(millis: Long): Int = utcCalendar(millis)[Calendar.DAY_OF_MONTH]

private fun monthTitle(month: Int, locale: Locale): String =
    SimpleDateFormat("LLLL yyyy", locale).apply { timeZone = UTC }
        .format(HelaDates.utcMillis(month / 12, month % 12 + 1, 1))

/** Six weeks of days for [month], null for the blanks before the 1st and after the last day. */
private fun monthGrid(month: Int, firstDayOfWeek: Int): List<Long?> {
    val first = HelaDates.utcMillis(month / 12, month % 12 + 1, 1)
    val cal = utcCalendar(first)
    val leading = (cal[Calendar.DAY_OF_WEEK] - firstDayOfWeek + 7) % 7
    val length = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    return List(42) { i ->
        val day = i - leading
        if (day in 0 until length) first + day * DAY_MS else null
    }
}

@HelaPreviews
@Composable
private fun HelaCalendarPreview() = HelaPreviewSurface {
    var date by remember { mutableStateOf<Long?>(HelaDates.utcMillis(2026, 10, 16)) }
    HelaCalendar(
        date, { date = it },
        minDateMillis = HelaDates.utcMillis(2026, 10, 5),
        maxDateMillis = HelaDates.utcMillis(2026, 11, 30),
    )
}

@HelaPreviews
@Composable
private fun HelaDatePickerModalPreview() = HelaPreviewSurface {
    val date = HelaDates.utcMillis(2026, 10, 30)
    HelaModalPanel(
        title = "Due date",
        description = HelaDates.format(date),
        actions = {
            HelaButton("Cancel", onClick = {}, variant = HelaButtonVariant.Secondary)
            HelaButton("Select", onClick = {})
        },
    ) {
        HelaCalendar(date, {})
    }
}
