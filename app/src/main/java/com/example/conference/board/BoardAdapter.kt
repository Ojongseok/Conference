package com.example.conference.board

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.conference.R
import com.example.conference.home.ProgramCommentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_post_list.view.*
import kotlinx.android.synthetic.main.item_program_comment.view.*
import java.text.SimpleDateFormat


class BoardAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var db: FirebaseFirestore? = null
    var user : FirebaseAuth? = null
    private val postList = ArrayList<BoardListDTO>()
    init {
        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance()

        db?.collection("post")?.orderBy("timestamp")?.addSnapshotListener { value, error ->
            if (value == null) return@addSnapshotListener  // 스냅샷이 살아있는데 다른데서 종료하려하면 자주 튕기니까 항상 달아줌
            postList.clear()
            for (snapshot in value.documents) {
                val post = snapshot.toObject(BoardListDTO::class.java)
                postList.add(post!!)
            }
            notifyDataSetChanged()
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_post_list,viewGroup,false)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = (holder as CustomViewHolder).itemView

        view.post_list_nickname_tv.text = postList[position].nickname
        view.post_list_time_tv.text = SimpleDateFormat("yyyy-MM-dd hh:mm").format(postList[position].timestamp)
        view.post_list_contents_tv.text = postList[position].contents


    }
    inner class CustomViewHolder(var view : View) : RecyclerView.ViewHolder(view)
    override fun getItemCount() = postList.size
}