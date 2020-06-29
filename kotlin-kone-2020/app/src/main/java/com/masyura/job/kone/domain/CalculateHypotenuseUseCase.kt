package com.masyura.job.kone.domain

import kotlin.math.hypot

class CalculateHypotenuseUseCase {

    operator fun invoke(sideA: Double, sideB: Double): Double {
        return hypot(sideA, sideB)
    }
}