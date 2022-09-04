package com.example.hansimcat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class BoardListAdapter(val boardList : MutableList<board>) : BaseAdapter() {
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {

        var view = contentView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item,parent, false)
        }

        val title = view?.findViewById<TextView>(R.id.titleArea2)
        val content = view?.findViewById<TextView>(R.id.contentSrea2)
        val time = view?.findViewById<TextView>(R.id.timeArea)

        title!!.text = boardList[position].title
        content!!.text = boardList[position].content
        time!!.text = boardList[position].time

        return view!!
    }
}