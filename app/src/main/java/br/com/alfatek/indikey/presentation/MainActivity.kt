package br.com.alfatek.indikey.presentation

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import br.com.alfatek.indikey.model.Cliente
import br.com.alfatek.indikey.presentation.pages.auth.RegisterUserScreen
import br.com.alfatek.indikey.presentation.pages.auth.RegisterUserViewModel
import br.com.alfatek.indikey.presentation.pages.cliente.AddClientScreen
import br.com.alfatek.indikey.presentation.pages.cliente.ClientListScreen
import br.com.alfatek.indikey.presentation.pages.cliente.UpdateClientScreen
import br.com.alfatek.indikey.presentation.pages.dashboard.DashboardScreen
import br.com.alfatek.indikey.presentation.pages.login.LoginScreen
import br.com.alfatek.indikey.ui.theme.IndiKeyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<RegisterUserViewModel>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {

            IndiKeyTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = if (viewModel.currentUser != null) ScreenC else ScreenA
                ) {
                    composable<ScreenA> {
                        RegisterUserScreen(onNavigateToDashboard = {
                            navController.navigate(ScreenC)
                        })
                    }
                    composable<ScreenB> {
                        LoginScreen(onLoginClick = {
                            navController.navigate(ScreenC)
                        })
                    }
                    composable<ScreenC> {
                        DashboardScreen(onClientClick = {
                            navController.navigate(ScreenD)
                        },onClientListClick = {
                            navController.navigate(ScreenE)
                        },onEditClientClick = {
                            navController.navigate(ScreenF)//Todo create edit screen
                        }
                        )
                    }
                    composable<ScreenD> {
                        AddClientScreen(onBackClick = {
                            navController.navigate(ScreenC)
                        })
                    }
                    composable<ScreenE> {
                        ClientListScreen(onBackClick = {
                            navController.navigate(ScreenC)
                        })

                    }
                    composable<ScreenF> {
                        val client = it.toRoute<ScreenF>()
                        UpdateClientScreen(onBackClick = {
                            navController.navigate(ScreenC)
                        })
                    }
                }
            }
        }

    }
}

