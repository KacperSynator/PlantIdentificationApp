package com.example.plantidentificationapp.adapters

import android.app.Activity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.plantidentificationapp.R
import com.example.plantidentificationapp.classes.MapMarkerData

class PlantAdapter (private val context : Activity, private val arrayList: ArrayList<MapMarkerData>) : ArrayAdapter<MapMarkerData>(context,
    R.layout.listitem_myplants, arrayList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.listitem_myplants, null)

        val plant = arrayList[position].imageURL
        val imageView : ImageView = view.findViewById(R.id.plant_image)
        val textView : TextView = view.findViewById(R.id.plant_name)

        Glide.with(context)
            .load(plant)
            .into(imageView)

        textView.text = arrayList[position].title

        return view
    }
}