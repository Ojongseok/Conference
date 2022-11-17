package com.example.conference.home.tab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conference.databinding.FragmentLearnBinding
import org.jsoup.Jsoup

class Learn : Fragment() {
    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLearnBinding.inflate(inflater, container, false)

        Thread(Runnable {
            val url = "https://do.sejong.ac.kr/ko/program/learn"
            val doc = Jsoup.connect(url).get()
            val programList = doc.select("ul.columns-4")
            val pageNumber = doc.select("div.pagination")
            Log.d("태그",pageNumber.select("li").size.toString())

            this@Learn.activity?.runOnUiThread {
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