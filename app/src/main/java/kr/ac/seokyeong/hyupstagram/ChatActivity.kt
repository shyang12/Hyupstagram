package kr.ac.seokyeong.hyupstagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ChatActivity : AppCompatActivity() {
    private lateinit var receiverEmail : String
    private lateinit var receiverUid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // 넘어온 데이터 변수에 담기
        receiverEmail = intent.getStringExtra("email").toString()
        receiverUid = intent.getStringExtra("uId").toString()

        // 액션바에 상대방 이름 보여주기
        supportActionBar?.title = receiverEmail
    }
}