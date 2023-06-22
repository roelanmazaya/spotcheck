package com.example.spotcheck

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spotcheck.databinding.AdminTambahpertanyaanPageBinding
import com.example.spotcheck.models.Pertanyaan
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Admin_TambahPertanyaanFragment : Fragment(), View.OnClickListener {
    private var _binding: AdminTambahpertanyaanPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FirebaseFirestore
    private lateinit var uri: Uri
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminTambahpertanyaanPageBinding.inflate(inflater, container, false)
        val view = binding.root

        db = Firebase.firestore
        binding.btnTambahPertanyaan.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTambahPertanyaan -> {
                val id = binding.inputIDPertanyaan.text.trim().toString()
                val p = binding.InputPertanyaanAdmin.text.trim().toString()
                val pertanyaan = Pertanyaan(id.toInt(), p)
                db.collection("pertanyaan")
                    .document(id)
                    .set(pertanyaan)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Data ditambahkan!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(requireContext(), Admin_PertanyaanFragment::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "Gagal: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
