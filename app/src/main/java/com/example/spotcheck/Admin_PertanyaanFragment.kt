package com.example.spotcheck

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spotcheck.databinding.AdminPertanyaanPageBinding
import com.example.spotcheck.models.Pertanyaan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Admin_PertanyaanFragment : Fragment(), View.OnClickListener {
    private var _binding: AdminPertanyaanPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: ArrayAdapter<*>
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var listPertanyaan: ArrayList<String?>
    private lateinit var pertanyaan: Pertanyaan

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminPertanyaanPageBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnTambahQAdmin.setOnClickListener(this)
        if (listPertanyaan.isNotEmpty()) {
            binding.lvDataPertanyaan.adapter = listAdapter
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        db = Firebase.firestore
        listPertanyaan = ArrayList()
        db.collection("pertanyaan").get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val listDoc: List<DocumentSnapshot> = result.documents
                    for (d in listDoc) {
                        pertanyaan = d.toObject(Pertanyaan::class.java)!!
                        listPertanyaan.add(pertanyaan.pertanyaan)
                    }
                    Toast.makeText(
                        requireContext(),
                        "Data: $listPertanyaan",
                        Toast.LENGTH_SHORT
                    ).show()

                    listAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        listPertanyaan
                    )
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Gagal mendapatkan data: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTambahQAdmin -> {
                val intent = Intent(requireContext(), Admin_TambahPertanyaanFragment::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
