package com.example.crsfhs.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class preWillkommensseiteFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_pre_willkommensseite, container, false)

        var btn: Button = rootView.findViewById(R.id.willkommensseite_btn_wie_funktioniert_das)
        val navController: NavController = findNavController()
        btn.setOnClickListener {
            navController.navigate(R.id.action_willkommensseite_to_wie_funktioniert_das)
        }

        return rootView
    }

}