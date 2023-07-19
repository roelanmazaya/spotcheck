package com.example.spotcheck

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotcheck.databinding.AdminTambahpertanyaanPageBinding
import com.example.spotcheck.models.Hasil
import com.example.spotcheck.models.Pertanyaan
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Admin_TambahPertanyaan : AppCompatActivity(), View.OnClickListener{

    lateinit var binding: AdminTambahpertanyaanPageBinding

    private lateinit var db: FirebaseFirestore
    private lateinit var uri: Uri
    private var currentUser: FirebaseUser? = null
    private lateinit var pertanyaan: Pertanyaan
    private lateinit var hasil: Hasil
    private var last_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminTambahpertanyaanPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = Firebase.firestore
        db.collection("pertanyaan").orderBy("id", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val listDoc: List<DocumentSnapshot> = result.documents
                    for (d in listDoc) {
                        pertanyaan = d.toObject(Pertanyaan::class.java)!!
                        last_id = pertanyaan.id!!
                        Log.d("Last Id Pertanyaan", "onCreate: "+(last_id+1))
                    }
                    binding.inputIDPertanyaan.setText((last_id+1).toString())

                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Gagal mendapatkan data: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        binding.btnTambahPertanyaan.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            com.example.spotcheck.R.id.btnTambahPertanyaan -> {
                val id = binding.inputIDPertanyaan.text.trim().toString()
                val p = binding.InputPertanyaanAdmin.text.trim().toString()
                val pertanyaan = Pertanyaan(id.toInt(), p)

                Log.d("Tambah Pertanyaan", "onClick: "+pertanyaan)
                db.collection("pertanyaan")
                    .document(id)
                    .set(pertanyaan)
                    .addOnSuccessListener {
                        update_hasil()
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
    }

    private fun update_hasil() {
        var listHasil: ArrayList<Int>
        db = Firebase.firestore
        db.collection("hasil").get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val listDoc: List<DocumentSnapshot> = result.documents
                    var total_row = listDoc.size - 1
                    var i = 1
                    for (d in listDoc)
                    {
                        Log.d("Tambah Pertanyaan Hasil", "update_hasil: ${d.id}")

                        if(i <= total_row)
                        {

                            hasil = d.toObject(Hasil::class.java)!!

                            var array_hasil: ArrayList<Int>

                            array_hasil = (hasil.array_hasil as ArrayList<Int>?)!!
                            listHasil = ArrayList()

                            Log.d("Tambah Pertanyaan Hasil", "update_hasil: "+hasil.hasil+", Jml Array Hasil : "+array_hasil.size)

                            for (j in array_hasil)
                            {
                                //menambahkan array list hasil lama
                                listHasil.add(j)
                            }

                            //menambahkan array list hasil baru
                            listHasil.add(-1)

                            Log.d("Tambah Pertanyaan Hasil", "update_hasil: "+hasil.hasil+", Jml Array Hasil Baru : "+listHasil.size)

                            val tp = HashMap<String, Any>()
                            tp["array_hasil"] = listHasil

                            Log.d("Tambah Pertanyaan Hasil", "update_hasil: "+hasil.hasil+", Jml Array Hasil Baru : "+tp)

                            db = Firebase.firestore
                            db.collection("hasil").document(d.id).update(tp)
                                .addOnSuccessListener {
                                    Log.d("Update Penyakit", "update_hasil: "+"Data penyakit "+hasil.hasil+" berhasil diupdate")
                                }
                                .addOnFailureListener { e ->
                                    Log.d("Update Penyakit", "update_hasil: "+"Data penyakit gagal diupdate: ${e.message}")
                                }
                        }else
                        {
                            Toast.makeText(
                                this,
                                "Data pertanyaan ditambahkan!",
                                Toast.LENGTH_SHORT
                            ).show()
//                            val intent = Intent(this, Admin_Pertanyaan::class.java)
//                            startActivity(intent)
                            finish()
                        }
                        i++
                    }


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

}