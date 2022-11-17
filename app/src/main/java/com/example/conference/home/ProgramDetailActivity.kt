package com.example.conference.home

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.conference.databinding.ActivityProgramDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jsoup.Jsoup
import java.lang.Runnable
import java.util.logging.Handler

class ProgramDetailActivity : AppCompatActivity() {
    private var _Binding: ActivityProgramDetailBinding? = null
    private val binding get() = _Binding!!
    var db: FirebaseFirestore? = null
    var user : FirebaseAuth? = null
    lateinit var programKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivityProgramDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url = intent.getStringExtra("href").toString()
        programKey = url.substring(46, 50)

        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance()
//        val commentAdapter = CommentAdapter(this,programKey)

        Thread(Runnable {
            val doc = Jsoup.connect(url).get()
            val imageUrl = "https://do.sejong.ac.kr" + doc.select("div.cover").attr("style")
                .replace("background-image:url(", "")
                .replace(");", "")

            this@ProgramDetailActivity.runOnUiThread {
                Glide.with(this).load(imageUrl).into(binding.programDetailMainIv)
                binding.programDetailFromTv.text =
                    doc.select("div.info").select("div.department").text().replace(" ", " - ")
                binding.programDetailTitleTv.text = doc.select("div.title").select("h4").text()
                binding.pdTargetTv.text = "모집대상 : " + doc.select("li.target").select("span").text()
                binding.pdGradeTv.text =
                    "학년/성별 : " + doc.select("div.title").select("ul").select("span")[1].text()
                binding.pdMajorTv.text =
                    "학과 : " + doc.select("div.title").select("ul").select("span")[2].text()
                binding.pdTimeTv.text =
                    "신청/마감일자 : " + doc.select("div.form").select("p")[2].text() + "\n" +
                            "운영일자 : " + doc.select("div.form").select("p")[0].text()
                doc.select("div.description div[data-role=wysiwyg-content]").first().children()
                    .forEach {
                        binding.pdContentTv.append(it.text() + "\n")
                    }
                binding.pdContentTv.append(
                    "\n" + "현황 : " + doc.select("li.tbody").first().children()[2].text()
                )
                binding.programDetailPb.visibility = View.INVISIBLE

//                binding.pdCommentRv.apply {
//                    layoutManager = LinearLayoutManager(context)
//                    adapter = commentAdapter
//                }.handler.postDelayed({
//                    binding.programCommentTv.text = "댓글 " + commentAdapter.itemCount.toString() + "개"
//                }, 200)

            }
        }).start()


//        binding.pdCommentWriteBtn.setOnClickListener {
//            val mDialogView = LayoutInflater.from(this).inflate(R.layout.comment_dialog, null)
//            val mBuilder = AlertDialog.Builder(this)
//                .setView(mDialogView)
//            mBuilder.show()
//
//            mDialogView.comment_write_ok_btn.setOnClickListener {
//                if (mDialogView.comment_contents_tv.text.isNotEmpty()) {
//                    val commentList = ProgramCommentDTO()
//                    commentList.uid = user?.currentUser?.uid
//                    commentList.comment = mDialogView.comment_contents_tv.text.toString()
//                    commentList.timestamp = System.currentTimeMillis()
//
//                    updateComment(programKey,commentList)
//                } else {
//                    Toast.makeText(applicationContext,"댓글을 입력해주세요.",Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
        binding.programDetailBackBtn.setOnClickListener {
            finish()
        }
        binding.pdWebBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("${intent.getStringExtra("href").toString()}"))
            startActivity(intent)
        }
    }

//    private fun updateComment(programKey: String, commentList: ProgramCommentDTO) {
//        db?.collection("program")?.document("learn")
//            ?.collection(programKey)?.document()?.set(commentList)
//            ?.addOnSuccessListener {
//                Toast.makeText(applicationContext,"댓글을 작성했습니다.",Toast.LENGTH_SHORT).show()
//            }?.addOnFailureListener {
//                Log.d("태그", "댓글 작성 에러")
//            }
//    }
//    interface CommentCount {
//        fun commentCount() : Int
//    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}