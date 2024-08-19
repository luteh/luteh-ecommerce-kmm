package org.luteh.ecommerce

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.luteh.ecommerce.di.appModule
import org.luteh.ecommerce.presentation.navigation.AppNavigation
import org.luteh.ecommerce.presentation.theme.LutehTheme
import org.luteh.ecommerce.presentation.ui.login.LoginScreen
import org.luteh.ecommerce.presentation.ui.register.RegisterScreen

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule())
    }) {
        LutehTheme {
            val navigator = rememberNavController()

            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = navigator,
                    startDestination = AppNavigation.Login.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(route = AppNavigation.Login.route) {
                        LoginScreen(
                            onNavigateToMainScreen = {},
                            onNavigateToRegisterScreen = {
                                navigator.navigate(AppNavigation.Register.route)
                            })
                    }
                    composable(route = AppNavigation.Register.route) {
                        RegisterScreen(onNavigateBack = {
                            navigator.popBackStack()
                        })
                    }
                }
            }

//            var showContent by remember { mutableStateOf(false) }
//            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                Button(onClick = { showContent = !showContent }) {
//                    Text("Click me!")
//                }
//                AnimatedVisibility(showContent) {
//                    val greeting = remember { Greeting().greet() }
//                    Column(
//                        Modifier.fillMaxWidth(),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Image(painterResource(Res.drawable.compose_multiplatform), null)
//                        Text("Compose: $greeting")
//                    }
//                }
//            }
        }
    }
}