package com.example.unibus

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.unibus.navigation.NavGraph
import com.example.unibus.presentation.signIn.SignInViewModel
import com.example.unibus.ui.theme.UniBusTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContainerApp(
    modifier: Modifier = Modifier,
    navigateTo: String? = null,
) {
    val appState = rememberAppState()
    val signInViewModel: SignInViewModel = hiltViewModel()
    val userRole = signInViewModel.userRole.collectAsState().value
    val isAccountReady = signInViewModel.isAccountReady.collectAsState().value

    UniBusTheme {
        Box(modifier = modifier.fillMaxSize()) {
            NavGraph(
                appState = appState,
                userRole = userRole,
                isAccountReady = isAccountReady
            )

            SnackbarHost(
                hostState = appState.scaffoldState.snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 36.dp)
                    .align(Alignment.BottomCenter),
                snackbar = { snackBarData ->
                    Snackbar(
                        snackBarData,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )
        }
    }
}