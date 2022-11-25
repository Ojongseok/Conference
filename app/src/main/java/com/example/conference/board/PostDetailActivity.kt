package com.example.conference.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conference.databinding.ActivityPostDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

class PostDetailActivity : AppCompatActivity() {
    private var mBinding: ActivityPostDetailBinding? = null
    private val binding get() = mBinding!!
    private lateinit var user: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private var postId : String? = null
    private var postInfo = BoardListDTO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        postId = intent.getStringExtra("postId").toString()

        CoroutineScope(Dispatchers.Main).launch {
            val postInfo = async {
                getPostInfo()
            }
            postInfo.await().apply {
                binding.postDetailPb.visibility = View.GONE
            }
        }

        binding.postDetailCommentRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PostCommentAdapter(context, postId!!)
        }
        binding.postDetailBackBtn.setOnClickListener {
            finish()
        }
    }
    fun getPostInfo() {
        db.collection("post").document(postId!!).get().addOnSuccessListener {
                if (it.exists()) {
                    postInfo = it.toObject(BoardListDTO::class.java)!!

                    binding.postDetailNicknameTv.text = postInfo.nickname
                    binding.postDetailContentsTv.text = postInfo.contents
                    binding.postDetailTimestampTv.text = SimpleDateFormat("yyyy-MM-dd hh:mm").format(postInfo.timestamp)

                } else {
                    Log.d("태그","postdetailAcitivity db 호출 에러")
                }
            }
            .addOnFailureListener {
                Log.d("태그","postdetailAcitivity 호출 에러")
            }
    }
    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}