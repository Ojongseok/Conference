package com.example.conference.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.conference.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_program_comment.view.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class CommentAdapter(val context: Context, val programKey :String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var db: FirebaseFirestore? = null
    var commentList = ArrayList<ProgramCommentDTO>()
    init {
        db = FirebaseFirestore.getInstance()
        db?.collection("program")?.document(programKey)?.get()
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val commentDTO =it.result.toObject(ProgramCommentDTO::class.java)
                    if (commentDTO != null) {
                        db?.collection("program")?.orderBy("timestamp")
                            ?.addSnapshotListener { value, error ->
                                if (value==null) return@addSnapshotListener
                                commentList.clear()
                                for (snapshot in value.documents) {
                                    commentList.add(snapshot.toObject(ProgramCommentDTO::class.java)!!)
                                }
                                notifyDataSetChanged()
                            }
                    } else {
                        Log.d("태그","CommentAdapter 오류")
                    }
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
//        db?.collection("user")?.document(commentUid!!)?.get()
//            ?.addOnCompleteListener {
//                view.pd_comment_nickname.text = it.result.get("nickname").toString()
//                Glide.with(context).load(it.result.get("profileUrl").toString()).into(view.pd_comment_iv)
//            }
    }
    inner class CustomViewHolder(var view : View) : RecyclerView.ViewHolder(view)
    override fun getItemCount() = commentList.size
}