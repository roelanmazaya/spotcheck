package com.example.spotcheck


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotcheck.databinding.AdminEditpenyakitPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Admin_EditPenyakit : AppCompatActivity(), View.OnClickListener{

    lateinit var binding: AdminEditpenyakitPageBinding

    private lateinit var storage: FirebaseStorage
    private lateinit var db: FirebaseFirestore
    private lateinit var db2: CollectionReference
    private lateinit var hasil: ArrayList<HashMap<String, Any>>
    private lateinit var adapter: SimpleAdapter
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    private val COLLECTION = "hasil"
    private val F_ID = "id"
    private val F_Hasil = "hasil"
    private val F_Gambar = "pict"
    private val F_Gambar2 = "pict2"
    private val F_Gambar3 = "pict3"
    private val F_Array = "array_hasil"
    private val F_Solusi = "solusi"
    private var docId = ""
    private var id = ""
    private var index_ke = 0
    private val RC_OK = 100
    var total_row = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminEditpenyakitPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        docId = intent.getStringExtra("docId").toString()
        id = intent.getStringExtra("id").toString()
        index_ke = intent.getStringExtra("index_ke")!!.toInt()

        Log.d("Index_Ke", "onCreate: "+index_ke)

        storage = FirebaseStorage.getInstance()
        hasil = ArrayList() //ini bener arraylist kah? kolom lainnya harus ditambah juga kah?
        auth = Firebase.auth

        binding.editIDPenyakitAdmin.text.toString()
        binding.editPenyakitAdmin.text.toString()
        binding.editGambarPenyakitAdmin.text.toString()
        binding.editGambarPenyakitAdmin2.text.toString()
        binding.editGambarPenyakitAdmin3.text.toString()
        binding.editArrayPenyakitAdmin.text.toString()
        binding.editSolusiAdmin.text.toString()

        binding.editIDPenyakitAdmin.setText(docId)
        binding.editPenyakitAdmin.setText(intent.getStringExtra("hasil").toString())
        binding.editGambarPenyakitAdmin.setText(intent.getStringExtra("pict").toString())
        binding.editGambarPenyakitAdmin2.setText(intent.getStringExtra("pict2").toString())
        binding.editGambarPenyakitAdmin3.setText(intent.getStringExtra("pict3").toString())
        binding.editSolusiAdmin.setText(intent.getStringExtra("solusi").toString())

        binding.editArrayPenyakitAdmin.setText(intent.getStringExtra("array_hasil"))

        binding.btnEditPenyakitAdmin.setOnClickListener(this)
        binding.btnHapusPenyakitAdmin.setOnClickListener(this)

        db = Firebase.firestore
        db.collection("pertanyaan").get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val listDoc: List<DocumentSnapshot> = result.documents
                    total_row = listDoc.size - 1
//                    Toast.makeText(this, total_row.toString(), Toast.LENGTH_SHORT).show()
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnEditPenyakitAdmin -> {
                val tp = HashMap<String, Any>()

                val arr_hasil = binding.editArrayPenyakitAdmin.text.trim().toString()
                val exp_hasil = arr_hasil.split(",")

                var listHasil: ArrayList<Int>
                listHasil = ArrayList()
                listHasil.clear()

                if(exp_hasil.size < total_row){
                    Toast.makeText(this, "Array Penyakit yang diinputkan (${exp_hasil.size}) tidak boleh kurang dari "+total_row.toString(), Toast.LENGTH_LONG).show()
                    binding.editArrayPenyakitAdmin.requestFocus()
                }else if(exp_hasil.size > total_row){
                    Toast.makeText(this, "Array Penyakit yang diinputkan (${exp_hasil.size}) tidak boleh lebih dari "+total_row.toString(), Toast.LENGTH_LONG).show()
                    binding.editArrayPenyakitAdmin.requestFocus()
                }else{
                    for (i in exp_hasil)
                    {
                        listHasil.add(i.trim().toInt())
                    }

//                    val hasil = Hasil(id.toInt(), penyakit, pict, solusi, listHasil)

                    tp[F_ID] = id.toInt()
                    tp[F_Hasil] = binding.editPenyakitAdmin.text.toString()
                    tp[F_Gambar] = binding.editGambarPenyakitAdmin.text.toString()
                    tp[F_Gambar2] = binding.editGambarPenyakitAdmin2.text.toString()
                    tp[F_Gambar3] = binding.editGambarPenyakitAdmin3.text.toString()
                    tp[F_Array] = listHasil
                    tp[F_Solusi] = binding.editSolusiAdmin.text.toString()

                    Log.d("Edit Penyakit", "onClick: "+tp)

                    db.collection(COLLECTION).document(docId).update(tp)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Data penyakit berhasil diupdate",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Data penyakit gagal diupdate: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }

            }

            R.id.btnHapusPenyakitAdmin -> {
                db = Firebase.firestore
                db.collection(COLLECTION).document(docId).delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Penyakit berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Penyakit gagal dihapus ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION).addSnapshotListener { querySnapshot, e ->
            if (e != null) Log.d("fireStore", e.localizedMessage)
        }
    }


}