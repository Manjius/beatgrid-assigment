package com.beatgridmedia.moviedb.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FrontPageStateHolderTest {
    private val stateHolder = FrontPageStateHolder()

    @Test
    fun `blank query hides dropdown`() {
        val state = stateHolder.updateQuery("")

        assertFalse(state.isDropdownVisible)
        assertTrue(state.suggestions.isEmpty())
    }

    @Test
    fun `non blank query shows default movie suggestion`() {
        val state = stateHolder.updateQuery("the")

        assertTrue(state.isDropdownVisible)
        assertEquals(listOf("the movie goes here"), state.suggestions)
    }
}
