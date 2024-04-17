package kr.ac.seokyeong.hyupstagram.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kr.ac.seokyeong.hyupstagram.R
import kr.ac.seokyeong.hyupstagram.databinding.ActivityCommentBinding
import kr.ac.seokyeong.hyupstagram.databinding.ItemCommentBinding
import kr.ac.seokyeong.hyupstagram.model.AlarmDTO
import kr.ac.seokyeong.hyupstagram.model.ContentModel
import kr.ac.seokyeong.hyupstagram.util.FcmPush

class CommentActivity : AppCompatActivity() {
    lateinit var binding : ActivityCommentBinding
    var contentUid : String? = null
    var destinationUid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment)

        contentUid = intent.getStringExtra("contentUid")
        destinationUid = intent.getStringExtra("destinationUid")

        binding.commentRecyclerview.adapter = CommentRecyclerviewAdapter()
        binding.commentRecyclerview.layoutManager = LinearLayoutManager(this)

        binding.commentBtnSend.setOnClickListener {
            var comment = ContentModel.Comment()
            comment.userId = FirebaseAuth.getInstance().currentUser?.email
            comment.uid = FirebaseAuth.getInstance().currentUser?.uid
            comment.comment = binding.commentEditMessage.text.toString()
            comment.timestamp = System.currentTimeMillis()

            FirebaseFirestore.getInstance().collection("images").document(contentUid!!).collection("comments").document().set(comment)
            commentAlarm(destinationUid!!, binding.commentEditMessage.text.toString())
            binding.commentEditMessage.setText("")
        }
    }

    fun commentAlarm(destinationUid : String, message : String) {
        var alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = FirebaseAuth.getInstance().currentUser?.email
        alarmDTO.kind = 1
        alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
        alarmDTO.timestamp = System.currentTimeMillis()
        alarmDTO.message = message
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)

        var msg = FirebaseAuth.getInstance().currentUser?.email + " " + getString(R.string.alarm_comment) + " of " + message
        FcmPush.instance.sendMessage(destinationUid, "Hyupstagram", msg)
    }
    inner class CommentHolder(var binding : ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

    inner class CommentRecyclerviewAdapter : RecyclerView.Adapter<CommentHolder>() {
        var comments = arrayListOf<ContentModel.Comment>()

        init {
            FirebaseFirestore.getInstance()
                .collection("images")
                .document(contentUid!!)
                .collection("comments")
                .orderBy("timestamp")
                .addSnapshotListener { quertSnapshot, firebaseFirestoreException ->
                    comments.clear()
                    if (quertSnapshot == null) return@addSnapshotListener

                    for (snapshot in quertSnapshot.documents!!) {
                        comments.add(snapshot.toObject(ContentModel.Comment::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CommentHolder {
            var view = ItemCommentBinding.inflate(LayoutInflater.from(p0.context), p0, false)
            return CommentHolder(view)
        }

        override fun getItemCount(): Int {
            return comments.size
        }

        override fun onBindViewHolder(holder: CommentHolder, position: Int) {
            holder.binding.commentviewitemTvComment.text = comments[position].comment
            holder.binding.commentviewitemTvProfile.text = comments[position].userId

            FirebaseFirestore.getInstance()
                .collection("profileImages")
                .document(comments[position].uid!!)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var url = task.result!!["image"]
                        Glide.with(holder.itemView.context).load(url)
                            .apply(RequestOptions().circleCrop())
                            .into(holder.binding.commentviewitemImageviewProfile)
                    }
                }
        }

    }
}