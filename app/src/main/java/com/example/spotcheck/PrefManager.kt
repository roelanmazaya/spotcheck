package com.example.spotcheck
import android.content.Context
import android.content.SharedPreferences

class PrefManager (var context: Context?) {

    // Shared pref mode
    val PRIVATE_MODE = 0

    // Sharedpref file name
    private val PREF_NAME = "SharedPreferences"

    private val IS_LOGIN = "is_login"

    var pref: SharedPreferences? = context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor: SharedPreferences.Editor? = pref?.edit()

    fun setLoggin(isLogin: Boolean) {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun setEmail(email: String?) {
        editor?.putString("email", email)
        editor?.commit()
    }

    fun setId(id: String?) {
        editor?.putString("id", id)
        editor?.commit()
    }

    fun setNama(nama: String?) {
        editor?.putString("nama", nama)
        editor?.commit()
    }

    fun setRole(role: String?) {
        editor?.putString("role", role)
        editor?.commit()
    }

    fun setUsia(usia: String?) {
        editor?.putString("usia", usia)
        editor?.commit()
    }

    fun isLogin(): Boolean? {
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun getEmail(): String? {
        return pref?.getString("email", "")
    }

    fun getId(): String? {
        return pref?.getString("id", "")
    }

    fun getNama(): String? {
        return pref?.getString("nama", "")
    }

    fun getRole(): String? {
        return pref?.getString("role", "")
    }

    fun getUsia(): String? {
        return pref?.getString("usia", "")
    }

    fun removeData() {
        editor?.clear()
//        editor?.remove(IS_LOGIN)
//        editor?.apply()
        editor?.commit()
    }
}