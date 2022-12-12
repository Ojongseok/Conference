package com.Conference.conference.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Conference.conference.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_program_comment.view.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class CommentAdapter(val context: Context, val programKey :String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var db: FirebaseFirestore? = null
    var commentList = ArrayList<ProgramCommentDTO>()
    init {
        db = FirebaseFirestore.getInstance()
        db?.collection("program")?.document(programKey)
            ?.collection("comment")?.orderBy("timestamp")?.get()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    commentList.clear()
                    for (i in it.result) {
                        commentList.add(i.toObject(ProgramCommentDTO::class.java)!!)
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

        val commentUid = commentList[position].uid
        view.pd_comment_contents.text = commentList[position].comment
        view.pd_comment_timestamp.text = SimpleDateFormat("yyyy-MM-dd hh:mm").format(commentList[position].timestamp)
        view.pd_comment_nickname.text = commentList[position].nickname
    }
    inner class CustomViewHolder(var view : View) : RecyclerView.ViewHolder(view)
    override fun getItemCount() = commentList.size
}