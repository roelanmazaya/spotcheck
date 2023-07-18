package com.example.spotcheck.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import com.example.spotcheck.R
import com.example.spotcheck.models.Pertanyaan_Model

class Adapter_InputArray(
    var mCtx: Context,
    var resource: Int,
    var items: List<Pertanyaan_Model>
) : ArrayAdapter<Pertanyaan_Model>(mCtx, resource, items) {

    private val selectedItems = mutableMapOf<Int, Boolean>()

    init {
        // Set default selected state to false for all items
        for (i in items.indices) {
            selectedItems[i] = false
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        val view: View = convertView ?: layoutInflater.inflate(resource, null)
        val tvIDPertanyaanPopup: TextView = view.findViewById(R.id.tvIDPertanyaan_popup)
        val tvPertanyaanPopup: TextView = view.findViewById(R.id.tvPertanyaan_popup)
        val rbIya: RadioButton = view.findViewById(R.id.radiobtn_popup_iya)
        val rbTidak: RadioButton = view.findViewById(R.id.radiobtn_popup_tidak)

        val pertanyaanPopup: Pertanyaan_Model = items[position]

        tvIDPertanyaanPopup.text = pertanyaanPopup.id.toString()
        tvPertanyaanPopup.text = pertanyaanPopup.pertanyaan

        // Set click listener for radio buttons
        rbIya.setOnClickListener {
            selectedItems[position] = true
            rbTidak.isChecked = false
        }

        rbTidak.setOnClickListener {
            selectedItems[position] = false
            rbIya.isChecked = false
        }

        // Set the initial selected state for each radio button
        val selected = selectedItems[position] ?: false
        rbIya.isChecked = selected
        rbTidak.isChecked = !selected

        return view
    }

    // Function to get the selected items
    fun getSelectedItems(): List<Boolean> {
        val selectedList = mutableListOf<Boolean>()
        for (i in items.indices) {
            val selected = selectedItems[i] ?: false
            selectedList.add(selected)
        }
        return selectedList
    }
}
