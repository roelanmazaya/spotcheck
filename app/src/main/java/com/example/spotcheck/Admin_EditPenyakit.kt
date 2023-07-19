package com.example.spotcheck


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotcheck.adapter.Adapter_EditArray
import com.example.spotcheck.adapter.Adapter_InputArray
import com.example.spotcheck.databinding.AdminEditpenyakitPageBinding
import com.example.spotcheck.models.Pertanyaan
import com.example.spotcheck.models.Pertanyaan_Model
import com.example.spotcheck.models.Pertanyaan_Model_Edit
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
    private lateinit var pertanyaanAdapter: Adapter_InputArray
    private lateinit var pertanyaan: Pertanyaan
    private var listPertanyaan = mutableListOf<Pertanyaan_Model_Edit>()

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
        binding.btnEditArrayPenyakit.setOnClickListener(this)

        db = Firebase.firestore
        db.collection("pertanyaan").whereNotEqualTo("id", "null").get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val listDoc: List<DocumentSnapshot> = result.documents
                    total_row = listDoc.size
                    var arrayHasil = intent.getStringExtra("array_hasil")
                    val exp = arrayHasil!!.split(",").toTypedArray()
                    var i = 0;
                    for (d in listDoc) {
                        if(i <= total_row){
                            pertanyaan = d.toObject(Pertanyaan::class.java)!!
                            listPertanyaan.add(Pertanyaan_Model_Edit(d.id, pertanyaan.id, pertanyaan.pertanyaan, exp[i].trim().toInt()))
                        }
                        i++
                    }
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
            R.id.btnEditArrayPenyakit -> {
                showArrayDialog()
//                Toast.makeText(this, "Popup", Toast.LENGTH_SHORT).show()
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

    private fun showArrayDialog() {
        val dialogView = layoutInflater.inflate(R.layout.popup_inputarraypenyakit_list, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val lv = dialogView.findViewById<ListView>(R.id.lvpopup_Pertanyaan)
        val layoutManager = LinearLayoutManager(this)
//        lv.layoutManager = layoutManager
        val adapter = Adapter_EditArray(this, R.layout.popup_inputarraypenyakit, listPertanyaan)
        lv.adapter = adapter

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
            binding.editArrayPenyakitAdmin.setText(arrayBuilder.toString())
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }


}