package com.example.spotcheck

import Admin_HomeFragment
import Admin_ProfilFragment
import User_HomeFragment
import User_ProfilFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.spotcheck.databinding.AdminDashboardPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Admin_Dashboard : AppCompatActivity() {
    private lateinit var binding: AdminDashboardPageBinding
    lateinit var auth: FirebaseAuth
    var currentUser: FirebaseUser? = null
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminDashboardPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        prefManager = PrefManager(this)
        checkLogin()

        auth = Firebase.auth

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_home_admin -> {
                    val fragment = Admin_HomeFragment()
                    showFragment(fragment)
                    true
                }
                R.id.menu_profil_admin -> {
                    val fragment = Admin_ProfilFragment()
                    showFragment(fragment)
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.menu_home_admin // Menandai menu_home sebagai menu yang aktif secara default
    }

    private fun checkLogin(){
        if (prefManager.isLogin() == false){
            val intent = Intent(this, LoginAct::class.java)
            startActivity(intent)
            finish()
        }
    }



    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, fragment)
            .commit()
    }
}
