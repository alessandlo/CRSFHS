package com.example.crsfhs.android.activities

import android.location.Geocoder
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
        /**
        val t1: Array<String> = Array(15){i -> "Test $i"}
        val t2: Array<String> = Array(15){i -> "Abc $i"}
        val s: ArrayList<String> = t1.plus(t2).toCollection(ArrayList<String>())

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CustomAdapter(s)

        recyclerView.adapter = adapter

        val searchbar = findViewById<EditText>(R.id.searchbar_text)
        searchbar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(p0)
            }
        })
        **/
    }
}
