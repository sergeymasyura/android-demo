package com.masyura.job.kone.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.masyura.job.kone.android.LiveEvent
import com.masyura.job.kone.domain.CalculateMetricsUseCase
import com.masyura.job.kone.data.Metric

class MainViewModel : ViewModel() {

    private val LOG_TAG by lazy { javaClass.simpleName }

    private var inputSideAMillimeters: Double? = null
    private var inputSideBMillimeters: Double? = null

    var outputMetrics = listOf<Metric>()

    data class CanCalculateAction(val canCalculate: Boolean)

    private val _canCalculateLiveData = MutableLiveData<LiveEvent<CanCalculateAction>>()
    val canCalculateLiveData: LiveData<LiveEvent<CanCalculateAction>>
        get() = _canCalculateLiveData

    data class ShowOutputAction(val metrics: List<Metric>)

    private val _showOutputLiveData = MutableLiveData<LiveEvent<ShowOutputAction>>()
    val showOutputLiveData: LiveData<LiveEvent<ShowOutputAction>>
        get() = _showOutputLiveData

    fun onSideATextChanged(text: CharSequence?) {
        inputSideAMillimeters = text?.toString()?.toDoubleOrNull()

        notifyOnCalculateChanged()
        clearMetrics()
        notifyMetricsChanged()
    }

    fun onSideBTextChanged(text: CharSequence?) {
        inputSideBMillimeters = text?.toString()?.toDoubleOrNull()

        notifyOnCalculateChanged()
        clearMetrics()
        notifyMetricsChanged()
    }

    fun onActionButtonClicked() {
        if (canCalculate()) {
            outputMetrics = CalculateMetricsUseCase().invoke(
                sideAMillimeters = requireNotNull(inputSideAMillimeters),
                sideBMillimeters = requireNotNull(inputSideBMillimeters)
            )

            notifyMetricsChanged()
        }
    }

    fun canCalculate(): Boolean {
        val inputs = listOf(inputSideAMillimeters, inputSideBMillimeters)

        return inputs.all {
            it != null && it > 0
        }
    }

    private fun notifyOnCalculateChanged() {
        _canCalculateLiveData.postValue(
            LiveEvent(
                CanCalculateAction(
                    canCalculate = canCalculate()
                )
            )
        )
    }

    private fun clearMetrics() {
        outputMetrics = listOf()
    }

    private fun notifyMetricsChanged() {
        _showOutputLiveData.postValue(
            LiveEvent(
                ShowOutputAction(
                    metrics = outputMetrics
                )
            )
        )
    }
}