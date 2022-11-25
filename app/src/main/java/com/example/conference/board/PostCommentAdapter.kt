package com.example.conference.board

import android.content.Context
import android.content.Intent
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

class PostCommentAdapter(val context: Context, val postId : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var db: FirebaseFirestore? = null
    var user : FirebaseAuth? = null
    private val commentlist = ArrayList<ProgramCommentDTO>()
    init {
        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance()

        db?.collection("post")?.document(postId)
            ?.collection("comment")?.addSnapshotListener { value, error ->
                if (value == null) return@addSnapshotListener  // 스냅샷이 살아있는데 다른데서 종료하려하면 자주 튕기니까 항상 달아줌
                commentlist.clear()
                for (snapshot in value.documents) {
                    commentlist.add(snapshot.toObject(ProgramCommentDTO::class.java)!!)
                }
            }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_program_comment,viewGroup,false)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = (holder as CustomViewHolder).itemView


    }
    inner class CustomViewHolder(var view : View) : RecyclerView.ViewHolder(view)
    override fun getItemCount() = 3
}