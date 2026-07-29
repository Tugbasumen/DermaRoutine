package com.dermaroutine.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dermaroutine.presentation.assistant.AssistantScreen
import com.dermaroutine.presentation.auth.LoginScreen
import com.dermaroutine.presentation.auth.RegisterScreen
import com.dermaroutine.presentation.home.HomeScreen
import com.dermaroutine.presentation.journal.JournalScreen
import com.dermaroutine.presentation.onboarding.OnboardingScreen
import com.dermaroutine.presentation.products.ProductsScreen
import com.dermaroutine.presentation.profile.ProfileScreen
import com.dermaroutine.presentation.progress.ProgressScreen
import com.dermaroutine.presentation.routine.CreateRoutineScreen
import com.dermaroutine.presentation.routine.RoutineDetailScreen
import com.dermaroutine.presentation.routine.RoutineListScreen

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
                    navController.navigate(Screen.Home) {
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
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Register) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToRoutine = { navController.navigate(Screen.Routine) },
                onNavigateToProducts = { navController.navigate(Screen.Products) },
                onNavigateToJournal = { navController.navigate(Screen.Journal) },
                onNavigateToAssistant = { navController.navigate(Screen.Assistant) }
            )
        }

        composable<Screen.Routine> {
            RoutineListScreen(
                onNavigateToCreate = { navController.navigate(Screen.CreateRoutine) },
                onNavigateToDetail = { id -> navController.navigate(Screen.RoutineDetail(id)) }
            )
        }

        composable<Screen.RoutineDetail> { backStackEntry ->
            val detail: Screen.RoutineDetail = backStackEntry.toRoute()
            RoutineDetailScreen(
                routineId = detail.id,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Screen.CreateRoutine> {
            CreateRoutineScreen(
                onNavigateBack = { navController.popBackStack() },
                onRoutineCreated = { navController.popBackStack() }
            )
        }

        composable<Screen.Products> {
            ProductsScreen()
        }

        composable<Screen.Progress> {
            ProgressScreen()
        }
        
        composable<Screen.Profile> {
            ProfileScreen()
        }

        composable<Screen.Journal> {
            JournalScreen()
        }

        composable<Screen.Assistant> {
            AssistantScreen()
        }
    }
}
