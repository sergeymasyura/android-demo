package com.masyura.job.kone.domain

import kotlin.math.asin

class CalculateAngleInDegreesUseCase {

    operator fun invoke(side: Double, hypotenuse: Double): Double {
        val radians = asin(side / hypotenuse)
        return Math.toDegrees(radians)
    }
}