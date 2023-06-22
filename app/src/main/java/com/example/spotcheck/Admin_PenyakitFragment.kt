package com.example.spotcheck

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spotcheck.databinding.AdminPenyakitPageBinding
import com.example.spotcheck.models.Hasil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Admin_PenyakitFragment : Fragment(), View.OnClickListener {
    private var _binding: AdminPenyakitPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: ArrayAdapter<*>
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var listhasil: ArrayList<String?>
    private lateinit var hasil: Hasil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminPenyakitPageBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnTambahPenyakitAdmin.setOnClickListener(this)
        if (listhasil.isNotEmpty()) {
            binding.lvDataHasil.adapter = listAdapter
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        db = Firebase.firestore
        listhasil = ArrayList()
        db.collection("hasil").get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val listDoc: List<DocumentSnapshot> = result.documents
                    for (d in listDoc) {
                        hasil = d.toObject(Hasil::class.java)!!
                        listhasil.add(hasil.hasil)
                    }
                    Toast.makeText(
                        requireContext(),
                        "Data: $listhasil",
                        Toast.LENGTH_SHORT
                    ).show()

                    listAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        listhasil
                    )
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Gagal memuat data: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTambahPenyakitAdmin -> {
                val intent = Intent(requireContext(), Admin_TambahPenyakitFragment::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
