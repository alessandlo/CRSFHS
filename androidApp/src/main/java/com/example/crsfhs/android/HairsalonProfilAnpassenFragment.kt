package com.example.crsfhs.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.crsfhs.android.databinding.FragmentHairsalonProfilAnpassenBinding

class HairsalonProfilAnpassenFragment : Fragment() {
    private lateinit var binding: FragmentHairsalonProfilAnpassenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHairsalonProfilAnpassenBinding.inflate(inflater, container, false)
        return binding.root
    }
}
