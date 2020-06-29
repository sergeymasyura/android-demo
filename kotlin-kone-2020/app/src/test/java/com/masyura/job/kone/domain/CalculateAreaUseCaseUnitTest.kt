package com.masyura.job.kone.domain

import org.junit.Test

import org.junit.Assert.*

class CalculateAreaUseCaseUnitTest {
    @Test
    fun calculate_isCorrect() {
        val area = CalculateAreaUseCase().invoke(10.0, 20.0)
        val delta = 0.0000001

        assertEquals("hypotenuse is correct", 100.0, area, delta)
    }
}