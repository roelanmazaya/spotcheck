package com.example.spotcheck

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotcheck.databinding.AdminTambahpenyakitPageBinding
import com.example.spotcheck.models.Hasil
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Admin_TambahPenyakit : AppCompatActivity(), View.OnClickListener{

    lateinit var binding: AdminTambahpenyakitPageBinding

    private lateinit var db: FirebaseFirestore
    private lateinit var uri: Uri
    private var currentUser: FirebaseUser? = null
    private lateinit var hasil: Hasil
    private var last_id = 0
    var total_row = 0

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
                        Log.d("Last Id Hasil", "onCreate: "+(last_id+1))
                    }
                    binding.inputIDPenyakitAdmin.setText((last_id+1).toString())

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
                    val listDoc: List<DocumentSnapshot> = result.documents
                    total_row = listDoc.size - 1
//                       Toast.makeText(this, total_row.toString(), Toast.LENGTH_SHORT).show()
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
    }

    override fun onClick(v: View?) {
        when (v?.id) {
           R.id.btnTambahPenyakit -> {
               val id = binding.inputIDPenyakitAdmin.text.trim().toString()
               val penyakit = binding.inputPenyakitAdmin.text.trim().toString()
               val pict = binding.inputGambarPenyakitAdmin.text.trim().toString()
               val solusi = binding.inputSolusiAdmin.text.trim().toString()
               val arr_hasil = binding.inputArrayPenyakitAdmin.text.trim().toString()
               val exp_hasil = arr_hasil.split(",")

               var listHasil: ArrayList<Int>
               listHasil = ArrayList()
               listHasil.clear()
//               Log.d("Total Penyakit", "onClick: "+total_row)

               if(exp_hasil.size < total_row){
                   Toast.makeText(this, "Array Penyakit yang diinputkan (${exp_hasil.size}) tidak boleh kurang dari "+total_row.toString(), Toast.LENGTH_LONG).show()
                   binding.inputArrayPenyakitAdmin.requestFocus()
               }else if(exp_hasil.size > total_row){
                   Toast.makeText(this, "Array Penyakit yang diinputkan (${exp_hasil.size}) tidak boleh lebih dari "+total_row.toString(), Toast.LENGTH_LONG).show()
                   binding.inputArrayPenyakitAdmin.requestFocus()
               }else{
                   for (i in exp_hasil)
                   {
                       listHasil.add(i.trim().toInt())
//                       Log.d("Array_Hasil_Insert", "onClick: "+i)
                   }

//                   Log.d("Array_Hasil_Insert", "onClick: "+listHasil.size)

                   val hasil = Hasil(id.toInt(), penyakit, pict, solusi, listHasil)

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
        }
    }


}