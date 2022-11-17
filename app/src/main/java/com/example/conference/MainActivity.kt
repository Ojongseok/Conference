package com.example.conference

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.conference.chat.ChatFragment
import com.example.conference.databinding.ActivityMainBinding
import com.example.conference.home.HomeFragment
import com.example.conference.mypage.MypageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var _Binding: ActivityMainBinding? = null
    private val binding get() = _Binding!!
    var auth : FirebaseAuth? = null
    private var clickable: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // 바텀내비게이션 세팅
        binding.mainBottomNav.setOnNavigationItemSelectedListener(this)
        binding.mainBottomNav.selectedItemId = R.id.nav_item1
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // 네비게이션 중복 클릭 시 팅김 방지
        if (isThrottleClick()) {
            when(item.itemId) {
                R.id.nav_item1 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container_layout, HomeFragment()).commit()
                    return true
                }
                R.id.nav_item2 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,ChatFragment()).commit()
                    return true
                }
                R.id.nav_item3 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,MypageFragment()).commit()
                    return true
                }
            }
        }
        return false
    }

    // 2초내 다시 클릭하면 앱 종료
    private var backPressedTime : Long = 0
    override fun onBackPressed() {
        Log.d("TAG", "뒤로가기")
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
            return
        }
        // 처음 클릭 메시지
        Toast.makeText(this, "'뒤로가기' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
    fun isThrottleClick(): Boolean {
        if (clickable) {
            clickable = false
            Handler(Looper.getMainLooper()).postDelayed({
                clickable = true
            }, 500)
            return true
        } else {
            Log.i("TAG", "waiting for a while")
            return false
        }
    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}
