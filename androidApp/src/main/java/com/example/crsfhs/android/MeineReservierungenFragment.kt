package com.example.crsfhs.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class MeineReservierungenFragment : Fragment() {
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_meine_reservierungen, container, false)

        var btn: Button = rootView.findViewById(R.id.meine_reservierungen_btn_1)
        val navController: NavController = findNavController()
        btn.setOnClickListener {
            navController.navigate(R.id.action_mr_to_mrv)
        }

        return rootView
    }
}