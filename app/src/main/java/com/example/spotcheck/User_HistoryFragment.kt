import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spotcheck.PrefManager
import com.example.spotcheck.R
import com.example.spotcheck.adapter.Adapter_Riwayat
import com.example.spotcheck.databinding.UserRiwayatPageBinding
import com.example.spotcheck.models.Riwayat
import com.example.spotcheck.models.Riwayat_model
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User_HistoryFragment : Fragment() {
    private var _binding: UserRiwayatPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefManager: PrefManager
    private lateinit var db: FirebaseFirestore
    private lateinit var riwayat: Riwayat
    private lateinit var listAdapter: Adapter_Riwayat
    private var listRiwayat = mutableListOf<Riwayat_model>()
    var user_id = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserRiwayatPageBinding.inflate(inflater, container, false)
        val view = binding.root

        // TODO: Add your code logic here
        prefManager = PrefManager(requireContext())
        user_id = prefManager.getId().toString()

        listRiwayat = mutableListOf<Riwayat_model>()
        listAdapter = Adapter_Riwayat(requireContext(), R.layout.item_riwayat, listRiwayat)
        binding.lvDataRiwat.adapter = listAdapter

        get_riwayat()

        return view
    }

    fun get_riwayat(){
        listRiwayat.clear()
        db = Firebase.firestore
        db.collection("riwayat").orderBy("created_at", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val listDoc: List<DocumentSnapshot> = result.documents
                    Log.d("Riwayat", "get_riwayat: "+listDoc.size)
                    var total_row = 0
                    var i = 1
                    for (d in listDoc) {
                        riwayat = d.toObject(Riwayat::class.java)!!

                        if(riwayat.user_id==user_id){
                            listRiwayat.add(
                                Riwayat_model(i, riwayat.id, riwayat.user_id,
                                    riwayat.hasil, riwayat.pict, riwayat.solusi, riwayat.created_at)
                            )
                            total_row++
                        }
                        i++
                    }

                    listAdapter.notifyDataSetChanged()
                    if(total_row>0){
                        binding.lvDataRiwat.visibility = View.VISIBLE
                        binding.textView19.visibility = View.GONE
                    }else{
                        binding.lvDataRiwat.visibility = View.GONE
                        binding.textView19.visibility = View.VISIBLE
                    }
                }else{
                    Log.d("Riwayat", "get_riwayat: Kosong")
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Gagal mendapatkan data: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


