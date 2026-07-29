package com.dermaroutine.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.dermaroutine.presentation.products.*
import com.dermaroutine.presentation.profile.ProfileScreen
import com.dermaroutine.presentation.progress.ProgressScreen
import com.dermaroutine.presentation.routine.CreateRoutineScreen
import com.dermaroutine.presentation.routine.RoutineDetailScreen
import com.dermaroutine.presentation.routine.RoutineListScreen
import com.dermaroutine.presentation.scanner.BarcodeScannerScreen

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
            val selectedProductId by backStackEntry.savedStateHandle.getStateFlow<Int?>("selected_product_id", null).collectAsState()
            
            RoutineDetailScreen(
                routineId = detail.id,
                selectedProductId = selectedProductId,
                onProductHandled = { backStackEntry.savedStateHandle.set("selected_product_id", null) },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddProduct = { navController.navigate(Screen.ProductSelector(detail.id)) }
            )
        }

        composable<Screen.CreateRoutine> {
            CreateRoutineScreen(
                onNavigateBack = { navController.popBackStack() },
                onRoutineCreated = { navController.popBackStack() }
            )
        }

        composable<Screen.Products> {
            ProductListScreen(
                onNavigateToAdd = { navController.navigate(Screen.AddProduct) },
                onNavigateToDetail = { id -> navController.navigate(Screen.ProductDetail(id)) }
            )
        }

        composable<Screen.AddProduct> { backStackEntry ->
            val scannedBarcode by backStackEntry.savedStateHandle.getStateFlow<String?>("scanned_barcode", null).collectAsState()
            AddEditProductScreen(
                scannedBarcode = scannedBarcode,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToScanner = { navController.navigate(Screen.BarcodeScanner) },
                onNavigateToProductDetail = { id -> navController.navigate(Screen.ProductDetail(id)) },
                onProductSaved = { navController.popBackStack() }
            )
        }

        composable<Screen.EditProduct> { backStackEntry ->
            val edit: Screen.EditProduct = backStackEntry.toRoute()
            AddEditProductScreen(
                productId = edit.id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToScanner = { navController.navigate(Screen.BarcodeScanner) },
                onNavigateToProductDetail = { id -> navController.navigate(Screen.ProductDetail(id)) },
                onProductSaved = { navController.popBackStack() }
            )
        }

        composable<Screen.ProductDetail> { backStackEntry ->
            val detail: Screen.ProductDetail = backStackEntry.toRoute()
            ProductDetailScreen(
                productId = detail.id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id -> navController.navigate(Screen.EditProduct(id)) }
            )
        }

        composable<Screen.ProductSelector> {
            ProductSelectorScreen(
                onProductSelected = { product ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_product_id", product.id)
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Screen.BarcodeScanner> {
            BarcodeScannerScreen(
                onBarcodeDetected = { barcode ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("scanned_barcode", barcode)
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
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
