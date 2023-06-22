import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.spotcheck.LoginAct
import com.example.spotcheck.R
import com.example.spotcheck.databinding.AdminProfilPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Admin_ProfilFragment : Fragment(), View.OnClickListener {
    private var _binding: AdminProfilPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminProfilPageBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnKeluarAdmin
        binding.btnEditProfilAdmin
        auth = Firebase.auth
        sharedPreferences = requireContext().getSharedPreferences("ProfileImage", Context.MODE_PRIVATE)

        binding.btnKeluarAdmin.setOnClickListener {
            val intent = Intent(requireContext(), LoginAct::class.java)
            startActivity(intent)
        }
        binding.btnEditProfilAdmin.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Load profile image from shared preferences
        val profileImage = sharedPreferences.getString("profileImage", null)
        if (profileImage != null) {
            val bitmap = BitmapFactory.decodeFile(profileImage)
            binding.ImageProfilUserAdmin.setImageBitmap(getRoundedBitmap(bitmap))
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val PROFILE_IMAGE_SIZE_DP = 115
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            if (selectedImageUri != null) {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
                    val resizedBitmap = resizeBitmap(bitmap, PROFILE_IMAGE_SIZE_DP)
                    val roundedBitmap = getRoundedBitmap(resizedBitmap)
                    binding.ImageProfilUserAdmin.setImageBitmap(roundedBitmap)

                    // Save profile image to shared preferences
                    val profileImage = saveImageToInternalStorage(resizedBitmap)
                    sharedPreferences.edit().putString("profileImage", profileImage).apply()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, sizeInDp: Int): Bitmap {
        val density = resources.displayMetrics.density
        val sizeInPixels = (sizeInDp * density).toInt()
        return Bitmap.createScaledBitmap(bitmap, sizeInPixels, sizeInPixels, true)
    }

    private fun getRoundedBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val radius = bitmap.width / 2f
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = Color.WHITE
        canvas.drawRoundRect(rectF, radius, radius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val contextWrapper = ContextWrapper(requireContext())
        val directory = contextWrapper.getDir("profileImages", Context.MODE_PRIVATE)
        val file = File(directory, "profile.jpg")

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnKeluarAdmin -> {
                auth.signOut()
                requireActivity().finish()
            }
            R.id.btnEditProfilAdmin -> {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            }
        }
    }
}
