package dev.slne.surf.lobby.core.client.util

import java.time.LocalDate

/**
 * The progress of [date]'s year as a fraction between 0 and 1, based on the day of the year.
 */
fun yearProgress(date: LocalDate = LocalDate.now()): Float {
    val dayOfYear = date.dayOfYear
    val daysInYear = date.lengthOfYear()

    return dayOfYear.toFloat() / daysInYear
}
