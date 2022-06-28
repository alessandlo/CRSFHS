package com.example.crsfhs.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.databinding.FragmentPreWieFunktioniertDasBinding

class preWieFunktioniertDasFragment : Fragment() {
    private lateinit var binding: FragmentPreWieFunktioniertDasBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPreWieFunktioniertDasBinding.inflate(inflater, container, false)

        binding.continueButton.setOnClickListener {
            binding.continueButton.findNavController()
                .navigate(R.id.preDatenschutzFragment)
        }

        return binding.root
    }
}
