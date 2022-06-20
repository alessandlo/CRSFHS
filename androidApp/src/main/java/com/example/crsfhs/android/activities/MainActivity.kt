package com.example.crsfhs.android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.ActivityMainBinding
import com.example.crsfhs.android.services.Encryption.toSHA
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://database.deta.sh/v1/a0a1f9b4/"
var loggedInUser: String? = null

// Navigation Components:
private lateinit var drawerLayout: DrawerLayout
private lateinit var navView: NavigationView
private lateinit var navController: NavController

// AppBarConfiguration: Hier mehr Fragemente hinzufügen, wenn wir das Menü erweitern sollten
private val idSets = setOf(R.id.fragment_startseite, R.id.fragment_pers_daten, R.id.fragment_meine_reservierungen, R.id.fragment_meine_favoriten)
private lateinit var appBarConfig: AppBarConfiguration

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // FIRST TIME LOGIK START
        val isFirstRun: Boolean = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getBoolean("isFirstRun", true)

        if (isFirstRun) {
            //show sign up activity
            startActivity(Intent(this@MainActivity, FirstTimeMainActivity::class.java))
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
            .putBoolean("isFirstRun", false).apply()
        // FIRST TIME END

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navi Drawer
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host)
        appBarConfig = AppBarConfiguration(idSets,drawerLayout)
        setupActionBarWithNavController(navController, appBarConfig)
        navView.setupWithNavController(navController)
    }

    // Navi Drawer
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}
