package org.luteh.ecommerce.presentation.ui.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lutehecommerce.composeapp.generated.resources.Res
import lutehecommerce.composeapp.generated.resources.register
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.luteh.ecommerce.presentation.component.ErrorView
import org.luteh.ecommerce.presentation.component.LoadingView
import org.luteh.ecommerce.presentation.core.ResultState
import org.luteh.ecommerce.presentation.ui.register.component.RegisterForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(vm: RegisterViewModel = koinInject(), onNavigateBack: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                RegisterViewModel.Effect.NavigateBack -> onNavigateBack()
                is RegisterViewModel.Effect.ShowToast -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(Res.string.register),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            navigationIcon = {
                IconButton(onClick = { vm.processEvent(RegisterViewModel.Event.OnClickBackButton) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            state.value.getRolesResult.let { result ->
                when (result) {
                    is ResultState.Error -> ErrorView(message = result.exception.message.orEmpty()) {
                        vm.processEvent(RegisterViewModel.Event.OnClickRefreshButton)
                    }

                    is ResultState.Success -> RegisterForm(state.value, processEvent = { event ->
                        vm.processEvent(event)
                    })

                    else -> {
                        LoadingView(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}


