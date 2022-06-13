package com.example.crsfhs.android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crsfhs.android.R

class Start : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val searchbar = findViewById<EditText>(R.id.searchbar_text)
        searchbar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(p0: Editable?) {
                TODO("Not yet implemented")
            }

        })
        val s: Array<String> = Array(15){i -> "Test $i"}

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CustomAdapter(s)
        recyclerView.adapter = adapter

    }
}
