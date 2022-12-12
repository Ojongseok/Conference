package com.Conference.conference

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.Conference.conference.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private var _Binding: ActivitySplashBinding? = null
    private val binding get() = _Binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기화면 1초동안 띄워주기
        Handler().postDelayed({
            val intent = Intent(this, GoogleLoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },1000)
    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}