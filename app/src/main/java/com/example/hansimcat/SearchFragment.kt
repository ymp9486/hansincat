package com.example.hansimcat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hansimcat.databinding.FragmentMainBinding
import com.example.hansimcat.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    private val imageList: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.database
        database = db.getReference("FeedList")

        if (arguments?.getString("keyword")!=null){
//            검색
            val values =database.orderByChild("userId").equalTo(arguments?.getString("keyword"))
            values.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        imageList.add(data.child("imageUrl").value as String)
                    }
                    binding.serchRvRecommend.adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }else{
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val values = snapshot.value as ArrayList<HashMap<String, Any>>?
                    for (i: Int in 1 until (values?.size ?: 0)) {
                        val data = values?.get(i)
                        imageList.add(
                            data?.get("imageUrl") as String
                        )
                    }
                    binding.serchRvRecommend.adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

        binding.serchRvRecommend.layoutManager = GridLayoutManager(activity, 3)
        binding.serchRvRecommend.adapter = RecommendAdapter(activity as MainActivity,imageList)

        binding.searchLl.setOnClickListener{
            Intent(context, SearchActivity::class.java).apply {

            }.run { context?.startActivity(this) }
        }
    }
}