package com.dermaroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.dermaroutine.core.designsystem.DermaRoutineTheme
import com.dermaroutine.core.navigation.NavGraph
import com.dermaroutine.core.navigation.Screen
import com.dermaroutine.core.util.OnboardingDataStore
import com.dermaroutine.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var onboardingDataStore: OnboardingDataStore

    @Inject
    lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        setContent {
            val isOnboardingCompleted by onboardingDataStore.isOnboardingCompleted
                .collectAsState(initial = null)
            val currentUser by getCurrentUserUseCase()
                .collectAsState(initial = null)

            // Keep splash screen until we know both states
            splashScreen.setKeepOnScreenCondition { 
                isOnboardingCompleted == null 
            }

            DermaRoutineTheme {
                isOnboardingCompleted?.let { onboardingDone ->
                    val navController = rememberNavController()
                    
                    val startDestination = when {
                        currentUser != null -> Screen.Profile
                        onboardingDone -> Screen.Login
                        else -> Screen.Onboarding
                    }

                    NavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
