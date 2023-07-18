package com.example.spotcheck

import android.util.Log
import com.example.spotcheck.models.DataHasil
import kotlin.math.pow
import kotlin.math.sqrt

class KNNClassifier(private val k: Int, private val dataResults: List<DataHasil>) {

    fun classify(hasilJawaban: List<Int>): String {
        val distances = mutableListOf<Pair<Double, String>>()
        val distances_solusi = mutableListOf<Pair<Double, String>>()
        val distances_pict = mutableListOf<Pair<Double, String>>()
        val distances_pict2 = mutableListOf<Pair<Double, String>>()
        val distances_pict3 = mutableListOf<Pair<Double, String>>()

        var total_row = dataResults.size - 1
        var i = 1
        // Perulangan/lopping untuk mengelompokkan hasil jawaban yang didapat
        for (dataResult in dataResults) {
            if(i <= total_row) {
                Log.d("Hasil ", "classify: "+dataResult.hasil+", arrayHasil :"+dataResult.arrayHasil)
                Log.d("Hasil Jawaban", "classify: "+hasilJawaban)
                val distance = euclideanDistance(hasilJawaban, dataResult.arrayHasil)
                distances.add(distance to dataResult.hasil)
                distances_solusi.add(distance to dataResult.solusi)
                distances_pict.add(distance to dataResult.pict)
                distances_pict2.add(distance to dataResult.pict2)
                distances_pict3.add(distance to dataResult.pict3)
            }
            i++
        }
// Proses pengurutan jarak dan pencarian tetangga terdekat
        val sortedDistances = distances.sortedBy { it.first }
        val kNearest = sortedDistances.take(k)
        val counts = kNearest.groupingBy { it.second }.eachCount()
        val predictedClass = counts.maxByOrNull { it.value }?.key ?: ""

        val sortedDistances_solusi = distances_solusi.sortedBy { it.first }
        val kNearest_solusi = sortedDistances_solusi.take(k)
        val counts_solusi = kNearest_solusi.groupingBy { it.second }.eachCount()
        val predictedClass_solusi = counts_solusi.maxByOrNull { it.value }?.key ?: ""

        val sortedDistances_pict = distances_pict.sortedBy { it.first }
        val kNearest_pict = sortedDistances_pict.take(k)
        val counts_pict = kNearest_pict.groupingBy { it.second }.eachCount()
        val predictedClass_pict = counts_pict.maxByOrNull { it.value }?.key ?: ""

        val sortedDistances_pict2 = distances_pict2.sortedBy { it.first }
        val kNearest_pict2 = sortedDistances_pict2.take(k)
        val counts_pict2 = kNearest_pict2.groupingBy { it.second }.eachCount()
        val predictedClass_pict2 = counts_pict2.maxByOrNull { it.value }?.key ?: ""

        val sortedDistances_pict3 = distances_pict3.sortedBy { it.first }
        val kNearest_pict3 = sortedDistances_pict3.take(k)
        val counts_pict3 = kNearest_pict3.groupingBy { it.second }.eachCount()
        val predictedClass_pict3 = counts_pict3.maxByOrNull { it.value }?.key ?: ""

        return predictedClass+"|"+predictedClass_solusi+"|"+predictedClass_pict+"|"+predictedClass_pict2+"|"+predictedClass_pict3
    }
// Fungsi yang digunakan untuk menghitung jarak euclidean antara 2 vektor yaitu hasil dan datapenyakit dalam database
    private fun euclideanDistance(point1: List<Int>, point2: List<Int>): Double {
        var sum = 0.0
        for (i in point1.indices) {
            val diff = point1[i] - point2[i]
            sum += diff.toDouble().pow(2)
        }
        return sqrt(sum)
//        return sum;
    }
}
