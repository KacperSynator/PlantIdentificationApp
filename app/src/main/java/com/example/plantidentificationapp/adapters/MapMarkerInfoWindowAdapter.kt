package com.example.plantidentificationapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.plantidentificationapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.bumptech.glide.Glide


class MapMarkerInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    private val view: View = LayoutInflater.from(context).inflate(R.layout.map_marker_info_window_layout, null)

    override fun getInfoContents(marker: Marker): View? {
        val title = view.findViewById<TextView>(R.id.location_title)
        val image = view.findViewById<ImageView>(R.id.location_image)
        val description = view.findViewById<TextView>(R.id.location_description)

        title.text = marker.title
        description.text = marker.snippet

        // Load the image using a library like Picasso or Glide
        val imageUrl = marker.tag as? String // Assume marker tag contains image URL

        if (imageUrl != null) {
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.loading)
                .into(image)
        }

        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        // Return null to use default frame with custom contents
        return null
    }
}
