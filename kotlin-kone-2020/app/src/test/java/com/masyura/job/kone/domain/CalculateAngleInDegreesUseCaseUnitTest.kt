package com.masyura.job.kone.domain

import org.junit.Test

import org.junit.Assert.*

class CalculateAngleInDegreesUseCaseUnitTest {
    @Test
    fun calculate_isCorrect() {
        val hypotenuse = CalculateAngleInDegreesUseCase().invoke(10.0, 22.36068)
        val delta = 0.0001

        assertEquals("angle is correct", 26.565, hypotenuse, delta)
    }
}