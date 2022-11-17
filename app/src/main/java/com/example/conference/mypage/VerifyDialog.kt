package com.example.conference.mypage

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.conference.R
import kotlinx.android.synthetic.main.dialog_sejong_verify.*
import org.jsoup.Connection
import org.jsoup.Jsoup

class VerifyDialog(val context : Context) {
    private val dialog = Dialog(context)
    val userAgent = R.string.clawling_userAgent.toString()

    fun showDialog() {
        dialog.setContentView(R.layout.dialog_sejong_verify)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        dialog.verify_ok_btn.setOnClickListener {
            dialog.verify_dialog_pb.visibility = View.VISIBLE
            sejongVerify()
        }
        dialog.verify_cancel_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun sejongVerify() {
        val data = HashMap<String,String>()
        data["userId"] = dialog.login_input_std_id.text.toString()
        data["password"] = dialog.login_input_std_password.text.toString()

        Thread {
            val loginForm  = Jsoup.connect("http://classic.sejong.ac.kr/userLoginPage.do")
                .timeout(3000)
                .method(Connection.Method.GET)
                .execute()

            val homePage = Jsoup.connect("http://classic.sejong.ac.kr/userLogin.do")
                .timeout(3000)
                .data(data)
                .method(Connection.Method.POST)
                .userAgent(userAgent)
                .cookies(loginForm.cookies())
                .execute()

            if (homePage.body().contains("접속자 정보")){
                registerUserInfo()
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    dialog.dismiss()
                    Toast.makeText(context,"교내 학생 인증에 성공했습니다.",Toast.LENGTH_SHORT).show()
                },0)
            } else {
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    Toast.makeText(context,"교내 학생 인증에 실패했습니다.",Toast.LENGTH_SHORT).show()
                },0)
            }
        }.start()
    }
    private fun registerUserInfo() {
        TODO("Not yet implemented")
    }
}