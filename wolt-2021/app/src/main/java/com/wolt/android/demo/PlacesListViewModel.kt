package com.wolt.android.demo

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wolt.android.demo.data.IPlacesRepository
import com.wolt.android.demo.data.Place
import com.wolt.android.demo.data.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlacesListViewModel(
    private val placesRepository: IPlacesRepository
) : ViewModel() {

    data class ShowProgressAction(val isVisible: Boolean)
    data class ShowPlacesAction(val places: List<Place>)
    data class ShowStatusAction(@StringRes val messageResId: Int)
    data class UpdateItemAction(val place: Place, val position: Int)
    data class ShowToastAction(@StringRes val messageResId: Int)

    private val _onShowProgress = MutableLiveData<LiveEvent<ShowProgressAction>>()
    private val _onShowPlaces = MutableLiveData<LiveEvent<ShowPlacesAction>>()
    private val _onShowStatus = MutableLiveData<LiveEvent<ShowStatusAction>>()
    private val _onUpdateItem = MutableLiveData<LiveEvent<UpdateItemAction>>()
    private val _onShowToast = MutableLiveData<LiveEvent<ShowToastAction>>()

    val onShowProgress: LiveData<LiveEvent<ShowProgressAction>>
        get() = _onShowProgress

    val onShowPlaces: LiveData<LiveEvent<ShowPlacesAction>>
        get() = _onShowPlaces

    val onShowStatus: LiveData<LiveEvent<ShowStatusAction>>
        get() = _onShowStatus

    val onUpdateItem: LiveData<LiveEvent<UpdateItemAction>>
        get() = _onUpdateItem

    val onShowToast: LiveData<LiveEvent<ShowToastAction>>
        get() = _onShowToast

    init {
        _onShowProgress.postValue(
            LiveEvent(
                ShowProgressAction(isVisible = true)
            )
        )

        viewModelScope.launch {
            placesRepository.places.collect {

                _onShowProgress.postValue(
                    LiveEvent(
                        ShowProgressAction(isVisible = false)
                    )
                )

                when (it) {
                    is Result.Success -> {
                        if (it.data.any()) {
                            _onShowPlaces.postValue(
                                LiveEvent(
                                    ShowPlacesAction(it.data)
                                )
                            )
                        } else {
                            _onShowStatus.postValue(
                                LiveEvent(
                                    ShowStatusAction(R.string.notification_search_empty)
                                )
                            )
                        }
                    }
                    is Result.Error -> {
                        _onShowStatus.postValue(
                            LiveEvent(
                                ShowStatusAction(R.string.notification_search_failure)
                            )
                        )
                    }
                }
            }
        }
    }

    fun onFavoriteClick(place: Place, position: Int) {
        viewModelScope.launch {
            val result = placesRepository.toggleFavorite(place)

            when (result) {
                is Result.Success -> {
                    _onUpdateItem.postValue(
                        LiveEvent(
                            UpdateItemAction(
                                place = place,
                                position = position
                            )
                        )
                    )
                }
                is Result.Error -> {
                    _onShowToast.postValue(
                        LiveEvent(
                            ShowToastAction(
                                messageResId = R.string.notification_favorite_failure
                            )
                        )
                    )
                }
            }
        }
    }
}

class PlacesListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlacesListViewModel::class.java)) {

            val placesRepository = (context.applicationContext as WoltApplication).placesRepository

            @Suppress("UNCHECKED_CAST")
            return PlacesListViewModel(placesRepository = placesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
