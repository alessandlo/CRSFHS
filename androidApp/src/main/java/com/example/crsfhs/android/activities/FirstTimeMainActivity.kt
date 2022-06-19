package com.example.crsfhs.android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.crsfhs.android.R

class FirstTimeMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_pre_willkommensseite)
        setContentView(R.layout.fragment_pre_wie_funktioniert_das)
        //setContentView(R.layout.fragment_pre_datenschutz)
    }
}