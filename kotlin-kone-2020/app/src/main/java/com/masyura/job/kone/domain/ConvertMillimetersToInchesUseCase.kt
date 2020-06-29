package com.masyura.job.kone.domain

class ConvertMillimetersToInchesUseCase {

    companion object {
        const val MILLIMETERS_IN_ONE_INCH = 25.4
    }

    operator fun invoke(millimeters: Double): Double {
        return millimeters / MILLIMETERS_IN_ONE_INCH
    }
}