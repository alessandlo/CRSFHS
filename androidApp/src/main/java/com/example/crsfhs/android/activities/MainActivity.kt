package com.example.crsfhs.android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import com.example.crsfhs.android.R
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.ActivityMainBinding
import com.example.crsfhs.android.services.Encryption.toSHA
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://database.deta.sh/v1/a0a1f9b4/"
var loggedInUser: String? = null

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        binding.guestButton.setOnClickListener {
            val intent = Intent(this, Start::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            login()
        }
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
                    }
                    else {
                        binding.usernameContainer.isErrorEnabled = false
                    }
                }
                R.id.passwordEditText -> {
                    if (binding.passwordEditText.text.toString().trim().isEmpty()) {
                        binding.passwordContainer.error = getString(R.string.requiredInput)
                        binding.passwordEditText.requestFocus()
                    }
                    else {
                        binding.passwordContainer.isErrorEnabled = false
                    }
                }
            }
        }
    }

    private fun login() {
        val userCheck = UserCheck(
            query =
            listOf(UserQuery(findViewById<EditText>(R.id.usernameEditText).text.toString()))
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
                    if (binding.passwordEditText.text.toString().toSHA() != passwordCheck){
                        binding.passwordContainer.error = getString(R.string.wrongPassword)
                        binding.passwordEditText.requestFocus()
                        Log.i("Login", "falsches Passwort")
                    }
                    else{
                        loggedInUser = response.body()!!.items[0].key
                        val intent = Intent(this@MainActivity, Start::class.java)
                        startActivity(intent)
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
