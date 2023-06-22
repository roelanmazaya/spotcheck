package com.example.spotcheck

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.spotcheck.databinding.UserHasilPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors


class User_Hasil_Act : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: UserHasilPageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var hasilJawaban: ArrayList<Int>
    private lateinit var viewModel: UserHasilViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.user_hasil_page)

        viewModel = ViewModelProvider(this).get(UserHasilViewModel::class.java)

        auth = Firebase.auth
        hasilJawaban = intent.getIntegerArrayListExtra("hasil_penyakit") ?: ArrayList()

        Log.d("Hasil Jawaban", "onCreate: "+hasilJawaban)


        firestore = FirebaseFirestore.getInstance()
        binding.btnbacktohome.setOnClickListener(this)
        val firestoreRepo = FirestoreRepositoryHasil()
        firestoreRepo.getDataResults { dataResults ->
            if (dataResults.isNotEmpty()) {
                Log.d("Data Result e ", "onCreate: "+dataResults)
                viewModel.calculateKNN(hasilJawaban, dataResults, 1)
                viewModel.hasilPenyakit.observe(this) { hasilJawaban ->
                    val exp = hasilJawaban!!.split("|").toTypedArray()
                    binding.txHasilPenyakit.text = "\nGejala:\n"+exp[0]
                    binding.txSolusiPenyakit.setMovementMethod(ScrollingMovementMethod())
                    binding.txSolusiPenyakit.text = Html.fromHtml("<br><br>Solusi: "+exp[1]+"")
                    Log.d("URL Picture", "onCreate: "+exp[2])

                    // Declaring executor to parse the URL
                    val executor = Executors.newSingleThreadExecutor()

                    // Once the executor parses the URL
                    // and receives the image, handler will load it
                    // in the ImageView
                    val handler = Handler(Looper.getMainLooper())

                    // Initializing the image
                    var image: Bitmap? = null

                    // Only for Background process (can take time depending on the Internet speed)
                    executor.execute {

                        // Image URL
                        val imageURL = exp[2]
//                        val imageURL = "https://drive.google.com/file/d/1sWPKTtC6FmoMnqH3ghc9vBqZvRNcsjkg/view"

                        // Tries to get the image and post it in the ImageView
                        // with the help of Handler
                        try {
                            val `in` = java.net.URL(imageURL).openStream()
                            image = BitmapFactory.decodeStream(`in`)

                            // Only for making changes in UI
                            handler.post {
                                binding.imgHasilPenyakit.setImageBitmap(image)
                            }
                        }

                        // If the URL doesnot point to
                        // image or any other kind of failure
                        catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            } else {
                runOnUiThread {
                    binding.txHasilPenyakit.text = "Data penyakit kosong"
                }
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnbacktohome -> {
                val intent = Intent(this@User_Hasil_Act, User_Dashboard::class.java)
                startActivity(intent)
            }
        }
    }

}
