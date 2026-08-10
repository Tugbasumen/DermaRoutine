package com.dermaroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dermaroutine.core.designsystem.DermaRoutineTheme
import com.dermaroutine.core.navigation.NavGraph
import com.dermaroutine.core.navigation.Screen
import com.dermaroutine.core.navigation.bottomNavItems
import com.dermaroutine.core.util.OnboardingDataStore
import com.dermaroutine.domain.usecase.GetCurrentUserUseCase
import com.dermaroutine.domain.usecase.GetProfileUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var onboardingDataStore: OnboardingDataStore

    @Inject
    lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @Inject
    lateinit var getProfileUseCase: GetProfileUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        setContent {
            val isOnboardingCompleted by onboardingDataStore.isOnboardingCompleted
                .collectAsState(initial = null)
            val currentUser by getCurrentUserUseCase()
                .collectAsState(initial = null)
            
            var hasProfile by remember { mutableStateOf<Boolean?>(null) }

            // Fetch profile status once user is logged in
            LaunchedEffect(currentUser) {
                if (currentUser != null) {
                    getProfileUseCase().onSuccess {
                        hasProfile = it != null
                    }.onFailure {
                        hasProfile = false // Assume no profile on error for safety
                    }
                } else {
                    hasProfile = null
                }
            }

            // Keep splash screen until we know the essential states
            splashScreen.setKeepOnScreenCondition { 
                isOnboardingCompleted == null || (currentUser != null && hasProfile == null)
            }

            DermaRoutineTheme {
                isOnboardingCompleted?.let { onboardingDone ->
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    // ... existing deep link handling ...

                    // Deep Link Handling from Notifications
                    LaunchedEffect(intent) {
                        intent?.getStringExtra("navigate_to")?.let { target ->
                            when (target) {
                                "morning_routine" -> {
                                    // For now navigate to the general Routine list or specific detail if we have ID
                                    // But based on Phase 13 spec, just open appropriate routine screen.
                                    navController.navigate(Screen.Routine)
                                }
                                "evening_routine" -> {
                                    navController.navigate(Screen.Routine)
                                }
                            }
                            intent.removeExtra("navigate_to")
                        }
                    }

                    val showBottomBar = bottomNavItems.any { item ->
                        currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
                    }

                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 8.dp
                                ) {
                                    bottomNavItems.forEach { item ->
                                        val label = stringResource(id = item.labelRes)
                                        NavigationBarItem(
                                            icon = { Icon(item.icon, contentDescription = label) },
                                            label = { Text(label) },
                                            selected = currentDestination?.hierarchy?.any { 
                                                it.hasRoute(item.route::class) 
                                            } == true,
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                                indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                                            ),
                                            onClick = {
                                                navController.navigate(item.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) { padding ->
                        Box(modifier = Modifier.padding(padding)) {
                            val startDestination = when {
                                currentUser != null && hasProfile == true -> Screen.Home
                                currentUser != null && hasProfile == false -> Screen.Profile(isFirstTime = true)
                                onboardingDone -> Screen.Auth
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
    }
}
