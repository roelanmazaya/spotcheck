import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.spotcheck.Admin_EditPenyakitFragment
import com.example.spotcheck.Admin_PertanyaanFragment
import com.example.spotcheck.Admin_EditPertanyaanFragment
import com.example.spotcheck.Admin_PenyakitFragment
import com.example.spotcheck.R
import com.example.spotcheck.databinding.AdminHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Admin_HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: AdminHomePageBinding
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AdminHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPertanyaan.setOnClickListener {
            val intent = Intent(activity, Admin_PertanyaanFragment::class.java)
            startActivity(intent)
        }
        binding.btnPenyakit.setOnClickListener {
            val intent = Intent(activity, Admin_EditPenyakitFragment::class.java)
            startActivity(intent)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPertanyaan -> {
                val intent = Intent(activity, Admin_PertanyaanFragment::class.java)
                startActivity(intent)
            }
            R.id.btnPenyakit -> {
                val intent = Intent(activity, Admin_PenyakitFragment::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, fragment)
            .commit()
    }
}
