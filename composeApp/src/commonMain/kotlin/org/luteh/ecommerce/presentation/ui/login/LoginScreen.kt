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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
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
import lutehecommerce.composeapp.generated.resources.sign_in_with_google
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.luteh.ecommerce.presentation.component.RoundedTextField
import org.luteh.ecommerce.presentation.core.ResultState
import org.luteh.ecommerce.presentation.ui.login.component.GoogleSignInButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinInject(),
    onNavigateToMainScreen: () -> Unit,
    onNavigateToRegisterScreen: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginViewModel.Effect.ShowToast -> snackbarHostState.showSnackbar(effect.message)

                LoginViewModel.Effect.NavigateToMainScreen -> onNavigateToMainScreen()
                LoginViewModel.Effect.NavigateBack -> onNavigateBack()
                LoginViewModel.Effect.NavigateToRegisterScreen -> onNavigateToRegisterScreen()
            }
        }
    }

    LoginScreenContent(
        state = state.value,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::processEvent,
        onNavigateBack = onNavigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    state: LoginViewModel.State,
    snackbarHostState: SnackbarHostState,
    onEvent: (LoginViewModel.Event) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.login)) },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("back_button"),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { paddingValue ->
        Box(
            modifier = Modifier.padding(paddingValue).fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (state.loginState) {
                ResultState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
                }

                else -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        RoundedTextField(
                            modifier = Modifier.fillMaxWidth().testTag("email_input"),
                            valueText = state.email,
                            onValueChange = { onEvent(LoginViewModel.Event.OnChangeEmailText(it)) },
                            labelText = stringResource(Res.string.email),
                            singleLine = true,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        RoundedTextField(
                            modifier = Modifier.fillMaxWidth().testTag("password_input"),
                            valueText = state.password,
                            onValueChange = {
                                onEvent(LoginViewModel.Event.OnChangePasswordText(it))
                            },
                            labelText = stringResource(Res.string.password),
                            visualTransformation = PasswordVisualTransformation('*'),
                            singleLine = true,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            modifier =
                                Modifier.fillMaxWidth().height(50.dp).testTag("login_button"),
                            shape = RoundedCornerShape(12.dp),
                            onClick = { onEvent(LoginViewModel.Event.OnClickLoginButton) },
                        ) {
                            Text(text = stringResource(Res.string.login))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(text = stringResource(Res.string.don_t_have_an_account))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(Res.string.register_here),
                                modifier =
                                    Modifier.clickable {
                                            onEvent(LoginViewModel.Event.OnClickRegisterButton)
                                        }
                                        .testTag("register_button"),
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(Res.string.or),
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GoogleSignInButton(
                            modifier = Modifier.testTag("google_signin_button"),
                            text = stringResource(Res.string.sign_in_with_google),
                            onClick = { onEvent(LoginViewModel.Event.OnClickGoogleSignInButton) },
                        )
                    }
                }
            }
        }
    }
}
