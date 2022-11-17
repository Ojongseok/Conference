package com.example.conference.home.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conference.databinding.FragmentCareerBinding
import org.jsoup.Jsoup

class Career : Fragment() {
    private var _binding: FragmentCareerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCareerBinding.inflate(inflater, container, false)

        Thread(Runnable {
            val url = "https://do.sejong.ac.kr/ko/program/career"
            val doc = Jsoup.connect(url).get()
            val programList = doc.select("ul.columns-4")

            this@Career.activity?.runOnUiThread {
                if (programList.select("li.empty").text() == "등록된 프로그램이 없습니다.") {
                    binding.careerNothing.visibility = View.VISIBLE
                }
                else {
                    binding.programMajorRv.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = ProgramListAdapter(requireContext(),programList)
                    }
                    binding.majorPb.visibility = View.GONE
                }
            }
        }).start()


        return binding.root
    }
}