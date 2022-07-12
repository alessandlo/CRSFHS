package com.example.crsfhs.android.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.activities.MainActivity
import com.example.crsfhs.android.activities.loggedInUserKey
import com.example.crsfhs.android.activities.loggedInUserRole
import com.example.crsfhs.android.activities.userLoggedIn

class AbmeldenFragment : Fragment() {
    private lateinit var mainPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainPref = (activity as MainActivity).getSharedPreferences(
            "PREFERENCE",
            Context.MODE_PRIVATE
        )

        if (userLoggedIn) {
            // set global var
            loggedInUserKey = "empty"
            userLoggedIn = false
            loggedInUserRole = "empty"

            // set shared Pref
            mainPref.edit().putBoolean(
                "userLoggedIn", false
            ).apply()
            mainPref.edit().putString("loggedInUserKey", "empty").apply()
            mainPref.edit().putString("loggedInUserRole", "empty").apply()

            val handler = Handler()
            handler.postDelayed({
                findNavController().navigate(R.id.action_global_fragment_login)
            }, 999)
        } else findNavController().navigate(R.id.action_global_fragment_login)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_abmelden, container, false)
    }
}
