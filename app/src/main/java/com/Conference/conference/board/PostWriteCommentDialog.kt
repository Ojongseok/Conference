package com.Conference.conference.board

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.Conference.conference.R
import com.Conference.conference.home.ProgramCommentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dialog_comment.*

class PostWriteCommentDialog(val context: Context, val programKey : String) {
    private val dialog = Dialog(context)
    private val user = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    fun showDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_comment)
        dialog.show()

        dialog.comment_write_ok_btn.setOnClickListener {
            if (dialog.comment_contents_tv.text.isNotEmpty()) {
                val commentList = ProgramCommentDTO()
                commentList.uid = user.currentUser?.uid!!
                commentList.comment = dialog.comment_contents_tv.text.toString()
                commentList.timestamp = System.currentTimeMillis()
                commentList.nickname = user.currentUser?.email?.split('@')!![0]

                updateComment(programKey,commentList)
            } else {
                Toast.makeText(context,"댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.comment_close_btn.setOnClickListener {
            dialog.dismiss()
        }
    }
    private fun updateComment(programKey: String, commentList: ProgramCommentDTO) {
        db.collection("post").document(programKey)
            .collection("comment").document()
            ?.set(commentList)
            ?.addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(context,"댓글을 작성했습니다.",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Log.d("태그", "댓글 작성 에러")
            }
    }
}