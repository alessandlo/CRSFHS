package com.example.crsfhs.android.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.databinding.FragmentPreDatenschutzBinding

class preDatenschutzFragment : Fragment() {
    private lateinit var binding: FragmentPreDatenschutzBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPreDatenschutzBinding.inflate(inflater, container, false)

        binding.acceptButton.setOnClickListener {
            binding.acceptButton.findNavController()
                .navigate(R.id.action_fragment_pre_datenschutz_to_fragment_login)
        }

        binding.showDatapolicyButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://reshairvation.sunnyshaiba.de/")
            startActivity(intent)
            /*binding.showDatapolicyButton.findNavController()
                .navigate(R.id.action_fragment_pre_datenschutz_to_fragment_datenschutz_anzeigen)*/
        }

        return binding.root
    }
}
