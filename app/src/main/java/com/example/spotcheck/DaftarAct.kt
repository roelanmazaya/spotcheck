package com.example.spotcheck

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.spotcheck.databinding.DaftarPageBinding
import com.example.spotcheck.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore

class DaftarAct : AppCompatActivity() {
    private lateinit var binding: DaftarPageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DaftarPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        db = Firebase.firestore

        binding.btnDaftar.setOnClickListener {
            val email = binding.InputEmailDaftar.text.trim().toString()
            val password = binding.InputSandiDaftar.text.trim().toString()
            val nama = binding.InputNamaDaftar.text.trim().toString()
            val usia = binding.InputUsiaDaftar.text.trim().toString().toInt()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser!!.uid
                        val user = User(uid, email, role = "umum", nama, usia)

                        db.collection("users")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Sukses Daftar User!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(applicationContext, LoginAct::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    applicationContext,
                                    "Gagal: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Gagal membuat akun: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
