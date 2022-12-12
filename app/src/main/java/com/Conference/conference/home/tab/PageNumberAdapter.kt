package com.Conference.conference.home.tab

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.Conference.conference.R
import kotlinx.android.synthetic.main.item_page_number.view.*
import kotlinx.android.synthetic.main.item_program_list.view.*
import org.jsoup.select.Elements

class PageNumberAdapter(val context: Context, val pageNumber : Elements) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener
    private var selectedPosition = 0
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_page_number,viewGroup,false)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = (holder as CustomViewHolder).itemView

        view.item_page_number_tv.text = (position+1).toString()
        view.page_number_layout.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            itemClickListener.onClick(it, position)
        }
        if (selectedPosition == position) {
            view.item_page_number_tv.apply {
                setTypeface(view.item_page_number_tv.typeface,Typeface.BOLD)
                setTextColor(ContextCompat.getColor(context,R.color.main_blue))
            }
        } else {
            view.item_page_number_tv.apply {
                setTypeface(view.item_page_number_tv.typeface, Typeface.NORMAL)
                setTextColor(ContextCompat.getColor(context, R.color.black1))
            }
        }
    }
    inner class CustomViewHolder(var view : View) : RecyclerView.ViewHolder(view)
    override fun getItemCount() = pageNumber.size - 2

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}