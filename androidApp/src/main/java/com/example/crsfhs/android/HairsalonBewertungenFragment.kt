package com.example.crsfhs.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.crsfhs.android.databinding.FragmentHairsalonBewertungenBinding

class HairsalonBewertungenFragment : Fragment() {
    private lateinit var binding: FragmentHairsalonBewertungenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHairsalonBewertungenBinding.inflate(inflater, container, false)
        return binding.root
    }
}
