package com.snechaev1.myroutes

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.snechaev1.myroutes.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: MainActivityBinding

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val appBarConfiguration by lazy { AppBarConfiguration(setOf(
        R.id.nav_map, R.id.nav_profile
    )) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        val toolbarView: Toolbar = findViewById(R.id.toolbar)

        toolbarView.setupWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { navCtrl, destination, arguments ->
            if(arguments != null) {
                Timber.d("DestinationChangedListener argument: ${arguments.getBoolean("ShowAppBar", false)}");
//                appBar.isVisible = arguments?.getBoolean("ShowAppBar", false) == true
            }
            when(destination.id) {
                R.id.nav_map -> {
                    Timber.d("destination.id: R.id.nav_map")
                    binding.bottomNavView.visibility = View.VISIBLE
//                    navCtrl.popBackStack(R.id.nav_map, true)
                }
                R.id.nav_profile -> {
                    Timber.d("destination.id: R.id.nav_profile, R.id.nav_requests")
                    binding.bottomNavView.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavView.visibility = View.GONE
                }
            }

            binding.toolbar.navigationIcon?.apply {
                colorFilter = PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN)
            }
            binding.toolbar.setTitleTextColor(Color.DKGRAY)
        }

//        GoogleApiAvailability.makeGooglePlayServicesAvailable()

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras.
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Timber.d("Key: $key Value: $value")
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }


    fun settings(item: MenuItem) {
        lifecycleScope.launch {
//            val navController = findNavController(R.id.nav_host_fragment)
//            navController.navigate(R.id.action_global_settings)
        }
    }

//    fun showProgress(show: Boolean) {
//        if (show)
//            binding.progressCircular.visibility = View.VISIBLE
//        else
//            binding.progressCircular.visibility = View.GONE
//    }

}