package kr.ac.seokyeong.hyupstagram.fragment

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import java.util.*
import kotlin.collections.*
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kr.ac.seokyeong.hyupstagram.R
import kr.ac.seokyeong.hyupstagram.databinding.FragmentDetailViewBinding
import kr.ac.seokyeong.hyupstagram.databinding.ItemDetailBinding
import kr.ac.seokyeong.hyupstagram.model.ContentModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var uid : String? = null
    var firestore : FirebaseFirestore? = null
    lateinit var binding : FragmentDetailViewBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_view, container, false)

        binding.detailviewRecyclerview.adapter = DetailViewRecyclerviewAdapter()
        binding.detailviewRecyclerview.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    inner class DetailViewHolder(var binding : ItemDetailBinding) : RecyclerView.ViewHolder(binding.root)

    inner class DetailViewRecyclerviewAdapter() : RecyclerView.Adapter<DetailViewHolder>() {
        var firestore = FirebaseFirestore.getInstance()
        var uid = FirebaseAuth.getInstance().currentUser?.uid
        var contentModels = arrayListOf<ContentModel>()
        var contentUidList = arrayListOf<String>()


        init {
            firestore.collection("images").addSnapshotListener { value, error ->
                contentModels.clear()
                contentUidList.clear()
                for (item in value!!.documents) {
                    var contentModel = item.toObject(ContentModel::class.java)
                    contentModels.add(contentModel!!)
                    contentUidList.add(item.id)
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
            var view = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DetailViewHolder(view)
        }

        override fun getItemCount(): Int {
            return contentModels.size
        }

        override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
            var contentModel = contentModels[position]

            holder.binding.profileTextview.text = contentModel.userId
            holder.binding.explainTextview.text = contentModel.explain
            Glide.with(holder.itemView.context).load(contentModel.imageUrl).into(holder.binding.contentImageview)
            holder.binding.likeTextview.text = "Like " + contentModel.favoriteCount

            // this code is the button is clicked
            holder.binding.favoriteImageview.setOnClickListener {
                favorirteEvent(position)
            }

            // This code is when the page is loaded
            if(contentModels[position].favorites.containsKey(uid)) {
                // this is like status
                holder.binding.favoriteImageview.setImageResource(R.drawable.ic_favorite)
            } else {
                // this is unlike status
                holder.binding.favoriteImageview.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        fun favorirteEvent(position : Int) {
            var tsDoc = firestore.collection("images").document(contentUidList[position])
            firestore.runTransaction { transaction ->
                var contentModel = transaction.get(tsDoc).toObject(ContentModel::class.java)

                if(contentModel!!.favorites.containsKey(uid)) {
                    // when the button is clicked
                    contentModel.favoriteCount = contentModel.favoriteCount - 1
                    contentModel.favorites.remove(uid)
                } else {
                    // when the button is not clicked
                    contentModel.favoriteCount = contentModel.favoriteCount + 1
                    contentModel.favorites[uid!!] = true
                }
                transaction.set(tsDoc, contentModel)
            }
        }

    }
}