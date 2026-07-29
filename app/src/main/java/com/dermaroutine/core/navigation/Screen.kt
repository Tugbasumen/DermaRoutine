package com.dermaroutine.core.navigation

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
    data object Products : Screen()
    
    @Serializable
    data object Progress : Screen()
}
