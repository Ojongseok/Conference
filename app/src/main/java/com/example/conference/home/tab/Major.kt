package com.example.conference.home.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conference.databinding.FragmentMajorBinding
import org.jsoup.Jsoup

class Major : Fragment() {
    private var _binding: FragmentMajorBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMajorBinding.inflate(inflater, container, false)

        Thread(Runnable {
            val url = "https://do.sejong.ac.kr/ko/program/major"
            val doc = Jsoup.connect(url).get()
            val programList = doc.select("ul.columns-4")

            this@Major.activity?.runOnUiThread {
                binding.programMajorRv.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = ProgramListAdapter(requireContext(),programList)
                }
                binding.majorPb.visibility = View.GONE
            }
        }).start()


        return binding.root
    }
}