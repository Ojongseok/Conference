package com.example.conference.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.conference.GoogleLoginActivity
import com.example.conference.R
import com.example.conference.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MypageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!
    private lateinit var user : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    var verifyState : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        user = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        checkVerify()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        binding.mypageVerifyIv.setOnClickListener {
            val dialog = VerifyDialog(requireContext())
            dialog.showDialog()
        }
        binding.mypageLogoutBtn.setOnClickListener {
            Toast.makeText(requireContext(),"로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            user.signOut()
            startActivity(Intent(requireContext(),GoogleLoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun initData() {
        binding.userEmailTv.text = (user?.currentUser?.email)!!.split('@')[0] + "님 환영합니다 :)"
    }
    private fun checkVerify()  {
        db.collection("user").document(user.currentUser?.uid!!).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    binding.userVerifyTv.visibility = View.GONE
                    binding.mypageVerifyIv.setImageResource(R.drawable.ic_verify_on)
                    binding.mypageVerifyIv.isClickable = false
                    binding.mypagePb.visibility = View.GONE
                } else {
                    binding.userVerifyTv.text = "회원 인증을 진행해주세요."
                    binding.userVerifyTv.visibility = View.VISIBLE
                    binding.mypageVerifyIv.setImageResource(R.drawable.ic_verify_off)
                    binding.mypagePb.visibility = View.GONE
                }
            }
    }
}