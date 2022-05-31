package com.example.crsfhs.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.crsfhs.android.api.ApiInterface
import com.example.crsfhs.android.api.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        retrofitData.enqueue(object : Callback<List<UserItem>?> {
            override fun onResponse(
                call: Call<List<UserItem>?>,
                response: Response<List<UserItem>?>
            ) {
                val responseBody = response.body()!!
                val stringBuilder = StringBuilder()

                for (User in responseBody) {
                    stringBuilder.append(User.username)
                    stringBuilder.append("\n")
                    if (User.username == "test") {
                        stringBuilder.append("User is in Database\n")
                    }
                }

                val data = findViewById<TextView>(R.id.dataView)
                data.text = stringBuilder
            }

            override fun onFailure(call: Call<List<UserItem>?>, t: Throwable) {
                Log.e("Test", "onFailure: " + t.message)
            }
        })
    }
}
