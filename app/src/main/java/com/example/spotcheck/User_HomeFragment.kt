import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.spotcheck.R
import com.example.spotcheck.databinding.UserHomePageBinding
import com.example.spotcheck.User_PertanyaanCek_Act
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class User_HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: UserHomePageBinding
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCek.setOnClickListener {
            val intent = Intent(activity, User_PertanyaanCek_Act::class.java)
            startActivity(intent)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnCek -> {
                val intent = Intent(activity, User_PertanyaanCek_Act::class.java)
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
