import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.spotcheck.databinding.UserProfilPageBinding
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

class User_ProfilFragment : Fragment(), View.OnClickListener {
    private var _binding: UserProfilPageBinding? = null
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
        _binding = UserProfilPageBinding.inflate(inflater, container, false)
        val view = binding.root

        prefManager = PrefManager(requireActivity())
        checkLogin()

        binding.txUsername.text = prefManager.getNama()
        binding.txEmailProfil.text = prefManager.getEmail()
        binding.txUsiaProfil.text = prefManager.getUsia()

        binding.btnKeluar
        binding.btnEditProfil
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
                            .into(binding.ImageProfilUser)

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


        binding.btnKeluar.setOnClickListener {
            val intent = Intent(requireContext(), LoginAct::class.java)
            startActivity(intent)
            prefManager.removeData()
            requireActivity().finish()
        }

        storageReference  = Firebase.storage.reference

        binding.btnEditProfil.setOnClickListener {
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
                            .into(binding.ImageProfilUser)
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
                                .into(binding.ImageProfilUser)

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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnKeluarAdmin -> {
                auth.signOut()
                requireActivity().finish()
            }
            R.id.btnEditProfil -> {
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
        }
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }

}
