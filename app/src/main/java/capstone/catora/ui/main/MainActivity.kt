package capstone.catora.ui.main

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import capstone.catora.R
import capstone.catora.data.pref.UserPreferences
import capstone.catora.data.pref.dataStore
import capstone.catora.databinding.ActivityMainBinding
import capstone.catora.ui.main.profile.ProfileFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

//        val pref = UserPreferences.getInstance(this.dataStore)
//        val user = runBlocking { pref.getSession().first() }
//
//
//        val userId = user.userId
//        val profileFragment = ProfileFragment.newInstance(userId)
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.nav_host_fragment_activity_main, profileFragment)
////            .addToBackStack(null) // Optional, to add the transaction to the back stack
//            .commit()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun navigateToProfileFragment(){
        findNavController(R.id.nav_host_fragment_activity_main).popBackStack()
        findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_notifications)
    }
}