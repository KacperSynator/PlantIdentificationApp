package com.example.plantidentificationapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.plantidentificationapp.R
import com.example.plantidentificationapp.classes.MenuItem

class MenuAdapter (private val context : Activity, private val arrayList: ArrayList<MenuItem>) : ArrayAdapter<MenuItem>(context,
    R.layout.list_item_main, arrayList)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.list_item_main, null);

        val imageView : ImageView = view.findViewById(R.id.menuPictureView)
        val textView : TextView = view.findViewById(R.id.menuText)

        imageView.setImageResource(arrayList[position].imageId)
        textView.text = arrayList[position].name

        return view
    }
}