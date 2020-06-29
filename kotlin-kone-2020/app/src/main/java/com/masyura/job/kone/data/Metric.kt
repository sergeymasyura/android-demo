package com.masyura.job.kone.data

import androidx.annotation.StringRes

data class Metric(
    @StringRes val titleResId: Int,
    val value: Double,
    @StringRes val valueFormatResIs: Int
)