package com.example.crsfhs.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.databinding.FragmentPreWillkommensseiteBinding

class preWillkommensseiteFragment : Fragment() {
    private lateinit var binding: FragmentPreWillkommensseiteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPreWillkommensseiteBinding.inflate(inflater, container, false)

        binding.goButton.setOnClickListener {
            binding.goButton.findNavController()
                .navigate(R.id.action_willkommensseite_to_wie_funktioniert_das1)
        }

        return binding.root
    }
}
