package com.example.spotcheck

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotcheck.models.DataHasil

class UserHasilViewModel : ViewModel() {
    val hasilPenyakit: MutableLiveData<String> = MutableLiveData()

    fun calculateKNN(hasilJawaban: List<Int>, dataResults: List<DataHasil>, k: Int) {
        val knnClassifier = KNNClassifier(k, dataResults)
        val predictedClass = knnClassifier.classify(hasilJawaban)
        hasilPenyakit.postValue(predictedClass)
    }
}
