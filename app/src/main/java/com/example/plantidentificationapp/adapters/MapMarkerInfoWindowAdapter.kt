package com.example.plantidentificationapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.plantidentificationapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class MapMarkerInfoWindowAdapter(val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.map_marker_info_window_layout, null)

        val titleTextView = view.findViewById<TextView>(R.id.location_title)
        val descriptionTextView = view.findViewById<TextView>(R.id.location_description)
        val imageView = view.findViewById<ImageView>(R.id.location_image)

        titleTextView.text = marker.title
        descriptionTextView.text = marker.snippet

        Glide.with(context)
            .load(marker.tag as? String) // Assume marker tag contains image URL
            .into(imageView)

        return view
    }
}
