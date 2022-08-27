package com.example.hansimcat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hansimcat.databinding.FragmentHeartBinding
import com.example.hansimcat.databinding.FragmentMainBinding

class HeartFragment : Fragment() {
    private var _binding: FragmentHeartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHeartBinding.inflate(inflater, container, false)
        return binding.root
    }
}