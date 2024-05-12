package kr.ac.seokyeong.hyupstagram

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.seokyeong.hyupstagram.R
import kr.ac.seokyeong.hyupstagram.model.User

class UserAdapter(val context: Context, val userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // 데이터 담기
        val currentUser = userList[position]
        // 화면에 데이터 보여주기
        holder.nameText.text = currentUser.email
        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            // 넘길 데이터
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uId", currentUser.uId)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.name_text) as TextView
    }
}