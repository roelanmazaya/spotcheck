package com.example.spotcheck

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotcheck.adapter.Adapter_InputArray
import com.example.spotcheck.databinding.AdminTambahpenyakitPageBinding
import com.example.spotcheck.models.Hasil
import com.example.spotcheck.models.Pertanyaan_Model
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Admin_TambahPenyakit : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: AdminTambahpenyakitPageBinding

    private lateinit var db: FirebaseFirestore
    private lateinit var uri: Uri
    private var currentUser: FirebaseUser? = null
    private lateinit var hasil: Hasil
    private var last_id = 0
    var total_row = 0
    private lateinit var pertanyaanAdapter: Adapter_InputArray
    private lateinit var pertanyaanList: List<Pertanyaan_Model>

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminTambahpenyakitPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = Firebase.firestore
        db.collection("hasil").orderBy("id", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val listDoc: List<DocumentSnapshot> = result.documents
                    for (d in listDoc) {
                        hasil = d.toObject(Hasil::class.java)!!
                        last_id = hasil.id!!
                        Log.d("Last Id Hasil", "onCreate: " + (last_id + 1))
                    }
                    binding.inputIDPenyakitAdmin.setText((last_id + 1).toString())

                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Gagal mendapatkan data: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        db = Firebase.firestore
        db.collection("pertanyaan").get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    pertanyaanList = result.toObjects(Pertanyaan_Model::class.java)
                    total_row = pertanyaanList.size - 1
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Gagal mendapatkan data: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        binding.btnTambahPenyakit.setOnClickListener(this)
        binding.btnMasukkanArray.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTambahPenyakit -> {
                val id = binding.inputIDPenyakitAdmin.text.trim().toString()
                val penyakit = binding.inputPenyakitAdmin.text.trim().toString()
                val pict = binding.inputGambarPenyakitAdmin.text.trim().toString()
                val pict2 = binding.inputGambarPenyakitAdmin2.text.trim().toString()
                val pict3 = binding.inputGambarPenyakitAdmin3.text.trim().toString()
                val solusi = binding.inputSolusiAdmin.text.trim().toString()
                val array_hasil = binding.inputArrayPenyakitAdmin.text.trim().toString()
                val exp_hasil = array_hasil.split(",")

                var listHasil: ArrayList<Int>
                listHasil = ArrayList()
                listHasil.clear()

                if (exp_hasil.size < total_row) {
                    Toast.makeText(
                        this,
                        "Array Penyakit yang diinputkan (${exp_hasil.size}) tidak boleh kurang dari " + total_row.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    binding.inputArrayPenyakitAdmin.requestFocus()
                } else if (exp_hasil.size > total_row) {
                    Toast.makeText(
                        this,
                        "Array Penyakit yang diinputkan (${exp_hasil.size}) tidak boleh lebih dari " + total_row.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    binding.inputArrayPenyakitAdmin.requestFocus()
                } else {
                    for (i in exp_hasil) {
                        listHasil.add(i.trim().toInt())
                    }

                    val hasil = Hasil(id.toInt(), penyakit, pict, pict2, pict3, solusi, listHasil)

                    db.collection("hasil")
                        .document(id)
                        .set(hasil)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Data penyakit ditambahkan!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Gagal: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }

            }
            R.id.btnMasukkanArray -> {
                showArrayDialog()
            }
        }
    }

    private fun showArrayDialog() {
        val dialogView = layoutInflater.inflate(R.layout.popup_inputarraypenyakit_list, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.lvpopup_Pertanyaan)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = Adapter_InputArray(this, R.layout.popup_inputarraypenyakit, pertanyaanList)
        recyclerView.adapter = adapter

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val selectedItems = adapter.getSelectedItems()
            val arrayBuilder = StringBuilder()
            for (i in selectedItems.indices) {
                if (selectedItems[i]) {
                    arrayBuilder.append("1")
                } else {
                    arrayBuilder.append("-1")
                }
                if (i != selectedItems.size - 1) {
                    arrayBuilder.append(",")
                }
            }
            binding.inputArrayPenyakitAdmin.setText(arrayBuilder.toString())
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}
