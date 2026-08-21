package dev.slne.surf.lobby.core.client.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class YearProgressTest {

    @Test
    fun `first day of the year is one day worth of progress`() {
        assertEquals(1f / 365f, yearProgress(LocalDate.of(2026, 1, 1)))
    }

    @Test
    fun `last day of the year is full progress`() {
        assertEquals(1f, yearProgress(LocalDate.of(2026, 12, 31)))
    }

    @Test
    fun `leap years use their actual length`() {
        assertEquals(1f / 366f, yearProgress(LocalDate.of(2028, 1, 1)))
        assertEquals(1f, yearProgress(LocalDate.of(2028, 12, 31)))
    }
}
