package com.dermaroutine.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dermaroutine.presentation.auth.LoginScreen
import com.dermaroutine.presentation.auth.RegisterScreen
import com.dermaroutine.presentation.home.HomeScreen
import com.dermaroutine.presentation.onboarding.OnboardingScreen
import com.dermaroutine.presentation.profile.ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Screen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Onboarding> {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Onboarding) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Profile) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register)
                }
            )
        }

        composable<Screen.Register> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Profile) {
                        popUpTo(Screen.Register) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        
        composable<Screen.Home> {
            HomeScreen()
        }
        
        composable<Screen.Profile> {
            ProfileScreen()
        }
    }
}
