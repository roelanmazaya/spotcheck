package com.example.spotcheck.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.spotcheck.R
import com.example.spotcheck.models.Penyakit

class Adapter_Penyakit (var mCtx: Context, var resource:Int, var items:List<Penyakit>)
    : ArrayAdapter<Penyakit>( mCtx , resource , items ){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(resource , null )
        var tvPenyakit : TextView = view.findViewById(R.id.tvPenyakit)


        var penyakit : Penyakit = items[position]

        tvPenyakit.text = penyakit.id.toString()+". "+penyakit.hasil


        return view
    }

}