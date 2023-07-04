package com.example.spotcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

import com.example.spotcheck.databinding.LoginPageBinding
import com.example.spotcheck.models.User


class LoginAct : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: LoginPageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var isPasswordVisible = false
    var currentUser: FirebaseUser? = null
    private lateinit var prefManager: PrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        prefManager = PrefManager(this)

        auth = Firebase.auth
        db = Firebase.firestore

        binding.btnLogin.setOnClickListener {
            val email = binding.InputEmailLogin.text.trim().toString()
            val password = binding.InputSandiLogin.text.trim().toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        db.collection("users")
                            .document(user!!.uid)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                val user = snapshot.toObject<User>()

                                prefManager.setLoggin(true)
                                prefManager.setEmail(email)
                                prefManager.setRole(user!!.role)
                                prefManager.setNama(user!!.nama)
                                prefManager.setId(user!!.id)
                                prefManager.setUsia(user!!.usia.toString())

                                if (user!!.role == "admin") {
                                    val intent = Intent(this, Admin_Dashboard::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    val intent = Intent(this, User_Dashboard::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Email/Sandi Salah", Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.btnShowPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.InputSandiLogin.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.btnShowPassword.setIconResource(R.drawable.close_eye2)
            } else {
                binding.InputSandiLogin.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.btnShowPassword.setIconResource(R.drawable.open_eye2)
            }
        }
    }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btnLogin -> {
                    auth.signInWithEmailAndPassword(
                        binding.InputEmailLogin.text.toString(),
                        binding.InputSandiLogin.text.toString()
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            currentUser = auth.currentUser
                            if (currentUser != null) {
                                if (currentUser!!.isEmailVerified) {
                                    Toast.makeText(this, "Masuk Berhasil", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this, User_Dashboard::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Email anda belum terverifikasi",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Email/Sandi Salah", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

    }

