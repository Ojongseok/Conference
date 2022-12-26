package com.Conference.conference

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.Conference.conference.databinding.ActivityAgreeBinding
import com.Conference.conference.databinding.ActivitySplashBinding

class AgreeActivity : AppCompatActivity() {
    private var _Binding: ActivityAgreeBinding? = null
    private val binding get() = _Binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivityAgreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.agreeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}