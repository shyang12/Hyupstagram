package kr.ac.seokyeong.hyupstagram

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.seokyeong.hyupstagram.R
import kr.ac.seokyeong.hyupstagram.model.User

class UserAdapter(val context: Context, val userList: ArrayList<User>) {

    class userViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.name_text)
    }
}