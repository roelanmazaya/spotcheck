package com.example.spotcheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotcheck.adapter.Adapter_Pertanyaaan
import com.example.spotcheck.databinding.AdminPertanyaanPageBinding
import com.example.spotcheck.models.Pertanyaan
import com.example.spotcheck.models.Pertanyaan_Model
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Admin_Pertanyaan : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: AdminPertanyaanPageBinding

    private lateinit var listAdapter: Adapter_Pertanyaaan
    private lateinit var db: FirebaseFirestore
    private var listPertanyaan = mutableListOf<Pertanyaan_Model>()
    private lateinit var pertanyaan: Pertanyaan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminPertanyaanPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        listPertanyaan = mutableListOf<Pertanyaan_Model>()
        listAdapter = Adapter_Pertanyaaan(this,R.layout.item_pertanyaan, listPertanyaan)
        binding.lvDataPertanyaan.adapter = listAdapter

        binding.btnTambahQAdmin.setOnClickListener(this)



        binding.lvDataPertanyaan.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, Admin_EditPertanyaan::class.java)
            intent.putExtra("docId", listPertanyaan.get(position).docId)
            intent.putExtra("id", listPertanyaan.get(position).id.toString())
            intent.putExtra("pertanyaan", listPertanyaan.get(position).pertanyaan)
            intent.putExtra("index_ke", position.toString())
            startActivity(intent)
        }
    }

    fun get_pertanyaan(){
        listPertanyaan.clear()
        db = Firebase.firestore
        db.collection("pertanyaan").get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val listDoc: List<DocumentSnapshot> = result.documents
                    Log.d("Pertanyaan", "get_pertanyaan: "+listDoc.size)
                    var total_row = listDoc.size - 1
                    var i = 1
                    for (d in listDoc) {
                        if(i <= total_row){
                            pertanyaan = d.toObject(Pertanyaan::class.java)!!
                            listPertanyaan.add(Pertanyaan_Model(d.id, pertanyaan.id, pertanyaan.pertanyaan))
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
        get_pertanyaan()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTambahQAdmin -> {
                val intent = Intent(this, Admin_TambahPertanyaan::class.java)
                startActivity(intent)
            }
        }
    }

}
