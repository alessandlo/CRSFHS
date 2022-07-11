package com.example.crsfhs.android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.crsfhs.android.activities.MainActivity
import com.example.crsfhs.android.activities.loggedInUserKey
import com.example.crsfhs.android.activities.loggedInUserRole
import com.example.crsfhs.android.activities.userLoggedIn

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HairsalonAbmeldenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HairsalonAbmeldenFragment : Fragment() {
    private lateinit var mainPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainPref = (activity as MainActivity).getSharedPreferences("PREFERENCE",
            Context.MODE_PRIVATE
        )


        if (userLoggedIn) {
            // set global var
            loggedInUserKey = "empty"
            userLoggedIn = false
            loggedInUserRole = "empty"

            // set shared Pref
            mainPref.edit().putBoolean("userLoggedIn", false
            ).apply()
            mainPref.edit().putString("loggedInUserKey", "empty").apply()
            mainPref.edit().putString("loggedInUserRole", "empty").apply()


            val handler: Handler = Handler()
            handler.postDelayed({
                (activity as MainActivity).finish()
                startActivity(Intent(activity as MainActivity, MainActivity::class.java))
                // findNavController().navigate(R.id.action_global_fragment_login)
            }, 999)
        }



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_abmelden, container, false)
    }

}
