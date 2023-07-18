package com.example.spotcheck.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.spotcheck.R
import com.example.spotcheck.models.Pertanyaan_Model

class Adapter_Pertanyaaan (var mCtx: Context, var resource:Int, var items:List<Pertanyaan_Model>)
    : ArrayAdapter<Pertanyaan_Model>( mCtx , resource , items ){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(resource , null )
        var tvPertanyaan : TextView = view.findViewById(R.id.tvPertanyaan)


        var pertanyaan : Pertanyaan_Model = items[position]

        tvPertanyaan.text = pertanyaan.id.toString()+". "+pertanyaan.pertanyaan


        return view
    }

}