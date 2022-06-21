package com.example.crsfhs.android.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.crsfhs.android.R
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.crsfhs.android.DatePickerFragment
import com.example.crsfhs.android.databinding.FragmentTerminDetailsBinding

class TerminDetailsFragment : Fragment(R.layout.fragment_termin_details) {
    private var _binding: FragmentTerminDetailsBinding? = null
    private val binding get() = _binding!!


//opens up the datepicker and works with the output
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    )
    {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTerminDetailsBinding.bind(view)

        binding.apply {
            tdbtn1.setOnClickListener {
                // create new instance of DatePickerFragment
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                // we have to implement setFragmentResultListener
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {

                        val date = bundle.getString("SELECTED_DATE")
                        tvDate.text = date
                    }
                }

                // show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
            // binding.tdbtn2.setOnClickListener {
            //}

        }
    }

//fills the dropdown menu with content
    override fun onResume() {
        super.onResume()
        val zeit = resources.getStringArray(R.array.zeit)

        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdownzeiten, zeit )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }
    override fun onCreateView(
        inflater:LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View{
        _binding = FragmentTerminDetailsBinding.inflate(inflater, container, false)

        return binding.root
        }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    /*private fun comment(){
        val comment = binding.Kommentarzeile.addTextChangedListener(
            Register.TextFieldValidation(
                binding.Kommentarzeile
            )
        )
        return comment
    }*/
}






