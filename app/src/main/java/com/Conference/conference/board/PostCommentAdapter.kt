package com.Conference.conference.board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Conference.conference.R
import com.Conference.conference.home.ProgramCommentDTO
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
        commentlist.clear()
        db?.collection("post")?.document(postId)
            ?.collection("comment")?.orderBy("timestamp")?.get()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    commentlist.clear()
                    for (i in it.result) {
                        commentlist.add(i.toObject(ProgramCommentDTO::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
            }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_program_comment,viewGroup,false)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = (holder as CustomViewHolder).itemView

        view.pd_comment_nickname.text = commentlist[position].nickname
        view.pd_comment_contents.text = commentlist[position].comment
        view.pd_comment_timestamp.text = SimpleDateFormat("yyyy-MM-dd hh:mm").format(commentlist[position].timestamp)


    }
    inner class CustomViewHolder(var view : View) : RecyclerView.ViewHolder(view)
    override fun getItemCount() = commentlist.size
}