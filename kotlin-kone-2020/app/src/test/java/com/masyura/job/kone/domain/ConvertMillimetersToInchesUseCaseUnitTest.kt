package com.masyura.job.kone.domain

import org.junit.Test

import org.junit.Assert.*

class ConvertMillimetersToInchesUseCaseUnitTest {
    @Test
    fun convert_isCorrect() {
        val inches = ConvertMillimetersToInchesUseCase().invoke(1000.0)
        val delta = 0.0000001

        assertEquals("1000 millimeters = 39.3700787 inches", 39.3700787, inches, delta)
    }
}