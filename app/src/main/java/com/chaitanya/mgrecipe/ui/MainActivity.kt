package com.chaitanya.mgrecipe.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.chaitanya.mgrecipe.R
import com.chaitanya.mgrecipe.databinding.ActivityMainBinding
import com.chaitanya.mgrecipe.utility.gone
import com.chaitanya.mgrecipe.utility.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.homeMenu -> navController.navigate(R.id.homeFragment)
                R.id.favouriteMenu -> navController.navigate(R.id.favouriteFragment)
            }
            true
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.searchFragment || destination.id == R.id.recipeDetailFragment) {
                binding.bottomNavigation.gone()
            } else {
                binding.bottomNavigation.visible()
            }
            when(destination.id){
                R.id.homeFragment -> binding.bottomNavigation.menu.findItem(R.id.homeMenu)?.isChecked = true
                R.id.favouriteFragment -> binding.bottomNavigation.menu.findItem(R.id.favouriteMenu)?.isChecked = true
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}