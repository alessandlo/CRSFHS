package com.example.crsfhs.android

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.crsfhs.android.activities.BASE_URL
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentRegistrierenBinding
import com.example.crsfhs.android.services.Encryption.toSHA
import com.example.crsfhs.android.services.Validation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistrierenFragment : Fragment() {
    private lateinit var binding: FragmentRegistrierenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrierenBinding.inflate(inflater, container, false)

        setupListeners()

        binding.registerButton.setOnClickListener {
            if (isValidate()) {
                checkUser()
            }
        }

        return binding.root
    }

    private fun isValidate(): Boolean =
        validateUsername() && validateFirstname() && validateLastname() &&
                validateEmail() && validatePassword()

    private fun setupListeners() {
        binding.usernameEditText.addTextChangedListener(TextFieldValidation(binding.usernameEditText))
        binding.firstnameEditText.addTextChangedListener(TextFieldValidation(binding.firstnameEditText))
        binding.lastnameEditText.addTextChangedListener(TextFieldValidation(binding.lastnameEditText))
        binding.emailEditText.addTextChangedListener(TextFieldValidation(binding.emailEditText))
        binding.passwordEditText.addTextChangedListener(TextFieldValidation(binding.passwordEditText))
    }

    /**
     * Eingabe darf nicht leer sein
     */
    private fun validateUsername(): Boolean {
        if (binding.usernameEditText.text.toString().trim().isEmpty()) {
            binding.usernameContainer.error = getString(R.string.requiredInput)
            binding.usernameEditText.requestFocus()
            return false
        } else {
            binding.usernameContainer.isErrorEnabled = false
        }
        return true
    }

    /**
     * 1) Eingabe darf nicht leer sein
     * 2) Eingabe muss aus Buchstaben bestehen
     */
    private fun validateFirstname(): Boolean {
        if (binding.firstnameEditText.text.toString().trim().isEmpty()) {
            binding.firstnameContainer.error = getString(R.string.requiredInput)
            binding.firstnameEditText.requestFocus()
            return false
        } else if (!Validation.isValidName(binding.firstnameEditText.text.toString())) {
            binding.firstnameContainer.error = getString(R.string.invalidFirstname)
            binding.firstnameEditText.requestFocus()
            return false
        } else {
            binding.firstnameContainer.isErrorEnabled = false
        }
        return true
    }

    /**
     * 1) Eingabe darf nicht leer sein
     * 2) Eingabe muss aus Buchstaben bestehen
     */
    private fun validateLastname(): Boolean {
        if (binding.lastnameEditText.text.toString().trim().isEmpty()) {
            binding.lastnameContainer.error = getString(R.string.requiredInput)
            binding.lastnameEditText.requestFocus()
            return false
        } else if (!Validation.isValidName(binding.lastnameEditText.text.toString())) {
            binding.lastnameContainer.error = getString(R.string.invalidLastname)
            binding.lastnameEditText.requestFocus()
            return false
        } else {
            binding.lastnameContainer.isErrorEnabled = false
        }
        return true
    }

    /**
     * 1) Eingabe darf nicht leer sein
     * 2) Eingabe muss in E-Mail Format sein
     */
    private fun validateEmail(): Boolean {
        if (binding.emailEditText.text.toString().trim().isEmpty()) {
            binding.emailContainer.error = getString(R.string.requiredInput)
            binding.emailEditText.requestFocus()
            return false
        } else if (!Validation.isValidEmail(binding.emailEditText.text.toString())) {
            binding.emailContainer.error = getString(R.string.invalidEmail)
            binding.emailEditText.requestFocus()
            return false
        } else {
            binding.emailContainer.isErrorEnabled = false
        }
        return true
    }

    /**
     * 1) Eingabe darf nicht leer sein
     * 2) Passwordlänge muss mindestens 8 sein
     * 3) Passwort muss aus mindestens einer Zahl bestehen
     * 4) Passwort muss aus mindestens einen Groß- und Kleinbuchstaben bestehen
     * 5) Passwort muss aus mindestens einem Sonderzeichen bestehen
     */
    private fun validatePassword(): Boolean {
        if (binding.passwordEditText.text.toString().trim().isEmpty()) {
            binding.passwordContainer.error = getString(R.string.requiredInput)
            binding.passwordEditText.requestFocus()
            return false
        } else if (binding.passwordEditText.text.toString().length < 8) {
            binding.passwordContainer.error = getString(R.string.passwordMin8Char)
            binding.passwordEditText.requestFocus()
            return false
        } else if (!Validation.isStringContainNumber(binding.passwordEditText.text.toString())) {
            binding.passwordContainer.error = getString(R.string.passwordMin1Number)
            binding.passwordEditText.requestFocus()
            return false
        } else if (!Validation.isStringLowerAndUpperCase(binding.passwordEditText.text.toString())) {
            binding.passwordContainer.error = getString(R.string.passwordMin1UpperLower)
            binding.passwordEditText.requestFocus()
            return false
        } else if (!Validation.isStringContainSpecialCharacter(binding.passwordEditText.text.toString())) {
            binding.passwordContainer.error = getString(R.string.passwordMin1Special)
            binding.passwordEditText.requestFocus()
            return false
        } else {
            binding.passwordContainer.isErrorEnabled = false
        }
        return true
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.usernameEditText -> {
                    validateUsername()
                }
                R.id.firstnameEditText -> {
                    validateFirstname()
                }
                R.id.lastnameEditText -> {
                    validateLastname()
                }
                R.id.emailEditText -> {
                    validateEmail()
                }
                R.id.passwordEditText -> {
                    validatePassword()
                }
            }
        }
    }

    private fun checkUser() {
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
                Log.i("Info", "${response.body()!!.paging.size}, $userInDatabase")
                if (userInDatabase) {
                    binding.usernameContainer.error = getString(R.string.userAlreadyExists)
                    binding.usernameEditText.requestFocus()
                } else {
                    register()
                }
            }

            override fun onFailure(call: Call<UserList?>, t: Throwable) {
                Log.e("CheckUser", "onFailure: " + t.message)
            }
        })
    }

    private fun register() {
        val userItem = UserItem(
            UserDetails(
                key = null,
                username = binding.usernameEditText.text.toString(),
                gender = binding.genderSelection.text.toString(),
                firstname = binding.firstnameEditText.text.toString(),
                lastname = binding.lastnameEditText.text.toString(),
                email = binding.emailEditText.text.toString(),
                password = binding.passwordEditText.text.toString().toSHA()
            )
        )

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.storeUser(userItem)

        retrofitData.enqueue(object : Callback<UserItem?> {
            override fun onResponse(call: Call<UserItem?>, response: Response<UserItem?>) {
                Toast.makeText(activity, getString(R.string.accountCreated), Toast.LENGTH_SHORT)
                    .show()
                binding.registerButton.findNavController()
                    .navigate(R.id.action_fragment_registrieren_to_fragment_startseite)
                Log.i("Register", "Account created")
            }

            override fun onFailure(call: Call<UserItem?>, t: Throwable) {
                Log.e("Register", "onFailure: " + t.message)
            }
        })
    }

}
