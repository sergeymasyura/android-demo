package com.wolt.android.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wolt.android.demo.data.Place

class RestaurantNearbyAdapter(
    private val onFavoriteClick: (restaurant: Place, position: Int) -> Unit
) : RecyclerView.Adapter<RestaurantNearbyAdapter.ViewHolder>() {

    private val items = mutableListOf<Place>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return ViewHolder(view, onFavoriteClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(newItems: List<Place>) {
        val diffResult = DiffUtil.calculateDiff(
            PlaceDiffCallback(items, newItems)
        )

        items.clear()
        items.addAll(newItems)

        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(item: Place, position: Int) {
        items[position] = item
        notifyItemChanged(position)
    }

    class ViewHolder(
        itemView: View,
        val onFavoriteClick: (place: Place, position: Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        private val imageView: ImageView = itemView.findViewById(R.id.image)
        private val favoriteView: ImageView = itemView.findViewById(R.id.favorite)

        fun bind(place: Place) {
            titleTextView.text = place.name
            descriptionTextView.text = place.description
            imageView.load(place.imageUrl)
            favoriteView.isSelected = place.favorite
            favoriteView.setOnClickListener {
                onFavoriteClick(place, adapterPosition)
            }
        }
    }
}

class PlaceDiffCallback(
    private val oldList: List<Place>,
    private val newList: List<Place>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]

        return old.id == new.id && old.name == new.name
    }
}

