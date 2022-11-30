package com.example.conference.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conference.databinding.ActivityPostDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_program_detail.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

class PostDetailActivity : AppCompatActivity() {
    private var mBinding: ActivityPostDetailBinding? = null
    private val binding get() = mBinding!!
    private lateinit var user: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var postId: String? = null
    private var postInfo = BoardListDTO()
    private lateinit var postCommentAdapter: PostCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        postId = intent.getStringExtra("postId").toString()
        postCommentAdapter = PostCommentAdapter(applicationContext, postId!!)

        CoroutineScope(Dispatchers.Main).launch {
            val postInfo = async {
                getPostInfo()
            }
            postInfo.await().apply {
                binding.postDetailPb.visibility = View.GONE
            }
            launch {
                delay(300)
                binding.postDetailCommentRv.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = postCommentAdapter
                    binding.postListCommentCountTv.text = postCommentAdapter.itemCount.toString()
                }
            }
        }

        binding.postDetailWriteCommentBtn.setOnClickListener {
            val dialog = PostWriteCommentDialog(this, postId!!)
            dialog.showDialog()
        }
        binding.postDetailBackBtn.setOnClickListener {
            finish()
        }
        swipeRefresh()
    }

    fun getPostInfo() {
        db.collection("post").document(postId!!).get().addOnSuccessListener {
            if (it.exists()) {
                postInfo = it.toObject(BoardListDTO::class.java)!!

                binding.postDetailTitleTv.text = postInfo.title
                binding.postDetailNicknameTv.text = postInfo.nickname
                binding.postDetailContentsTv.text = postInfo.contents
                binding.postDetailTimestampTv.text = SimpleDateFormat("yyyy-MM-dd hh:mm").format(postInfo.timestamp)
            }
        }
    }
    private fun swipeRefresh() {
        binding.refreshLayout.setOnRefreshListener {
            postCommentAdapter = PostCommentAdapter(applicationContext, postId!!)
            binding.postDetailCommentRv.adapter = postCommentAdapter
            Handler().postDelayed(1000) {
                binding.postListCommentCountTv.text = postCommentAdapter.itemCount.toString()
                refreshLayout.isRefreshing = false
            }
        }
    }
    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}