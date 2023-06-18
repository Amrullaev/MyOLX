package com.amrullaev.myolx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.amrullaev.myolx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.profileFragment, R.id.messageFragment, R.id.saveFragment, R.id.addProductFragment -> {
                    binding.bottomNav.visibility= View.VISIBLE
                }
                else -> binding.bottomNav.visibility= View.GONE
            }
        }
    }

    fun hideBottomNawView() {
        binding.bottomNav.visibility = View.GONE
    }

    fun showBottomNawView() {
        binding.bottomNav.visibility = View.VISIBLE
    }
}