package com.masyura.job.kone.domain

class CalculateAreaUseCase {

    operator fun invoke(sideA: Double, sideB: Double): Double {
        return 0.5 * sideA * sideB
    }
}