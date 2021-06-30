package com.wolt.android.demo

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class PlacesListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationView: TextView
    private lateinit var progressView: ProgressBar

    private val viewModel by viewModels<PlacesListViewModel> {
        PlacesListViewModelFactory(this.applicationContext)
    }

    private lateinit var placesListAdapter: RestaurantNearbyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_list)

        placesListAdapter = RestaurantNearbyAdapter { place, position ->
            viewModel.onFavoriteClick(place, position)
        }
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = placesListAdapter

        progressView = findViewById(R.id.progress)
        notificationView = findViewById(R.id.status)

        viewModel.onShowProgress.observe(this, { event ->
            event.getContentIfNotHandled()?.let {
                progressView.visibility = if (it.isVisible) View.VISIBLE else View.GONE
            }
        })

        viewModel.onShowPlaces.observe(this, { event ->
            event.getContentIfNotHandled()?.let {
                if (it.places.any()) {
                    notificationView.visibility = View.GONE
                    progressView.visibility = View.GONE
                    placesListAdapter.updateItems(it.places)
                    recyclerView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.GONE
                }
            }
        })

        viewModel.onShowStatus.observe(this, { event ->
            event.getContentIfNotHandled()?.let {
                if (it.messageResId > 0) {
                    recyclerView.visibility = View.GONE
                    notificationView.setText(it.messageResId)
                    notificationView.visibility = View.VISIBLE
                } else {
                    notificationView.visibility = View.GONE
                }
            }
        })

        viewModel.onUpdateItem.observe(this, { event ->
            event.getContentIfNotHandled()?.let {
                if (it.position > -1) {
                    placesListAdapter.updateItem(it.place, it.position)
                }
            }
        })

        viewModel.onShowToast.observe(this, { event ->
            event.getContentIfNotHandled()?.let {
                if (it.messageResId > 0) {
                    Toast.makeText(this, it.messageResId, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}