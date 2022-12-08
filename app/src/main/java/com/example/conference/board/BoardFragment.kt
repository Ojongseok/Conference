package com.example.conference.board

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conference.databinding.FragmentBoardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_program_detail.*

class BoardFragment : Fragment() {
    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!
    lateinit var postAdapter : BoardAdapter
    lateinit var db : FirebaseFirestore
    lateinit var user : FirebaseAuth
    var verifyState = false
    init {
        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance()

        db.collection("user").document(user.currentUser?.uid!!).get().addOnSuccessListener {
            if (it.exists()) {
                verifyState = true
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBoardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boardListRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            postAdapter = BoardAdapter(requireContext())
            adapter = postAdapter
        }
        binding.writePostBtn.setOnClickListener {
            if (verifyState) {
                val dialog = WritePostDialog(requireContext())
                dialog.showDialog()
            } else {
                Toast.makeText(context,"교내 학생 인증이 완료된 회원만 게시글 작성이 가능합니다.",Toast.LENGTH_SHORT).show()
            }
        }
        swipeRefresh()
    }
    private fun swipeRefresh() {
        binding.refreshLayout.setOnRefreshListener {
            Handler().postDelayed(1000) {
                postAdapter = BoardAdapter(requireContext())
                binding.boardListRv.adapter = postAdapter
                refreshLayout.isRefreshing = false
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}