package com.example.conference.board

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.conference.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.dialog_comment.*
import kotlinx.android.synthetic.main.dialog_post_write.*
import kotlinx.android.synthetic.main.fragment_board.*

class WritePostDialog(val context: Context) {
    private val dialog = Dialog(context)
    private val user = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    fun showDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_post_write)
        dialog.show()

        dialog.post_write_ok_btn.setOnClickListener {
            if (dialog.post_write_contents_tv.text.isNotEmpty()) {
                val postList = BoardListDTO()
                postList.uid = user?.currentUser?.uid!!
                postList.contents = dialog.post_write_contents_tv.text.toString()
                postList.timestamp = System.currentTimeMillis()
                postList.nickname = user?.currentUser?.email!!.split('@')[0]

                updateComment(postList)
            } else {
                Log.d("태그","1")
                Toast.makeText(context,"내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.post_close_btn.setOnClickListener {
            dialog.dismiss()
        }
    }
    private fun updateComment(postList: BoardListDTO) {
        db?.collection("post")?.document()?.set(postList)
            ?.addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(context,"게시글을 작성했습니다.",Toast.LENGTH_SHORT).show()
            }?.addOnFailureListener {
                Log.d("태그", "게시글 작성 에러")
            }
    }
}