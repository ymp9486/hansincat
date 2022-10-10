package com.example.hansimcat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.hansimcat.databinding.FragmentMainBinding
import com.example.hansimcat.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyPageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var boardRVAdapter : BoardListAdapter

    private val boardDataList = mutableListOf<board>()
    private val boardKeyList = mutableListOf<String>()

    private fun getUemail() :String{
        auth = FirebaseAuth.getInstance()

        return auth.currentUser?.email.toString()
    }

    private fun getuid() :String{
        auth = FirebaseAuth.getInstance()

        return auth.currentUser?.uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        binding.myEmailTv.setText(getUemail())

        boardRVAdapter = BoardListAdapter(boardDataList)
        binding.boardListView2.adapter = boardRVAdapter

        binding.boardListView2.setOnItemClickListener { parent, view, positon, id ->

            val intent = Intent(context, BoardinsideActivity::class.java)
            intent.putExtra("key", boardKeyList[positon])
            startActivity(intent)
        }

        getFBBoardData()
        return binding.root
    }

    private fun getFBBoardData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardDataList.clear()

                for (dataModel in dataSnapshot.children) {
//                    dataModel.key
                    val item = dataModel.getValue(board::class.java)
                    val myUid = getuid()
                    val writteruid = item?.userId

                    if(myUid.equals(writteruid)){
                        boardDataList.add(item!!)
                        boardKeyList.add(dataModel.key.toString())
                    }else{

                    }

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