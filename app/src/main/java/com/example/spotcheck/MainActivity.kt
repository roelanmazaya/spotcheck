package com.example.spotcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_main.btn_masuk_main
import com.example.spotcheck.databinding.ActivityMainBinding
//import kotlinx.android.bind.main.activity_main.btn_daftar_main
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var auth : FirebaseAuth
    var currentUser : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnMasukMain
        binding.btnDaftarMain
        auth = Firebase.auth

        binding.btnMasukMain.setOnClickListener {
            val intent = Intent(this, LoginAct::class.java)
            startActivity(intent)
        }

        binding.btnDaftarMain.setOnClickListener {
            val intent = Intent(this, DaftarAct::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.btnMasukMain ->{
                binding.btnMasukMain.setOnClickListener {
                    val intent = Intent(this, LoginAct::class.java)
                    startActivity(intent)
                }
            }
            R.id.btnDaftarMain->{
                binding.btnDaftarMain.setOnClickListener {
                    val intent = Intent(this, DaftarAct::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}