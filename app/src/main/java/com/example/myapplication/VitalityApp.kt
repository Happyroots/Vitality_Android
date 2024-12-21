package com.example.myapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class CupcakeScreen() {
    Start,
    Settings,
}

@Composable
fun VitalityApp(){
    Scaffold(modifier = Modifier.fillMaxSize(),

        ) { innerPadding ->

        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = CupcakeScreen.Start.name) {
                OneKmThreeKmFiveKmsSyScreen(
                    modifier = Modifier.padding(innerPadding),
                    onNextButtonClicked = {
//                                    viewModel.setQuantity(it)
                        navController.navigate(CupcakeScreen.Settings.name)
                    },
                )
            }

            composable(route = CupcakeScreen.Settings.name) {
                val context = LocalContext.current
                SettingsScreen(modifier = Modifier.padding(innerPadding))
            }

        }
    }
}