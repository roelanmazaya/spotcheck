package com.example.spotcheck

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spotcheck.databinding.AdminTambahpenyakitPageBinding
import com.example.spotcheck.models.Hasil
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Admin_TambahPenyakitFragment : Fragment(), View.OnClickListener {
    private var _binding: AdminTambahpenyakitPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FirebaseFirestore
    private lateinit var uri: Uri
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminTambahpenyakitPageBinding.inflate(inflater, container, false)
        val view = binding.root

        db = Firebase.firestore
        binding.btnTambahPenyakit.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTambahPenyakit -> {
                val id = binding.inputIDPenyakitAdmin.text.trim().toString()
                val p = binding.inputPenyakitAdmin.text.trim().toString()
                val hasil = Hasil(id.toInt(), p)
                db.collection("hasil")
                    .document(id)
                    .set(hasil)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Data penyakit ditambahkan!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(requireContext(), Admin_PenyakitFragment::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "Data Penyakit Gagal Ditambahkan: ${e.message}",
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
