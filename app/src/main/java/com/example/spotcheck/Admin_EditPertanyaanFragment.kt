package com.example.spotcheck

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spotcheck.databinding.AdminEditpertanyaanPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Admin_EditPertanyaanFragment : Fragment(), View.OnClickListener {
    private var _binding: AdminEditpertanyaanPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var storage: FirebaseStorage
    private lateinit var db: FirebaseFirestore
    private lateinit var db2: CollectionReference
    private lateinit var pertanyaan: ArrayList<HashMap<String, Any>>
    private lateinit var adapter: SimpleAdapter
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    private val COLLECTION = "pertanyaan"
    private val F_ID = "id"
    private val F_Pertanyaan = "pertanyaan"
    private var docId = ""
    private val RC_OK = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminEditpertanyaanPageBinding.inflate(inflater, container, false)
        val view = binding.root

        storage = FirebaseStorage.getInstance()
        pertanyaan = ArrayList()
        auth = Firebase.auth

        binding.editIDpage.text.toString()
        binding.editPertanyaanpage.text.toString()
        binding.btnEditQAdmin.setOnClickListener(this)
        binding.btnHapusQAdmin.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnEditQAdmin -> {
                val tp = HashMap<String, Any>()
                tp[F_ID] = docId
                tp[F_Pertanyaan] = binding.editPertanyaanpage.text.toString()

                db.collection(COLLECTION).document(docId).update(tp)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Data berhasil diupdate",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "Data gagal diupdate: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            R.id.btnHapusQAdmin -> {
                db.collection(COLLECTION).whereEqualTo(F_ID, docId).get()
                    .addOnSuccessListener { result ->
                        for (doc in result) {
                            db.collection(COLLECTION).document(doc.id).delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Pertanyaan telah berhasil dihapus",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        requireContext(),
                                        "Pertanyaan gagal dihapus ${e.message}",
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
