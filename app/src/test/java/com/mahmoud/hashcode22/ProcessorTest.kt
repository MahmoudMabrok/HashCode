package com.mahmoud.hashcode22

import org.junit.Assert.*

import org.junit.Test

class ProcessorTest {

    private val processor = Processor()

    @Test
    fun process() {
        processor.process("2 tomato eeg" , isLike = true)
        processor.process("1 eeg" , isLike = false)

        val expected = mapOf(
            "eeg" to Gradient("eeg" , likes = 1 , dislikes = 1) ,
            "tomato" to Gradient("tomato" , likes = 1)
        )
        assertEquals( expected,processor.data)
    }

    @Test
    fun formatOutput() {
        processor.process("2 tomato eeg" , isLike = true)
        processor.process("1 eeg" , isLike = false)

        val expected = "1 tomato"
        assertEquals(expected , processor.formatOutput())
    }
}