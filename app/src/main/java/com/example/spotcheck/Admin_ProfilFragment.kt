import android.annotation.SuppressLint
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
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.spotcheck.LoginAct
import com.example.spotcheck.PrefManager
import com.example.spotcheck.R
import com.example.spotcheck.databinding.AdminProfilPageBinding
import com.example.spotcheck.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Admin_ProfilFragment : Fragment(), View.OnClickListener {
    private var _binding: AdminProfilPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefManager: PrefManager
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    private lateinit var db: FirebaseFirestore
    var image_old = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminProfilPageBinding.inflate(inflater, container, false)
        val view = binding.root


        prefManager = PrefManager(requireActivity())
        checkLogin()

        binding.txUsernameAdmin.text = prefManager.getNama()
        binding.txEmailProfilAdmin.text = prefManager.getEmail()
        binding.txUsiaProfilAdmin.text = prefManager.getUsia()

        binding.btnKeluarAdmin
        binding.btnEditProfilAdmin
        binding.btnDeleteProfil

        auth = Firebase.auth
        storage = Firebase.storage
        sharedPreferences = requireContext().getSharedPreferences("ProfileImage", Context.MODE_PRIVATE)

        db = Firebase.firestore
        db.collection("users").document(prefManager.getId()!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject<User>()
                    image_old = user?.image.toString()
                    storageReference.child("file/$image_old").downloadUrl.addOnSuccessListener {
                        Glide.with(requireActivity())
                            .load(it)
                            .circleCrop()
                            .into(binding.ImageProfilUserAdmin)

                        Log.e("Firebase", "download passed")
                    }.addOnFailureListener {

                        Log.e("Firebase", "Failed in downloading")
                    }
                    Log.d("Cek Data User", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("Cek Data User", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Cek Data User", "get failed with ", exception)
            }

        binding.btnKeluarAdmin.setOnClickListener {
            val intent = Intent(requireContext(), LoginAct::class.java)
            startActivity(intent)
            prefManager.removeData()
            requireActivity().finish()
        }

        storageReference  = Firebase.storage.reference

        binding.btnEditProfilAdmin.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imagePickerActivityResult.launch(galleryIntent)
        }

        binding.btnDeleteProfil.setOnClickListener {
            if(image_old.trim().isNotEmpty()){
                val tp = HashMap<String, Any>()
                tp["image"] = ""
                db = Firebase.firestore
                db.collection("users").document(prefManager.getId()!!).update(tp)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Image berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Create a reference to the file to delete
                        val desertRef = storageReference.child("file/${image_old}")
                        // Delete the file
                        desertRef.delete().addOnSuccessListener {

                        }.addOnFailureListener {e ->
                            Log.d("Hapus Image", ": File Image gagal dihapus, ${e.message}")
                        }


                        Glide.with(requireActivity())
                            .load(requireActivity().getResources().getDrawable(R.drawable.user3))
                            .circleCrop()
                            .into(binding.ImageProfilUserAdmin)
                        Log.d("Hapus Image", ": File Image berhasil dihapus")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "Image gagal dihapus: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

//        binding.btnEditProfilAdmin.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, PICK_IMAGE_REQUEST)
//        }
//
//        // Load profile image from shared preferences
//        val profileImage = sharedPreferences.getString("profileImage", null)
//        if (profileImage != null) {
//            val bitmap = BitmapFactory.decodeFile(profileImage)
//            binding.ImageProfilUserAdmin.setImageBitmap(getRoundedBitmap(bitmap))
//        }

        return view
    }

    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
    // lambda expression to receive a result back, here we
        // receive single item(photo) on selection
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result != null) {
                    // getting URI of selected Image
                    val imageUri: Uri? = result.data?.data

                    // val fileName = imageUri?.pathSegments?.last()

                    // extract the file name with extension
                    val sd = getFileName(requireContext(), imageUri!!)

                    // Upload Task with upload to directory 'file'
                    // and name of the file remains same
                    val uploadTask = storageReference.child("file/$sd").putFile(imageUri)

                    // On success, download the file URL and display it
                    uploadTask.addOnSuccessListener {

                        //delete image lama
                        if(image_old.trim().isNotEmpty()){
                            // Create a reference to the file to delete
                            val desertRef = storageReference.child("file/${image_old}")

                            // Delete the file
                            desertRef.delete().addOnSuccessListener {
                                Log.d("Hapus Image", ": File Image berhasil dihapus")
                            }.addOnFailureListener {e ->
                                Log.d("Hapus Image", ": File Image gagal dihapus, ${e.message}")
                            }
                        }

                        //update image baru
                        val tp = HashMap<String, Any>()
                        tp["image"] = "$sd"
                        image_old = "$sd"
                        db = Firebase.firestore
                        db.collection("users").document(prefManager.getId()!!).update(tp)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Image berhasil diupdate",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    requireContext(),
                                    "Image gagal diupdate: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        // using glide library to display the image
                        storageReference.child("file/$sd").downloadUrl.addOnSuccessListener {
                            Glide.with(requireActivity())
                                .load(it)
                                .circleCrop()
                                .into(binding.ImageProfilUserAdmin)

                            Log.e("Firebase", "download passed")
                        }.addOnFailureListener {

                            Log.e("Firebase", "Failed in downloading")
                        }
                    }.addOnFailureListener {
                        Log.e("Firebase", "Image Upload fail")
                    }
                }
            }
        }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val PROFILE_IMAGE_SIZE_DP = 115
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
//            val selectedImageUri = data.data
//            if (selectedImageUri != null) {
//                try {
//                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
//                    val resizedBitmap = resizeBitmap(bitmap, PROFILE_IMAGE_SIZE_DP)
//                    val roundedBitmap = getRoundedBitmap(resizedBitmap)
//                    binding.ImageProfilUserAdmin.setImageBitmap(roundedBitmap)
//
//                    // Save profile image to shared preferences
//                    val profileImage = saveImageToInternalStorage(resizedBitmap)
//                    sharedPreferences.edit().putString("profileImage", profileImage).apply()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//
//    private fun resizeBitmap(bitmap: Bitmap, sizeInDp: Int): Bitmap {
//        val density = resources.displayMetrics.density
//        val sizeInPixels = (sizeInDp * density).toInt()
//        return Bitmap.createScaledBitmap(bitmap, sizeInPixels, sizeInPixels, true)
//    }
//
//    private fun getRoundedBitmap(bitmap: Bitmap): Bitmap {
//        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(output)
//        val paint = Paint()
//        val rect = Rect(0, 0, bitmap.width, bitmap.height)
//        val rectF = RectF(rect)
//        val radius = bitmap.width / 2f
//        paint.isAntiAlias = true
//        canvas.drawARGB(0, 0, 0, 0)
//        paint.color = Color.WHITE
//        canvas.drawRoundRect(rectF, radius, radius, paint)
//        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//        canvas.drawBitmap(bitmap, rect, rect, paint)
//        return output
//    }
//
//    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
//        val contextWrapper = ContextWrapper(requireContext())
//        val directory = contextWrapper.getDir("profileImages", Context.MODE_PRIVATE)
//        val file = File(directory, "profile.jpg")
//
//        try {
//            val outputStream = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//            outputStream.flush()
//            outputStream.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        return file.absolutePath
//    }

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

    private fun checkLogin(){
        if (prefManager.isLogin() == false){
            val intent = Intent(requireActivity(), LoginAct::class.java)
            startActivity(intent)
            requireActivity().finish()
//            requireActivity().getFragmentManager().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }
}
