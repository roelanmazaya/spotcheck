package com.example.spotcheck


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotcheck.databinding.AdminEditpertanyaanPageBinding
import com.example.spotcheck.models.Hasil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Admin_EditPertanyaan : AppCompatActivity(), View.OnClickListener{

    lateinit var binding: AdminEditpertanyaanPageBinding

    private lateinit var storage: FirebaseStorage
    private lateinit var db: FirebaseFirestore
    private lateinit var db2: CollectionReference
    private lateinit var pertanyaan: ArrayList<HashMap<String, Any>>
    private lateinit var adapter: SimpleAdapter
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private lateinit var hasil: Hasil

    private val COLLECTION = "pertanyaan"
    private val F_ID = "id"
    private val F_Pertanyaan = "pertanyaan"
    private var docId = ""
    private var id = ""
    private var index_ke = 0
    private val RC_OK = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminEditpertanyaanPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        docId = intent.getStringExtra("docId").toString()
        id = intent.getStringExtra("id").toString()
        index_ke = intent.getStringExtra("index_ke")!!.toInt()

        Log.d("Index_Ke", "onCreate: "+index_ke)

        storage = FirebaseStorage.getInstance()
        pertanyaan = ArrayList()
        auth = Firebase.auth

        binding.editIDpage.text.toString()
        binding.editPertanyaanpage.text.toString()

        binding.editIDpage.setText(docId)
        binding.editPertanyaanpage.setText(intent.getStringExtra("pertanyaan").toString())

        binding.btnEditQAdmin.setOnClickListener(this)
        binding.btnHapusQAdmin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnEditQAdmin -> {
                val tp = HashMap<String, Any>()
                tp[F_ID] = id.toInt()
                tp[F_Pertanyaan] = binding.editPertanyaanpage.text.toString()

                db = Firebase.firestore
                db.collection(COLLECTION).document(docId).update(tp)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Data berhasil diupdate",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                           this,
                            "Data gagal diupdate: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            R.id.btnHapusQAdmin -> {
                db = Firebase.firestore
                db.collection(COLLECTION).document(docId).delete()
                    .addOnSuccessListener {
                        delete_array_hasil(index_ke)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Pertanyaan gagal dihapus ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun delete_array_hasil(indexKe: Int) {
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
                        Log.d("Hasil", "update_hasil: ${d.id}")

                        if(i <= total_row)
                        {

                            hasil = d.toObject(Hasil::class.java)!!

                            var array_hasil: ArrayList<Int>

                            array_hasil = hasil.array_hasil!!
                            listHasil = ArrayList()

                            Log.d("Hasil", "update_hasil: "+hasil.hasil+", Jml Array Hasil : "+array_hasil.size)

                            var x = 0
                            for (j in array_hasil)
                            {
                                //menambahkan array list hasil lama
                                if(x!=indexKe){
                                    listHasil.add(j)
                                }

                                x++
                            }

                            Log.d("Hasil", "update_hasil: "+hasil.hasil+", Jml Array Hasil Baru : "+listHasil.size)

                            val tp_hasil = HashMap<String, Any>()
                            tp_hasil["array_hasil"] = listHasil

                            db = Firebase.firestore
                            db.collection("hasil").document(d.id).update(tp_hasil)
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
                                "Pertanyaan telah berhasil dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
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

//    override fun onStart() {
//        super.onStart()
//        db = FirebaseFirestore.getInstance()
//        db.collection(COLLECTION).addSnapshotListener { querySnapshot, e ->
//            if (e != null) Log.d("fireStore", e.localizedMessage)
//        }
//    }

}