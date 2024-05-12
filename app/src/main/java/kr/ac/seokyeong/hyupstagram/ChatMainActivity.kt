package kr.ac.seokyeong.hyupstagram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.ac.seokyeong.hyupstagram.databinding.ActivityChatMainBinding
import kr.ac.seokyeong.hyupstagram.databinding.ItemCommentBinding
import kr.ac.seokyeong.hyupstagram.model.User

class ChatMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatMainBinding
    lateinit var  adapter: UserAdapter

    lateinit var mAuth: FirebaseAuth
    lateinit var mDbRef: DatabaseReference

    lateinit var userList : ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth
        mDbRef = Firebase.database.reference

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        binding.userRecycelrView.layoutManager = LinearLayoutManager(this)
        binding.userRecycelrView.adapter = adapter

        // 사용자 정보 가져오기
        mDbRef.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children) {
                    // 유저 정보
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(mAuth.currentUser?.uid != currentUser?.uId) {
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 실패 시 실행
            }

        })
    }
}