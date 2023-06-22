package com.example.spotcheck

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spotcheck.databinding.AdminEditpenyakitPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Admin_EditPenyakitFragment : Fragment(), View.OnClickListener {
    private var _binding: AdminEditpenyakitPageBinding? = null
    private val binding get() = _binding!!

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
    private val F_Array = "array_hasil"
    private val F_Solusi = "solusi"
    private var docId = ""
    private val RC_OK = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminEditpenyakitPageBinding.inflate(inflater, container, false)
        val view = binding.root

        storage = FirebaseStorage.getInstance()
        hasil = ArrayList() //ini bener arraylist kah? kolom lainnya harus ditambah juga kah?
        auth = Firebase.auth

        binding.editIDPenyakitAdmin.text.toString()
        binding.editPenyakitAdmin.text.toString()
        binding.editGambarPenyakitAdmin.text.toString()
        binding.editArrayPenyakitAdmin.text.toString()
        binding.editSolusiAdmin.text.toString()
        binding.btnEditPenyakitAdmin.setOnClickListener(this)
        binding.btnHapusPenyakitAdmin.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnEditPenyakitAdmin -> {
                val tp = HashMap<String, Any>()
                tp[F_ID] = docId
                tp[F_Hasil] = binding.editPenyakitAdmin.text.toString()
                tp[F_Gambar] = binding.editGambarPenyakitAdmin.text.toString()
                tp[F_Array] = binding.editArrayPenyakitAdmin.text.toString()
                tp[F_Solusi] = binding.editSolusiAdmin.text.toString()

                db.collection(COLLECTION).document(docId).update(tp)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Data penyakit berhasil diupdate",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "Data penyakit gagal diupdate: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            R.id.btnHapusPenyakitAdmin -> {
                db.collection(COLLECTION).whereEqualTo(F_ID, docId).get()
                    .addOnSuccessListener { result ->
                        for (doc in result) {
                            db.collection(COLLECTION).document(doc.id).delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Penyakit telah berhasil dihapus",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        requireContext(),
                                        "Penyakit gagal dihapus ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
