package kr.ac.seokyeong.hyupstagram.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kr.ac.seokyeong.hyupstagram.R
import kr.ac.seokyeong.hyupstagram.model.AlarmDTO

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlarmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlarmFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_alarm, container, false)
        view.alarm_recyclerview.adapter = AlarmRecyclerviewAdapter()
        view.alarm_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }

    inner class AlarmRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var alarmDTOList = arrayListOf<AlarmDTO>()

        init {
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            FirebaseFirestore.getInstance().collection("alarms").whereEqualTo("destinationUid", uid).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                alarmDTOList.clear()
                if (querySnapshot == null) return@addSnapshotListener

                for (snapshot in querySnapshot.documents) {
                    alarmDTOList.add(snapshot.toObject(AlarmDTO::class.java)!!)
                }
                notifyDataSetChanged()
            }
        }
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(p0.context).inflate(R.layout.item_comment, p0, false)

            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            var view = p0.itemView

            FirebaseFirestore.getInstance().collection("profileImages").document(alarmDTOList[p1].uid!!).get().addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val url = task.result!!["image"]
                    Glide.with(view.context).load(url).apply(RequestOptions().circleCrop()).into(view.commentviewitem_imageview_profile)
                }
            }

            when(alarmDTOList[p1].kind){
                0 -> {
                    val str_0 = alarmDTOList[p1].userId + getString(R.string.alarm_favorite)
                    view.commentviewitem_tv_profile.text = str_0
                }
                1 -> {
                    val str_0 = alarmDTOList[p1].userId + " " + getString(R.string.alarm_comment) +" of " + alarmDTOList[p1].message
                    view.commentviewitem_tv_profile.text = str_0
                }
                2 -> {
                    val str_0 = alarmDTOList[p1].userId + " " + getString(R.string.alarm_follow)
                    view.commentviewitem_tv_profile.text = str_0
                }
            }
            view.commentviewitem_tv_comment.visibility = View.INVISIBLE
        }

        override fun getItemCount(): Int {
            return alarmDTOList.size
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlarmFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlarmFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}