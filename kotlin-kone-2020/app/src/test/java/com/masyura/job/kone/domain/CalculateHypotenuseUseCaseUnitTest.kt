package com.masyura.job.kone.domain

import org.junit.Test

import org.junit.Assert.*

class CalculateHypotenuseUseCaseUnitTest {
    @Test
    fun calculate_isCorrect() {
        val hypotenuse = CalculateHypotenuseUseCase().invoke(10.0, 20.0)
        val delta = 0.0000001

        assertEquals("hypotenuse is correct", 22.360679774997898, hypotenuse, delta)
    }
}