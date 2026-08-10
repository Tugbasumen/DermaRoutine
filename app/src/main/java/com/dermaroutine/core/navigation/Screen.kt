package com.dermaroutine.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.dermaroutine.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Onboarding : Screen()
    
    @Serializable
    data object Auth : Screen()
    
    @Serializable
    data object Home : Screen()
    
    @Serializable
    data class Profile(val isFirstTime: Boolean = false) : Screen()

    @Serializable
    data object ReminderSettings : Screen()
    
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
    data class OcrScanner(val productId: Int) : Screen()

    @Serializable
    data object Progress : Screen()

    @Serializable
    data object AddSkinPhoto : Screen()

    @Serializable
    data class SkinPhotoDetail(val id: Int) : Screen()

    @Serializable
    data object ComparePhotos : Screen()

    @Serializable
    data object Journal : Screen()

    @Serializable
    data object AIAssistant : Screen()

    @Serializable
    data class Assistant(val ingredientName: String) : Screen()
}

data class BottomNavItem(
    val route: Screen,
    val icon: ImageVector,
    val labelRes: Int
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, Icons.Default.Home, R.string.nav_home),
    BottomNavItem(Screen.Routine, Icons.Default.EventNote, R.string.nav_routine),
    BottomNavItem(Screen.Products, Icons.Default.Inventory2, R.string.nav_products),
    BottomNavItem(Screen.Progress, Icons.Default.BarChart, R.string.nav_progress),
    BottomNavItem(Screen.Profile(), Icons.Default.Person, R.string.nav_profile)
)
