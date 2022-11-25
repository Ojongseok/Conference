package com.example.conference.home.tab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conference.databinding.FragmentCareerBinding
import com.example.conference.databinding.FragmentCounselBinding
import com.example.conference.databinding.FragmentLearnBinding
import com.example.conference.databinding.FragmentMajorBinding
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.lang.Runnable

class Major : Fragment() {
    private var _binding: FragmentMajorBinding? = null
    private val binding get() = _binding!!
    private var programListAdapter: ProgramListAdapter? = null
    private var pageAdapter: PageNumberAdapter? = null
    private lateinit var programList : Elements
    private lateinit var pageNumber : Elements

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMajorBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.Default).launch {
            val init = CoroutineScope(Dispatchers.Default).async {
                initList(1)
            }
            init.await().apply {
                withContext(Dispatchers.Main) {
                    if (programList.select("li.empty").isNotEmpty()) {
                        binding.nothingTv.visibility = View.VISIBLE
                        binding.programListPb.visibility = View.GONE
                    }
                    else {
                        binding.programRv.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            programListAdapter = ProgramListAdapter(requireContext(), programList)
                            adapter = programListAdapter
                        }
                        binding.programPageRv.apply {
                            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            pageAdapter = PageNumberAdapter(requireContext(), pageNumber)
                            adapter = pageAdapter
                        }
                        binding.programListPb.visibility = View.GONE

                        pageAdapter?.setItemClickListener(object : PageNumberAdapter.OnItemClickListener {
                            override fun onClick(v: View, position: Int) {
                                // 클릭 시 이벤트 작성
                                CoroutineScope(Dispatchers.Default).async {
                                    initList(position+1)
                                    withContext(Dispatchers.Main) {
                                        binding.programRv.apply {
                                            layoutManager = LinearLayoutManager(requireContext())
                                            programListAdapter = ProgramListAdapter(requireContext(), programList)
                                            adapter = programListAdapter
                                        }

                                    }
                                }
                            }
                        })
                    }
                }
            }
        }
        return binding.root
    }
    fun initList(page : Int) {
        val url = "https://do.sejong.ac.kr/ko/program/major/list/all/${page}"
        val doc = Jsoup.connect(url).get()
        programList = doc.select("ul.columns-4")
        pageNumber = doc.select("div.pagination").select("li")
    }
}