package org.luteh.ecommerce.presentation.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import lutehecommerce.composeapp.generated.resources.Res
import lutehecommerce.composeapp.generated.resources.don_t_have_an_account
import lutehecommerce.composeapp.generated.resources.email
import lutehecommerce.composeapp.generated.resources.login
import lutehecommerce.composeapp.generated.resources.or
import lutehecommerce.composeapp.generated.resources.password
import lutehecommerce.composeapp.generated.resources.register_here
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.luteh.ecommerce.presentation.component.RoundedTextField
import org.luteh.ecommerce.presentation.core.ResultState
import org.luteh.ecommerce.presentation.ui.login.component.GoogleSignInButton

@Composable
fun LoginScreen(
    vm: LoginViewModel = koinInject(),
    onNavigateToMainScreen: () -> Unit,
    onNavigateToRegisterScreen: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = vm.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                is LoginViewModel.Effect.ShowToast -> snackbarHostState.showSnackbar(
                    effect.message
                )

                LoginViewModel.Effect.NavigateToMainScreen -> onNavigateToMainScreen()
                LoginViewModel.Effect.NavigateToRegisterScreen -> onNavigateToRegisterScreen()
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValue ->
        Box(
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (state.value.loginState) {
                ResultState.Loading -> {
                    CircularProgressIndicator()
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        RoundedTextField(ß
                            modifier = Modifier.fillMaxWidth(),
                            valueText = state.value.email,
                            onValueChange = {
                                vm.processEvent(
                                    LoginViewModel.Event.OnChangeEmailText(
                                        it
                                    )
                                )
                            },
                            labelText = stringResource(Res.string.email),
                            singleLine = true,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        RoundedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            valueText = state.value.password,
                            onValueChange = {
                                vm.processEvent(
                                    LoginViewModel.Event.OnChangePasswordText(
                                        it
                                    )
                                )
                            },
                            labelText = stringResource(Res.string.password),
                            visualTransformation = PasswordVisualTransformation('*'),
                            singleLine = true,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            onClick = { vm.processEvent(LoginViewModel.Event.OnClickLoginButton) }
                        ) {
                            Text(text = stringResource(Res.string.login))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = stringResource(Res.string.don_t_have_an_account))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = stringResource(Res.string.register_here),
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .clickable {
                                        vm.processEvent(LoginViewModel.Event.OnClickRegisterButton)
                                    },
                                color = Color.Blue,
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(Res.string.or),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GoogleSignInButton(
                            onClick = { vm.processEvent(LoginViewModel.Event.OnClickGoogleSignInButton) }
                        )
                    }
                }
            }
        }
    }
}