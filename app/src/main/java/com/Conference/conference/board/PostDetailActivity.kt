package com.Conference.conference.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.Conference.conference.R
import com.Conference.conference.UserInfoDTO
import com.Conference.conference.databinding.ActivityPostDetailBinding
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
            async {
                getPostInfo()
            }
            async {
                getFavoriteState()
            }
            async {
                delay(300)
                binding.postDetailCommentRv.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = postCommentAdapter
                    binding.postListCommentCountTv.text = postCommentAdapter.itemCount.toString()

                }
                binding.postDetailPb.visibility = View.GONE
            }
        }

        binding.postDetailWriteCommentBtn.setOnClickListener {
            val dialog = PostWriteCommentDialog(this, postId!!)
            dialog.showDialog()
        }
        binding.postDetailFavoriteBtn.setOnClickListener {
            favoriteEvent()
        }
        binding.postDetailBackBtn.setOnClickListener {
            finish()
        }
        swipeRefresh()
    }

    private fun getFavoriteState() {
        db.collection("user").document(user.currentUser?.uid!!).addSnapshotListener { value, error ->
            if (value == null) return@addSnapshotListener
            if (value.data != null) {
                val map = value.data!!["favoritePost"] as Map<*, *>?
                if (map?.get(postId) == true) {
                    if (!this.isFinishing) {
                        Glide.with(this).load(R.drawable.ic_star_on).into(binding.postDetailFavoriteBtn)
                    }
                } else {
                    if (!this.isFinishing) {
                        Glide.with(this).load(R.drawable.ic_star_off).into(binding.postDetailFavoriteBtn)
                    }
                }
            }
        }
    }
    private fun favoriteEvent() {
        val doc = db.collection("user").document(user.currentUser?.uid!!)
        val postDoc = db.collection("post").document(postId.toString())
        db.runTransaction {
            val userInfoDTO = it.get(doc).toObject(UserInfoDTO::class.java)
            val postInfoDTO = it.get(postDoc).toObject(BoardListDTO::class.java)
            if (userInfoDTO?.favoritePost?.containsKey(postId!!)!!) {
                userInfoDTO.favoritePost.remove(postId!!)
                postInfoDTO!!.favoriteCount--
            } else {
                userInfoDTO.favoritePost[postId.toString()] = true
                postInfoDTO!!.favoriteCount++
            }
            it.set(doc,userInfoDTO)
            it.set(postDoc,postInfoDTO)
            return@runTransaction
        }
    }
    fun getPostInfo() {
        db.collection("post").document(postId!!).get().addOnSuccessListener {
            if (it.exists()) {
                postInfo = it.toObject(BoardListDTO::class.java)!!

                binding.postDetailTitleTv.text = postInfo.title
                binding.postDetailNicknameTv.text = postInfo.nickname
                binding.postDetailContentsTv.text = postInfo.contents
                binding.postDetailTimestampTv.text = SimpleDateFormat("yyyy-MM-dd hh:mm").format(postInfo.timestamp)
                binding.postListFavoriteCountTv.text = postInfo.favoriteCount.toString()
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