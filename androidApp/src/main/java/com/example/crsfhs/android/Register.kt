package com.example.crsfhs.android

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crsfhs.android.Validation.isStringContainNumber
import com.example.crsfhs.android.Validation.isStringContainSpecialCharacter
import com.example.crsfhs.android.Validation.isStringLowerAndUpperCase
import com.example.crsfhs.android.Validation.isValidEmail
import com.example.crsfhs.android.Validation.isValidName
import com.example.crsfhs.android.api.ApiInterface
import com.example.crsfhs.android.api.UserDetails
import com.example.crsfhs.android.api.UserItem
import com.example.crsfhs.android.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        binding.registerButton.setOnClickListener {
            if (isValidate()) {
                Toast.makeText(this, "Account erstellt", Toast.LENGTH_SHORT).show()
                register()
            }
        }
    }

    private fun isValidate(): Boolean =
        validateUsername() && validateFirstname() && validateLastname() && validateEmail() && validatePassword()

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
            binding.usernameContainer.error = "Erforderliche Eingabe!"
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
            binding.firstnameContainer.error = "Erforderliche Eingabe!"
            binding.firstnameEditText.requestFocus()
            return false
        } else if (!isValidName(binding.firstnameEditText.text.toString())) {
            binding.firstnameContainer.error = "Ungültiger Vorname!"
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
            binding.lastnameContainer.error = "Erforderliche Eingabe!"
            binding.lastnameEditText.requestFocus()
            return false
        } else if (!isValidName(binding.lastnameEditText.text.toString())) {
            binding.lastnameContainer.error = "Ungültiger Nachname!"
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
            binding.emailContainer.error = "Erforderliche Eingabe!"
            binding.emailEditText.requestFocus()
            return false
        } else if (!isValidEmail(binding.emailEditText.text.toString())) {
            binding.emailContainer.error = "Ungültige E-Mail!"
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
            binding.passwordContainer.error = "Erforderliche Eingabe!"
            binding.passwordEditText.requestFocus()
            return false
        } else if (binding.passwordEditText.text.toString().length < 8) {
            binding.passwordContainer.error = "Mindestens 8 Zeichen benötigt"
            binding.passwordEditText.requestFocus()
            return false
        } else if (!isStringContainNumber(binding.passwordEditText.text.toString())) {
            binding.passwordContainer.error = "Mindestens eine Zahl benötigt"
            binding.passwordEditText.requestFocus()
            return false
        } else if (!isStringLowerAndUpperCase(binding.passwordEditText.text.toString())) {
            binding.passwordContainer.error = "Mindestens einen Groß- und Kleinbuchstaben benötigt"
            binding.passwordEditText.requestFocus()
            return false
        } else if (!isStringContainSpecialCharacter(binding.passwordEditText.text.toString())) {
            binding.passwordContainer.error = "Mindestens ein Sonderzeichen benötigt"
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

    private fun register() {
        val userItem = UserItem(
            UserDetails(
                key = null,
                username = findViewById<EditText>(R.id.usernameEditText).text.toString(),
                firstname = findViewById<EditText>(R.id.firstnameEditText).text.toString(),
                lastname = findViewById<EditText>(R.id.lastnameEditText).text.toString(),
                email = findViewById<EditText>(R.id.emailEditText).text.toString(),
                password = findViewById<EditText>(R.id.passwordEditText).text.toString()
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
                if (response.code() == 400) {
                    Log.i("Error", "Doesn't work")
                }
                Log.i("Info", "$response")
            }

            override fun onFailure(call: Call<UserItem?>, t: Throwable) {
                Log.e("Register", "onFailure: " + t.message)
            }
        })
    }
}