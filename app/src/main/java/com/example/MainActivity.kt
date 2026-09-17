package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.NotificationHelper
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GarageScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.VehicleViewModel
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    NotificationHelper.createNotificationChannel(this)
    try {
      MobileAds.initialize(this)
    } catch (_: Throwable) {}
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val viewModel: VehicleViewModel = viewModel()
        var showSplash by remember { mutableStateOf(true) }
        var currentTab by remember { mutableStateOf(Tab.DASHBOARD) }

        Crossfade(targetState = showSplash, label = "splash_transition") { isSplash ->
          if (isSplash) {
            SplashScreen(
              onEnterApp = { showSplash = false }
            )
          } else {
            Scaffold(
              modifier = Modifier.fillMaxSize(),
              containerColor = MaterialTheme.colorScheme.background,
              bottomBar = {
                NavigationBar(
                  modifier = Modifier.testTag("bottom_nav"),
                  containerColor = MaterialTheme.colorScheme.surface,
                  tonalElevation = 8.dp
                ) {
                  NavigationBarItem(
                    selected = currentTab == Tab.DASHBOARD,
                    onClick = { currentTab = Tab.DASHBOARD },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Painel") },
                    label = { Text("Painel", fontWeight = if (currentTab == Tab.DASHBOARD) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                      unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                      unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.testTag("nav_dashboard")
                  )
                  NavigationBarItem(
                    selected = currentTab == Tab.GARAGE,
                    onClick = { currentTab = Tab.GARAGE },
                    icon = { Icon(Icons.Default.Garage, contentDescription = "Garagem") },
                    label = { Text("Garagem", fontWeight = if (currentTab == Tab.GARAGE) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                      unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                      unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.testTag("nav_garage")
                  )
                  NavigationBarItem(
                    selected = currentTab == Tab.WALLET,
                    onClick = { currentTab = Tab.WALLET },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Carteira") },
                    label = { Text("Carteira", fontWeight = if (currentTab == Tab.WALLET) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                      unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                      unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.testTag("nav_wallet")
                  )
                  NavigationBarItem(
                    selected = currentTab == Tab.CALCULATOR,
                    onClick = { currentTab = Tab.CALCULATOR },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Calculadoras") },
                    label = { Text("Calculadoras", fontWeight = if (currentTab == Tab.CALCULATOR) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                      unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                      unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.testTag("nav_calculator")
                  )
                }
              }
            ) { innerPadding ->
              when (currentTab) {
                Tab.DASHBOARD -> DashboardScreen(
                  viewModel = viewModel,
                  onNavigateToGarage = { currentTab = Tab.GARAGE },
                  modifier = Modifier.padding(innerPadding)
                )
                Tab.GARAGE -> GarageScreen(
                  viewModel = viewModel,
                  modifier = Modifier.padding(innerPadding)
                )
                Tab.WALLET -> WalletScreen(
                  viewModel = viewModel,
                  modifier = Modifier.padding(innerPadding)
                )
                Tab.CALCULATOR -> CalculatorScreen(
                  viewModel = viewModel,
                  modifier = Modifier.padding(innerPadding)
                )
              }
            }
          }
        }
      }
    }
  }
}

enum class Tab {
  DASHBOARD, GARAGE, WALLET, CALCULATOR
}

