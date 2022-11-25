package com.example.conference.board

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conference.databinding.FragmentBoardBinding
import kotlinx.android.synthetic.main.activity_program_detail.*

class BoardFragment : Fragment() {
    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!
    lateinit var postAdapter : BoardAdapter
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
            val dialog = WritePostDialog(requireContext())
            dialog.showDialog()
        }
        swipeRefresh()
    }
    private fun swipeRefresh() {
        binding.refreshLayout.setOnRefreshListener {
            Handler().postDelayed(1000) {
                postAdapter.notifyDataSetChanged()
                refreshLayout.isRefreshing = false
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}