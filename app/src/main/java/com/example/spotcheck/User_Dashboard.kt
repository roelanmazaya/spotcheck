package com.example.spotcheck

import User_HistoryFragment
import User_HomeFragment
import User_ProfilFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.spotcheck.databinding.UserDashboardPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class User_Dashboard : AppCompatActivity(){
    private lateinit var binding: UserDashboardPageBinding
    lateinit var auth: FirebaseAuth
    var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserDashboardPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        auth = Firebase.auth


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    val fragment = User_HomeFragment()
                    showFragment(fragment)
                    true
                }
                R.id.menu_history -> {
                    val fragment = User_HistoryFragment()
                    showFragment(fragment)
                    true
                }
                R.id.menu_profil -> {
                    val fragment = User_ProfilFragment()
                    showFragment(fragment)
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.menu_home // Menandai menu_home sebagai menu yang aktif secara default
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, fragment)
            .commit()
    }
}
