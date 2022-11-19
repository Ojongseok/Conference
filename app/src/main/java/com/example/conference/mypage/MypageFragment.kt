package com.example.conference.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.conference.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MypageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!
    private lateinit var user : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        user = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        binding.userVerifyBtn.setOnClickListener {
            val dialog = VerifyDialog(requireContext())
            dialog.showDialog()
        }
    }

    private fun initData() {
        checkVerify()
        binding.userEmailTv.text = user?.currentUser?.email
    }
    private fun checkVerify()  {
        db.collection("user").document(user.currentUser?.uid!!).get()
            .addOnCompleteListener {
                binding.userVerifyBtn.apply {
                    text = "회원 인증 완료"
                    isClickable = false
                }
                binding.mypagePb.visibility = View.GONE
            }
            .addOnFailureListener {
                binding.userVerifyBtn.text = "회원 인증"
                binding.mypagePb.visibility = View.GONE
            }
    }
}