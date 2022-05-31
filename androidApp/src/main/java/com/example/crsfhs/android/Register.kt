package com.example.crsfhs.android

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.crsfhs.android.api.ApiInterface
import com.example.crsfhs.android.api.UserItem
import com.example.crsfhs.android.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://friseurdb-ff86.restdb.io/rest/"

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usernameFocusListener()
        nameFocusListener()
        emailFocusListener()
        passwordFocusListener()
        //setContentView(R.layout.activity_register)

        val registerClick = findViewById<Button>(R.id.registerButton)

        registerClick.setOnClickListener {
            register()
        }
    }

    private fun usernameFocusListener() {
        binding.usernameEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.usernameContainer.error = validUsername()
            }
        }
    }

    private fun validUsername(): String? {
        val usernameText = binding.usernameEditText.text.toString()
        if (!usernameText.matches("[a-zA-Z0-9._]*".toRegex())) {
            return "Benutzername ungültig. Erlaubte Zeichen (a-z,A-Z,0-9,.,_)"
        }
        return null
    }

    private fun nameFocusListener() {
        binding.firstnameEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.firstnameContainer.error = validName(0)
            }
        }
        binding.lastnameEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.lastnameContainer.error = validName(1)
            }
        }
    }

    private fun validName(int: Int): String? {
        if (int == 0) {
            val firstnameText = binding.firstnameEditText.text.toString()
            if (!firstnameText.matches("[a-zA-Z]*".toRegex())) {
                return "Vorname ungültig"
            }
        } else {
            val lastnameText = binding.lastnameEditText.text.toString()
            if (!lastnameText.matches("[a-zA-Z]*".toRegex())) {
                return "Nachname ungültig"
            }
        }
        return null
    }

    private fun emailFocusListener() {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.emailContainer.error = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.emailEditText.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "E-Mail ungültig"
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.passwordEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.passwordContainer.error = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordEditText.text.toString()
        if (passwordText.length < 8) {
            return "Mindestens 8 Zeichen"
        }
        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return "Mindestens 1 Großbuchstaben"
        }
        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return "Mindestens 1 Kleinbuchstaben"
        }
        if (!passwordText.matches(".*[0-9].*".toRegex())) {
            return "Mindestens 1 Zahl"
        }
        if (!passwordText.matches(".*[@#\$%^+=].*".toRegex())) {
            return "Mindestens 1 Sonderzeichen (@#\$%^^+=)"
        }
        return null
    }

    private fun register() {
        val userItem = UserItem(
            _id = null,
            u_id = null,
            username = findViewById<EditText>(R.id.usernameEditText).text.toString(),
            email = findViewById<EditText>(R.id.emailEditText).text.toString(),
            firstname = findViewById<EditText>(R.id.firstnameEditText).text.toString(),
            lastname = findViewById<EditText>(R.id.lastnameEditText).text.toString(),
            active = true
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
                Log.i("Successful", "$response")
            }

            override fun onFailure(call: Call<UserItem?>, t: Throwable) {
                Log.e("Register", "onFailure: " + t.message)
            }
        })
    }
}