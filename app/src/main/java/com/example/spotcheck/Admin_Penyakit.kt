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
        listAdapter = Adapter_Penyakit(this,R.layout.item_penyakit, listPenyakit)
        binding.lvDataHasil.adapter = listAdapter

        binding.btnTambahPenyakitAdmin.setOnClickListener(this)

//        if (listPertanyaan.isNotEmpty()) {
//            binding.lvDataPertanyaan.adapter = listAdapter
//        }

        binding.lvDataHasil.setOnItemClickListener { parent, view, position, id ->
//            Toast.makeText(this,  "Edit data penyakit belum bisa diakses", Toast.LENGTH_LONG).show()
            val intent = Intent(this, Admin_EditPenyakit::class.java)
            intent.putExtra("docId", listPenyakit.get(position).docId)
            intent.putExtra("id", listPenyakit.get(position).id.toString())
            intent.putExtra("hasil", listPenyakit.get(position).hasil)
            intent.putExtra("pict", listPenyakit.get(position).pict)
            intent.putExtra("solusi", listPenyakit.get(position).solusi)
            val arr_hasil = listPenyakit.get(position).array_hasil
            var array_hasil = ""
            if (arr_hasil != null) {
                for (i in arr_hasil){
                    array_hasil += i.toString()
                }
            }
            intent.putExtra("array_hasil", arr_hasil!!.joinToString())
            intent.putExtra("index_ke", position.toString())
            Log.d("Array_Hasil_Old", "onCreate: "+listPenyakit.get(position).array_hasil)
            startActivity(intent)
        }
    }

    fun get_penyakit(){
        listPenyakit.clear()
        db = Firebase.firestore
        db.collection("hasil").get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val listDoc: List<DocumentSnapshot> = result.documents
                    Log.d("Hasil", "get_penyakit: "+listDoc.size)
                    var total_row = listDoc.size - 1
                    var i = 1
                    for (d in listDoc) {
                        if(i <= total_row){
                            hasil = d.toObject(Hasil::class.java)!!
                            listPenyakit.add(Penyakit(d.id, hasil.id, hasil.hasil, hasil.pict, hasil.solusi, hasil.array_hasil))
                        }
                        i++
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
        get_penyakit()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTambahPenyakitAdmin -> {
//                Toast.makeText(this, "Belum bisa diakses", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Admin_TambahPenyakit::class.java)
                startActivity(intent)
            }
        }
    }

}
