package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.SalonBottomNavigationBar
import com.example.ui.components.SalonTopAppBar
import com.example.ui.screens.BookingScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProviderPortalScreen
import com.example.ui.screens.SecurityAndSettingsScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.SalonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                StephNailSalonApp()
            }
        }
    }
}

@Composable
fun StephNailSalonApp(
    viewModel: SalonViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val unreadNotifs = notifications.count { !it.isRead }

    // Handle Android system back gesture to navigate back to Home if in sub-screen
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateTo(AppScreen.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SalonTopAppBar(
                currentScreen = currentScreen,
                unreadNotificationsCount = unreadNotifs,
                onNavigate = { viewModel.navigateTo(it) }
            )
        },
        bottomBar = {
            SalonBottomNavigationBar(
                currentScreen = currentScreen,
                onNavigate = { viewModel.navigateTo(it) }
            )
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                AppScreen.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                AppScreen.SERVICES -> ServicesScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                AppScreen.BOOKING -> BookingScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                AppScreen.GALLERY -> GalleryScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                AppScreen.CONTACT -> ContactScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                AppScreen.PROVIDER_PORTAL -> ProviderPortalScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                AppScreen.SETTINGS_SECURITY -> SecurityAndSettingsScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    }
}
