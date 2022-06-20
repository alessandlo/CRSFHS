package com.example.crsfhs.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.crsfhs.android.databinding.FragmentMeineReservierungenBinding

class MeineReservierungenFragment : Fragment() {
    private lateinit var binding: FragmentMeineReservierungenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMeineReservierungenBinding.inflate(inflater, container, false)

        return binding.root
    }
}