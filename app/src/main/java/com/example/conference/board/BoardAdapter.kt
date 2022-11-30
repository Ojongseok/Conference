package com.example.conference.board

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.conference.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_post_list.view.*
import java.text.SimpleDateFormat


class BoardAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var db: FirebaseFirestore? = null
    var user : FirebaseAuth? = null
    var postId  = ArrayList<String>()
    private val postList = ArrayList<BoardListDTO>()
    private val commentCountList = ArrayList<Int>()
    init {
        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance()

        db?.collection("post")?.orderBy("timestamp")?.addSnapshotListener { value, error ->
            if (value == null) return@addSnapshotListener  // 스냅샷이 살아있는데 다른데서 종료하려하면 자주 튕기니까 항상 달아줌
            postList.clear()
            for (snapshot in value.documents) {
                val post = snapshot.toObject(BoardListDTO::class.java)
                postList.add(post!!)
                postId.add(snapshot.id)
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


        db?.collection("post")?.document(postId[position])
            ?.collection("comment")?.get()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    view.post_list_comment_count_tv.text = it.result.documents.size.toString()
                }
            }
        view.post_list_title_tv.text = postList[position].title
        view.post_list_nickname_tv.text = postList[position].nickname
        view.post_list_time_tv.text = SimpleDateFormat("yyyy-MM-dd hh:mm").format(postList[position].timestamp)
        view.post_list_contents_tv.text = postList[position].contents

        view.post_list_layout.setOnClickListener {
            val intent = Intent(context,PostDetailActivity::class.java)
            intent.putExtra("postId",postId[position])
            context.startActivity(intent)
        }
    }


    inner class CustomViewHolder(var view : View) : RecyclerView.ViewHolder(view)
    override fun getItemCount() = postList.size
}