package com.example.spotcheck.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.spotcheck.R
import com.example.spotcheck.models.Penyakit
import com.example.spotcheck.models.Riwayat

class Adapter_Riwayat (var mCtx: Context, var resource:Int, var items:List<Riwayat>)
    : ArrayAdapter<Riwayat>( mCtx , resource , items ){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(resource , null )
        var tvCreatedAt : TextView = view.findViewById(R.id.tvCreatedAt)
        var tvPenyakit : TextView = view.findViewById(R.id.tvPenyakit)


        var riwayat : Riwayat = items[position]

        tvPenyakit.text = riwayat.hasil
        tvCreatedAt.text = riwayat.created_at


        return view
    }

}