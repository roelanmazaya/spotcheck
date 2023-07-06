package com.example.spotcheck.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.spotcheck.R
import com.example.spotcheck.models.Riwayat
import com.example.spotcheck.models.Riwayat_model

class Adapter_Riwayat(var mCtx: Context, var resource:Int, var items: MutableList<Riwayat_model>)
    : ArrayAdapter<Riwayat_model>( mCtx , resource , items ){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(resource , null )
        var tvCreatedAt : TextView = view.findViewById(R.id.tvCreatedAt)
        var tvPenyakit : TextView = view.findViewById(R.id.tvPenyakit)


        var riwayat : Riwayat_model = items[position]

//        tvPenyakit.text = riwayat.no.toString()+". "+riwayat.hasil
        tvPenyakit.text = riwayat.hasil
        tvCreatedAt.text = "("+riwayat.created_at+")"


        return view
    }

}