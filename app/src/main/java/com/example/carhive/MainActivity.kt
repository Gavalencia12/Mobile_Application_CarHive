package com.example.carhive

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.carhive.Presentation.AuthViewModel
import com.example.carhive.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ocultar la ActionBar
        supportActionBar?.hide()

        // Configurar el NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Configurar el BottomNavigationView con el NavController
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Verificar la autenticación en el inicio
        checkAuthentication()

        // Configura el BottomNavigationView para navegar entre fragmentos
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.userHomeFragment)
                    true
                }
                R.id.profile -> {
                    navController.navigate(R.id.userProfileFragment)
                    true
                }
                else -> false
            }
        }


        // Controlar la visibilidad del BottomNavigationView
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.userHomeFragment, R.id.userProfileFragment -> showBottomNavigation()
                else -> hideBottomNavigation()
            }
        }
    }

    private fun checkAuthentication() {
        lifecycleScope.launch {
            // Observar el estado de autenticación
            authViewModel.isAuthenticated.collect { isAuthenticated ->
                if (isAuthenticated) {
                    // Si está autenticado, redirigir según el rol
                    authViewModel.userRole.collect { role ->
                        when (role) {
                            0 -> navController.navigate(R.id.action_loginFragment_to_adminFragment)
                            1 -> navController.navigate(R.id.action_loginFragment_to_sellerFragment)
                            2 -> navController.navigate(R.id.action_loginFragment_to_userhomeFragment)
                            else -> navController.navigate(R.id.action_loginFragment_to_loginFragment)
                        }
                    }
                } else {
                    // Si no está autenticado, ir al login
                    navController.navigate(R.id.action_loginFragment_to_loginFragment)
                }
            }
        }
    }

    private fun showBottomNavigation() {
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        bottomNavigationView.visibility = View.GONE
    }
}
