package com.masyura.job.kone.android

/**
 * Wrapper to consume event only once.
 */
open class LiveEvent<out T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}
