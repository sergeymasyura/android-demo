package com.masyura.job.kone.domain

import com.masyura.job.kone.R
import com.masyura.job.kone.data.Metric

class CalculateMetricsUseCase {

    operator fun invoke(sideAMillimeters: Double, sideBMillimeters: Double): List<Metric> {
        val metrics = mutableListOf<Metric>()

        val hypotenuseMillimeters = CalculateHypotenuseUseCase().invoke(sideAMillimeters, sideBMillimeters)

        metrics.add(
            Metric(
                titleResId = R.string.output_title_hypotenuse,
                value = ConvertMillimetersToInchesUseCase().invoke(hypotenuseMillimeters),
                valueFormatResIs = R.string.output_value_inch
            )
        )

        metrics.add(
            Metric(
                titleResId = R.string.output_title_angle_a,
                value = CalculateAngleInDegreesUseCase().invoke(
                    sideAMillimeters,
                    hypotenuseMillimeters
                ),
                valueFormatResIs = R.string.output_value_angle
            )
        )

        metrics.add(
            Metric(
                titleResId = R.string.output_title_angle_b,
                value = CalculateAngleInDegreesUseCase().invoke(
                    sideBMillimeters,
                    hypotenuseMillimeters
                ),
                valueFormatResIs = R.string.output_value_angle
            )
        )

        val sideAInches = ConvertMillimetersToInchesUseCase().invoke(sideAMillimeters)
        val sideBInches = ConvertMillimetersToInchesUseCase().invoke(sideBMillimeters)

        metrics.add(
            Metric(
                titleResId = R.string.output_title_area,
                value = CalculateAreaUseCase().invoke(sideAInches, sideBInches),
                valueFormatResIs = R.string.output_value_square_inch
            )
        )

        return metrics
    }
}