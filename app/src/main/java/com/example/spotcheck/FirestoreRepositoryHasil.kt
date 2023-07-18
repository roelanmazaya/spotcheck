package com.example.spotcheck

import com.example.spotcheck.models.DataHasil
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepositoryHasil {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getDataResults(completion: (List<DataHasil>) -> Unit) {
        firestore.collection("hasil")
            .get()
            .addOnSuccessListener { result ->
                val dataResults = mutableListOf<DataHasil>()
                for (document in result) {
                    val id = document.getLong("id")?.toInt() ?: 0
                    val hasil = document.getString("hasil") ?: ""
                    val pict = document.getString("pict") ?: ""
                    val pict2 = document.getString("pict2") ?: ""
                    val pict3 = document.getString("pict3") ?: ""
                    val solusi = document.getString("solusi") ?: ""
                    val arrayHasil = document.get("array_hasil") as? ArrayList<Int> ?: arrayListOf()
                    val dataResult = DataHasil(id, hasil, pict, pict2, pict3, solusi, arrayHasil)
                    dataResults.add(dataResult)
                }
                completion(dataResults)
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }
}
