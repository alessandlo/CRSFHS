package com.example.crsfhs.android.activities

import android.content.Context
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.fragments.preWillkommensseiteFragment
import com.google.android.material.navigation.NavigationView

var loggedInUserKey: String? = null
var userLoggedIn: Boolean = false

// Navigation Components:
private lateinit var drawerLayout: DrawerLayout
private lateinit var navView: NavigationView
lateinit var navController: NavController



// AppBarConfiguration: Hier mehr Fragemente hinzufügen, wenn wir das Menü erweitern sollten
private val idSets = setOf(
    R.id.fragment_startseite,
    R.id.fragment_pers_daten,
    R.id.fragment_meine_reservierungen,
    R.id.fragment_meine_favoriten
)

private val idSetsHairsalon = setOf(
    R.id.fragment_hairsalon_bevorstehende_reservierungen,
    R.id.fragment_hairsalon_profil_anpassen,
    R.id.fragment_hairsalon_bewertungen,
    R.id.fragment_hairsalon_res_verwalten,
    R.id.abmeldenFragment
)


private lateinit var appBarConfig: AppBarConfiguration

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val context: Context = applicationContext

            super.onCreate(savedInstanceState)

        // Customer
        setContentView(R.layout.activity_main)

        // Hairsalon
        //setContentView(R.layout.hairsalon_activity_main)

        // Navi Drawer
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Customer
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host)
        appBarConfig = AppBarConfiguration(idSets, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfig)
        navView.setupWithNavController(navController)

        // FIRST TIME LOGIK START
        val isFirstRun: Boolean = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getBoolean("isFirstRun", true)

        //var UserLoggedIn: Boolean = false;

            if (isFirstRun) {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("userLoggedIn", false)
            navController.navigate(R.id.action_fragment_startseite_to_fragment_pre_willkommensseite)
            //startActivity(Intent(this@MainActivity, FirstTimeMainActivity::class.java))

            }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
            .putBoolean("isFirstRun", false).apply()
        // FIRST TIME END

        // set global var
        loggedInUserKey = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getString("loggedInUserKey", "empty")
        userLoggedIn = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getBoolean("userLoggedIn", false)

        println("Folgender User ist eingeloggt: $loggedInUserKey")
        println("MainActivity: User ist eingeloggt $userLoggedIn")


/*      // Hairsalon
        drawerLayout = findViewById(R.id.hairsalon_drawer_layout)
        navView = findViewById(R.id.hairsalon_nav_view)
        navController = findNavController(R.id.hairsalon_nav_host)
        appBarConfig = AppBarConfiguration(idSetsHairsalon, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfig)
        navView.setupWithNavController(navController)*/
    }

    // Customer Navi Drawer
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

/*    // Hairsalon Navi Drawer
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.hairsalon_nav_host)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }*/
}
