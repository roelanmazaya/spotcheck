import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.spotcheck.databinding.UserRiwayatPageBinding

class User_HistoryFragment : Fragment() {
    private var _binding: UserRiwayatPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserRiwayatPageBinding.inflate(inflater, container, false)
        val view = binding.root

        // TODO: Add your code logic here

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
