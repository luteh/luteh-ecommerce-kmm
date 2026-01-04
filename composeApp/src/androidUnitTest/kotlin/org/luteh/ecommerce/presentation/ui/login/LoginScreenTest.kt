package org.luteh.ecommerce.presentation.ui.login

import android.content.ContentProvider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import org.junit.Before
import org.junit.runner.RunWith
import org.luteh.ecommerce.presentation.core.ResultState
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.util.Logger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginScreenTest {

    @Before
    fun setup() {
        setupAndroidContextProvider()
    }

    // Configures Compose's AndroidContextProvider to access resources in tests.
    // See https://youtrack.jetbrains.com/issue/CMP-6612
    private fun setupAndroidContextProvider() {
        val type = findAndroidContextProvider() ?: return
        Robolectric.setupContentProvider(type)
    }

    private fun findAndroidContextProvider(): Class<ContentProvider>? {
        val providerClassName = "org.jetbrains.compose.resources.AndroidContextProvider"
        return try {
            @Suppress("UNCHECKED_CAST")
            Class.forName(providerClassName) as Class<ContentProvider>
        } catch (_: ClassNotFoundException) {
            Logger.debug("Class not found: $providerClassName")
            // Tests that don't depend on Compose will not have the provider class in classpath and
            // will get
            // ClassNotFoundException. Skip configuring the provider for them.
            null
        }
    }

    @Test
    fun testInitialState() = runComposeUiTest {
        val initialState = LoginViewModel.State()

        setContent {
            LoginScreenContent(
                state = initialState,
                snackbarHostState = SnackbarHostState(),
                onEvent = {},
                onNavigateBack = {},
            )
        }

        onNodeWithTag("email_input").assertIsDisplayed()
        onNodeWithTag("password_input").assertIsDisplayed()
        onNodeWithTag("login_button").assertIsDisplayed()
        onNodeWithTag("register_button").assertIsDisplayed()
        onNodeWithTag("google_signin_button").assertIsDisplayed()
        onNodeWithTag("back_button").assertIsDisplayed()

        // Verify fields are displayed (checking label presence as value is empty)
        onNodeWithTag("email_input").assertTextContains("Email")
        onNodeWithTag("password_input").assertTextContains("Password")
    }

    @Test
    fun testInputHandling() = runComposeUiTest {
        var capturedEvent: LoginViewModel.Event? = null
        val initialState = LoginViewModel.State()

        setContent {
            LoginScreenContent(
                state = initialState,
                snackbarHostState = SnackbarHostState(),
                onEvent = { capturedEvent = it },
                onNavigateBack = {},
            )
        }

        onNodeWithTag("email_input").performTextInput("test@example.com")
        assertTrue(capturedEvent is LoginViewModel.Event.OnChangeEmailText)
        assertEquals(
            "test@example.com",
            (capturedEvent as LoginViewModel.Event.OnChangeEmailText).value,
        )

        onNodeWithTag("password_input").performTextInput("password123")
        assertTrue(capturedEvent is LoginViewModel.Event.OnChangePasswordText)
        assertEquals(
            "password123",
            (capturedEvent as LoginViewModel.Event.OnChangePasswordText).value,
        )
    }

    @Test
    fun testLoadingState() = runComposeUiTest {
        val loadingState = LoginViewModel.State(loginState = ResultState.Loading)

        setContent {
            LoginScreenContent(
                state = loadingState,
                snackbarHostState = SnackbarHostState(),
                onEvent = {},
                onNavigateBack = {},
            )
        }

        onNodeWithTag("loading_indicator").assertIsDisplayed()

        // Verify other elements are NOT displayed (since they are in the 'else' block of 'when')
        onNodeWithTag("email_input").assertDoesNotExist()
        onNodeWithTag("login_button").assertDoesNotExist()
    }

    @Test
    fun testButtonInteractions() = runComposeUiTest {
        var capturedEvent: LoginViewModel.Event? = null
        val initialState = LoginViewModel.State()

        setContent {
            LoginScreenContent(
                state = initialState,
                snackbarHostState = SnackbarHostState(),
                onEvent = { capturedEvent = it },
                onNavigateBack = {},
            )
        }

        onNodeWithTag("login_button").performClick()
        assertEquals(LoginViewModel.Event.OnClickLoginButton, capturedEvent)

        onNodeWithTag("register_button").performClick()
        assertEquals(LoginViewModel.Event.OnClickRegisterButton, capturedEvent)

        onNodeWithTag("google_signin_button").performClick()
        assertEquals(LoginViewModel.Event.OnClickGoogleSignInButton, capturedEvent)
    }

    @Test
    fun testBackButtonInteraction() = runComposeUiTest {
        var backClicked = false
        val initialState = LoginViewModel.State()

        setContent {
            LoginScreenContent(
                state = initialState,
                snackbarHostState = SnackbarHostState(),
                onEvent = {},
                onNavigateBack = { backClicked = true },
            )
        }

        onNodeWithTag("back_button").performClick()
        assertTrue(backClicked)
    }
}
