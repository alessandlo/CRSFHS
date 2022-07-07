package com.example.crsfhs.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.databinding.FragmentTerminDetailsBinding
import com.example.crsfhs.android.databinding.FragmentZusammenfassungReservierungBinding


class ZusammenfassungReservierungFragment : Fragment() {
    private var _binding: FragmentZusammenfassungReservierungBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_zusammenfassung_reservierung, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentZusammenfassungReservierungBinding.bind(view)

        binding.apply {
            textView2.text = requireArguments().getString("date").toString()
            textView3.text = requireArguments().getString("time").toString()
            textView4.text = requireArguments().getString("comment").toString()

            button.setOnClickListener {         //jump to next fragment and transfer appointment details
                findNavController().navigate(R.id.action_zusammenfassung_to_wurde_reserviert)
        }

    }
}
}
