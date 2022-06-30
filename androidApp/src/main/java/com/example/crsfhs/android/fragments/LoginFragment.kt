package com.example.crsfhs.android.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.activities.MainActivity
import com.example.crsfhs.android.activities.loggedInUserKey
import com.example.crsfhs.android.activities.userLoggedIn
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentLoginBinding
import com.example.crsfhs.android.services.Encryption.toSHA
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var mainPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        mainPref = (activity as MainActivity).getSharedPreferences("PREFERENCE", MODE_PRIVATE)



        setupListeners()

        binding.guestButton.setOnClickListener {
            binding.guestButton.findNavController()
                .navigate(R.id.action_fragment_login_to_fragment_startseite)
        }

        binding.registerButton.setOnClickListener {
            binding.loginButton.findNavController()
                .navigate(R.id.action_fragment_login_to_fragment_registrieren)
        }

        binding.loginButton.setOnClickListener {
            login()
        }

        return binding.root
    }

    private fun setupListeners() {
        binding.usernameEditText.addTextChangedListener(TextFieldValidation(binding.usernameEditText))
        binding.passwordEditText.addTextChangedListener(TextFieldValidation(binding.passwordEditText))
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.usernameEditText -> {
                    if (binding.usernameEditText.text.toString().trim().isEmpty()) {
                        binding.usernameContainer.error = getString(R.string.requiredInput)
                        binding.usernameEditText.requestFocus()
                    } else {
                        binding.usernameContainer.isErrorEnabled = false
                    }
                }
                R.id.passwordEditText -> {
                    if (binding.passwordEditText.text.toString().trim().isEmpty()) {
                        binding.passwordContainer.error = getString(R.string.requiredInput)
                        binding.passwordEditText.requestFocus()
                    } else {
                        binding.passwordContainer.isErrorEnabled = false
                    }
                }
            }
        }
    }

    private fun login() {
        val userCheck = UserCheck(
            query =
            listOf(UserQuery(binding.usernameEditText.text.toString()))
        )

        val retrofitData = DbApi.retrofitService.checkUser(userCheck)

        retrofitData.enqueue(object : Callback<UserList?> {
            override fun onResponse(call: Call<UserList?>, response: Response<UserList?>) {
                val userInDatabase = response.body()!!.paging.size != 0

                if (!userInDatabase) {
                    binding.usernameContainer.error = getString(R.string.userDoesntExist)
                    binding.usernameEditText.requestFocus()
                } else {
                    val passwordCheck = response.body()!!.items[0].password
                    if (binding.passwordEditText.text.toString().toSHA() != passwordCheck) {
                        binding.passwordContainer.error = getString(R.string.wrongPassword)
                        binding.passwordEditText.requestFocus()
                        Log.i("Login", "falsches Passwort")
                    } else {

                        // SHARED PREF.
                        mainPref.edit().putBoolean("userLoggedIn", true).apply()
                        userLoggedIn = true

                        loggedInUserKey = response.body()!!.items[0].key
                        mainPref.edit().putString("loggedInUserKey", loggedInUserKey).apply()

                        binding.loginButton.findNavController()
                            .navigate(R.id.action_fragment_login_to_fragment_startseite)
                        Log.i("Login", "eingeloggt als $loggedInUserKey")
                    }
                }
            }

            override fun onFailure(call: Call<UserList?>, t: Throwable) {
                Log.e("Login", "onFailure: " + t.message)
            }
        })
    }

}
