package com.example.spotcheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotcheck.adapter.Adapter_Penyakit
import com.example.spotcheck.databinding.AdminPenyakitPageBinding
import com.example.spotcheck.models.Hasil
import com.example.spotcheck.models.Penyakit
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Admin_Penyakit : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: AdminPenyakitPageBinding

    private lateinit var listAdapter: Adapter_Penyakit
    private lateinit var db: FirebaseFirestore
    private var listPenyakit = mutableListOf<Penyakit>()
    private lateinit var hasil: Hasil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminPenyakitPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        listPenyakit = mutableListOf<Penyakit>()
        listAdapter = Adapter_Penyakit(this, R.layout.item_penyakit, listPenyakit)
        binding.lvDataHasil.adapter = listAdapter

        binding.btnTambahPenyakitAdmin.setOnClickListener(this)
        binding.lvDataHasil.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, Admin_EditPenyakit::class.java)
            intent.putExtra("docId", listPenyakit[position].docId)
            intent.putExtra("id", listPenyakit[position].id.toString())
            intent.putExtra("hasil", listPenyakit[position].hasil)
            intent.putExtra("pict", listPenyakit[position].pict)
            intent.putExtra("pict2", listPenyakit[position].pict2)
            intent.putExtra("pict3", listPenyakit[position].pict3)
            intent.putExtra("solusi", listPenyakit[position].solusi)
            intent.putExtra("array_hasil", listPenyakit[position].array_hasil?.joinToString())
            intent.putExtra("index_ke", position.toString())
            startActivity(intent)
        }
    }

    private fun getPenyakit() {
        listPenyakit.clear()
        db = Firebase.firestore
        db.collection("hasil").get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val listDoc: List<DocumentSnapshot> = result.documents
                    Log.d("Hasil", "getPenyakit: " + listDoc.size)
                    for (d in listDoc) {
                        hasil = d.toObject(Hasil::class.java)!!
                        if (hasil.hasil != null && !hasil.hasil.isNullOrEmpty()) {
                            val penyakit = Penyakit(
                                docId = d.id,
                                id = hasil.id,
                                hasil = hasil.hasil,
                                pict = hasil.pict,
                                pict2 = hasil.pict2,
                                pict3 = hasil.pict3,
                                solusi = hasil.solusi,
                                array_hasil = hasil.array_hasil
                            )
                            listPenyakit.add(penyakit)
                        }
                    }

                    listAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Gagal mendapatkan data: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        getPenyakit()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTambahPenyakitAdmin -> {
                val intent = Intent(this, Admin_TambahPenyakit::class.java)
                startActivity(intent)
            }
        }
    }
}
