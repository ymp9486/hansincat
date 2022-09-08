package com.example.hansimcat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hansimcat.databinding.FragmentHeartBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HeartFragment : Fragment() {
    private var _binding: FragmentHeartBinding? = null
    private val binding get() = _binding!!

    private val boardDataList = mutableListOf<board>()
    private val boardKeyList = mutableListOf<String>()

    private lateinit var boardRVAdapter : BoardListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeartBinding.inflate(inflater, container, false)

        boardRVAdapter = BoardListAdapter(boardDataList)
        binding.boardListView.adapter = boardRVAdapter

        binding.boardListView.setOnItemClickListener { parent, view, positon, id ->

//            intent.putExtra("title", boardDataList[positon].title)
//            intent.putExtra("content", boardDataList[positon].content)
//            intent.putExtra("time", boardDataList[positon].time)
//            startActivity(intent)

            val intent = Intent(context, BoardinsideActivity::class.java)
            intent.putExtra("key", boardKeyList[positon])
            startActivity(intent)
        }

        getFBBoardData()

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, HeartWriteActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
    private fun getFBBoardData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                for (dataModel in dataSnapshot.children) {
//                    dataModel.key
                    val item = dataModel.getValue(board::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())

                }
                boardKeyList.reverse()
                boardDataList.reverse()
                boardRVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        boardRef.addValueEventListener(postListener)
    }
}