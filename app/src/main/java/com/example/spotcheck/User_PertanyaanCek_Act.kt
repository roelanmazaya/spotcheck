package com.example.spotcheck

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.spotcheck.databinding.UserPertanyaancekPageBinding
import com.google.firebase.firestore.FirebaseFirestore



class User_PertanyaanCek_Act : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: UserPertanyaancekPageBinding
    private lateinit var firestore: FirebaseFirestore
    private var pertanyaanList: ArrayList<String> = ArrayList()
    private var currentPertanyaanIndex = 0
    private var hasilJawaban: ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserPertanyaancekPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firestore = FirebaseFirestore.getInstance()

        binding.btnBenarPage.setOnClickListener(this)
        binding.btnTidakPage.setOnClickListener(this)

        loadPertanyaan()
    }

    private fun loadPertanyaan() {
        firestore.collection("pertanyaan")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val pertanyaan = document.getString("pertanyaan")
                    pertanyaan?.let { pertanyaanList.add(it) }
                }
                showCurrentPertanyaan()
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }

    private fun showCurrentPertanyaan() {
        if (currentPertanyaanIndex < pertanyaanList.size) {
            val pertanyaan = pertanyaanList[currentPertanyaanIndex]
            binding.txPertanyaanPage.text = pertanyaan
        } else {
            // Pertanyaan sudah habis, tampilkan halaman hasil
            val intent = Intent(this, User_Hasil_Act::class.java)
            intent.putExtra("hasil_penyakit", hasilJawaban)
            startActivity(intent)
            finish() // Menutup activity ini agar tidak dapat kembali ke pertanyaan
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.btnBenarPage.id -> {
                hasilJawaban.add(1) // Tambahkan jawaban 1 ke dalam array
                currentPertanyaanIndex++
                showCurrentPertanyaan()
            }
            binding.btnTidakPage.id -> {
                hasilJawaban.add(-1) // Tambahkan jawaban -1 ke dalam array
                currentPertanyaanIndex++
                showCurrentPertanyaan()
            }
        }
    }
}
