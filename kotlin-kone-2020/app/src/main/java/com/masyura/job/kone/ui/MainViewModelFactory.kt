package com.masyura.job.kone.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory : ViewModelProvider.Factory {

    private val LOG_TAG by lazy { javaClass.simpleName }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(MainViewModel::class.java))

        return MainViewModel() as T
    }
}