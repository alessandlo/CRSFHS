package com.example.crsfhs.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.crsfhs.android.api.ApiInterface
import com.example.crsfhs.android.api.UserDetails
import com.example.crsfhs.android.api.UserList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://database.deta.sh/v1/a0a1f9b4/"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val guestClick = findViewById<Button>(R.id.guestButton)
        val registerClick = findViewById<Button>(R.id.registerButton)
        val loginClick = findViewById<Button>(R.id.loginButton)

        guestClick.setOnClickListener {
            val intent = Intent(this, Start::class.java)
            startActivity(intent)
        }

        registerClick.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        loginClick.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getUsers()

        retrofitData.enqueue(object : Callback<UserList?> {
            override fun onResponse(call: Call<UserList?>, response: Response<UserList?>) {
                Log.i("Info", "$response")
                val responseBody = response.body()!!.items
                val stringBuilder = StringBuilder()

                for (User in responseBody) {
                    stringBuilder.append(User.username)
                    stringBuilder.append("\n")
                    if (User.username == "test_username2") {
                        stringBuilder.append("User is in Database\n")
                    }
                }

                val data = findViewById<TextView>(R.id.dataView)
                data.text = stringBuilder
            }

            override fun onFailure(call: Call<UserList?>, t: Throwable) {
                Log.e("Login", "onFailure: " + t.message)
            }
        })

    }
}
