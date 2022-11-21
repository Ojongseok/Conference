package com.example.conference.home

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.conference.databinding.ActivityProgramDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class ProgramDetailActivity : AppCompatActivity() {
    private var _Binding: ActivityProgramDetailBinding? = null
    private val binding get() = _Binding!!
    var db: FirebaseFirestore? = null
    var user: FirebaseAuth? = null
    lateinit var programKey: String
    lateinit var commentAdapter : CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivityProgramDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance()

        val url = intent.getStringExtra("href").toString()
        programKey = url.substring(46, 50)
        commentAdapter = CommentAdapter(this,programKey)

        CoroutineScope(Dispatchers.Default).launch {
            val doc = Jsoup.connect(url).get()
            val imageUrl = "https://do.sejong.ac.kr" + doc.select("div.cover").attr("style")
                .replace("background-image:url(", "")
                .replace(");", "")
            withContext(Dispatchers.Main) {
                viewSetting(doc, imageUrl)
            }
        }
    }
    private fun viewSetting(doc: Document, imageUrl: String) {
        Glide.with(applicationContext).load(imageUrl).into(binding.programDetailMainIv)
        binding.programDetailFromTv.text =
            doc.select("div.info").select("div.department").text().replace(" ", " - ")
        binding.programDetailTitleTv.text = doc.select("div.title").select("h4").text()
        binding.pdTargetTv.text = "모집대상 : " + doc.select("li.target").select("span").text()
        binding.pdGradeTv.text = "학년/성별 : " + doc.select("div.title").select("ul").select("span")[1].text()
        binding.pdMajorTv.text = "학과 : " + doc.select("div.title").select("ul").select("span")[2].text()
        binding.pdTimeTv.text =
            "신청/마감일자 : " + doc.select("div.form").select("p")[2].text() + "\n" +
                    "운영일자 : " + doc.select("div.form").select("p")[0].text()
        doc.select("div.description div[data-role=wysiwyg-content]").first().children()
            .forEach {
                binding.pdContentTv.append(it.text() + "\n")
            }
        binding.pdContentTv.append("\n" + "현황 : " + doc.select("li.tbody").first().children()[2].text())

        binding.pdCommentWriteBtn.setOnClickListener {
            val writeCommentDialog = WriteCommentDialog(this, programKey)
            writeCommentDialog.showDialog()
        }
        binding.pdCommentRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
        }.handler.postDelayed({
            binding.programCommentTv.text = "댓글 " + commentAdapter.itemCount.toString() + "개"
        }, 500)

        binding.programDetailPb.visibility = View.INVISIBLE
        binding.programDetailBackBtn.setOnClickListener {
            finish()
        }
        binding.pdWebBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("${intent.getStringExtra("href").toString()}"))
            startActivity(intent)
        }
    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}