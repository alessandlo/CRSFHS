package com.example.crsfhs.android

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.crsfhs.android.activities.BASE_URL
import com.example.crsfhs.android.activities.loggedInUser
import com.example.crsfhs.android.api.ApiInterface
import com.example.crsfhs.android.api.UserCheck
import com.example.crsfhs.android.api.UserList
import com.example.crsfhs.android.api.UserQuery
import com.example.crsfhs.android.databinding.FragmentLoginBinding
import com.example.crsfhs.android.services.Encryption.toSHA
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

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

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.checkUser(userCheck)

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
                        loggedInUser = response.body()!!.items[0].key
                        binding.loginButton.findNavController()
                            .navigate(R.id.action_fragment_pre_datenschutz_to_fragment_login)
                        Log.i("Login", "eingeloggt")
                    }
                }
            }

            override fun onFailure(call: Call<UserList?>, t: Throwable) {
                Log.e("Login", "onFailure: " + t.message)
            }
        })
    }

}
