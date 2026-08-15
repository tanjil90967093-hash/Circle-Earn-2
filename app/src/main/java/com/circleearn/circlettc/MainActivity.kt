package com.circleearn.circlettc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.circleearn.circlettc.ui.navigation.Screen
import com.circleearn.circlettc.ui.screens.*
import com.circleearn.circlettc.ui.theme.TaskPayTheme
import com.circleearn.circlettc.ui.viewmodel.TaskPayViewModel
import com.circleearn.circlettc.ui.viewmodel.TaskPayViewModelFactory

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      TaskPayTheme {
        val application = application as TaskPayApplication
        val viewModel: TaskPayViewModel = viewModel(
          factory = TaskPayViewModelFactory(application.repository)
        )
        TaskPayApp(viewModel)
      }
    }
  }
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)

@Composable
fun TaskPayApp(viewModel: TaskPayViewModel) {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem(Screen.Home, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem(Screen.Jobs, "Jobs", Icons.Filled.Work, Icons.Outlined.Work),
        BottomNavItem(Screen.Wallet, "Wallet", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
        BottomNavItem(Screen.TopUp, "Top Up", Icons.Filled.SportsEsports, Icons.Outlined.SportsEsports),
        BottomNavItem(Screen.Profile, "Profile", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    androidx.compose.material3.HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    items.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == item.screen::class.qualifiedName } == true
                        val contentColor by androidx.compose.animation.animateColorAsState(
                            targetValue = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            label = "nav_content_color"
                        )
                        val icon = if (isSelected) item.activeIcon else item.inactiveIcon

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    navController.navigate(item.screen) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = icon, 
                                contentDescription = item.label, 
                                tint = contentColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = item.label,
                                color = contentColor,
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Screen.Home> { HomeScreen(viewModel) }
            composable<Screen.Jobs> { JobsScreen(viewModel) }
            composable<Screen.Wallet> { WalletScreen(viewModel) }
            composable<Screen.TopUp> { TopUpScreen(viewModel) }
            composable<Screen.Profile> { ProfileScreen(viewModel) }
        }
    }
}
