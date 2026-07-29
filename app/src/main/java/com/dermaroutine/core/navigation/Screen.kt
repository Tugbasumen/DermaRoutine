package com.dermaroutine.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Onboarding : Screen()
    
    @Serializable
    data object Login : Screen()
    
    @Serializable
    data object Register : Screen()
    
    @Serializable
    data object Home : Screen()
    
    @Serializable
    data object Profile : Screen()
    
    @Serializable
    data object Routine : Screen()

    @Serializable
    data class RoutineDetail(val id: Int) : Screen()

    @Serializable
    data object CreateRoutine : Screen()
    
    @Serializable
    data object Products : Screen()

    @Serializable
    data object AddProduct : Screen()

    @Serializable
    data class EditProduct(val id: Int) : Screen()

    @Serializable
    data class ProductDetail(val id: Int) : Screen()

    @Serializable
    data class ProductSelector(val routineId: Int) : Screen()

    @Serializable
    data object BarcodeScanner : Screen()
    
    @Serializable
    data object Progress : Screen()

    @Serializable
    data object Journal : Screen()

    @Serializable
    data object Assistant : Screen()
}

data class BottomNavItem(
    val route: Screen,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, Icons.Default.Home, "Home"),
    BottomNavItem(Screen.Routine, Icons.Default.EventNote, "Routine"),
    BottomNavItem(Screen.Products, Icons.Default.Inventory2, "Products"),
    BottomNavItem(Screen.Progress, Icons.Default.BarChart, "Progress"),
    BottomNavItem(Screen.Profile, Icons.Default.Person, "Profile")
)
