package com.example.hansimcat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hansimcat.databinding.FragmentHomeBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.database
        database = db.getReference("FeedList")

    }

    override fun onStart() {
        super.onStart()

        var feedList = ArrayList<Feed>()

        binding.homeRvFeed.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        binding.homeRvFeed.adapter = FeedAdapter(activity as MainActivity, feedList)
        binding.homeRvFeed.isNestedScrollingEnabled = false

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                feedList.clear()

                val values = snapshot.value as ArrayList<HashMap<String, Any>>?
                for (i: Int in 1 until (values?.size ?: 0)) {
                    val data = values?.get(i)
                    feedList.add(
                        0, Feed(
                            data?.get("userId") as String,
                            data.get("imageUrl") as String,
                            data.get("profileImageUrl") as String,
                            data.get("likeCount") as Long,
                            false,
                            false,
                            data.get("content") as String
                        )
                    )
                }
                binding.homeRvFeed.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}