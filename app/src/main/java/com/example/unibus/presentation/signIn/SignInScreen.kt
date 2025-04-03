package com.example.unibus.presentation.signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unibus.R
import com.example.unibus.presentation.common.EmailField
import com.example.unibus.presentation.common.PasswordField
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.ui.theme.UniBusTheme

@Composable
fun SignInScreen(
    onSignInClick: () -> Unit = {},
    onSignUpClickNav: () -> Unit = {},
    onDriverSignIn: () -> Unit = {},
    onNavigateToHome: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {

    val signInViewModel: SignInViewModel = hiltViewModel()
    val uiState by signInViewModel.uiState.collectAsStateWithLifecycle()
    val isSignInSucceeded by signInViewModel.isSignInSucceeded.collectAsStateWithLifecycle()
    val isAccountReady by signInViewModel.isAccountReady.collectAsStateWithLifecycle()
    val userRole by signInViewModel.userRole.collectAsStateWithLifecycle()

    LaunchedEffect(isSignInSucceeded) {
        if (isSignInSucceeded) {
            when (userRole) {
                "driver" -> onDriverSignIn()
                "user" -> onSignInClick()
            }
            signInViewModel.resetIsSignInSucceeded()

        }
    }

    val onEmailChange: (String) -> Unit = remember { signInViewModel::onEmailChange }
    val onPasswordChange: (String) -> Unit = remember { signInViewModel::onPasswordChange }

    SignInScreenContent(
        uiState = uiState,
        onEmailChange = onEmailChange,
        onPasswordChange = onPasswordChange,
        onSignInClick = { signInViewModel.signInToAccount("user") },
        onDriverClick = { signInViewModel.signInToAccount("driver") },
        onSignUpClick = onSignUpClickNav,
        onAppStart = { userRole?.let { onNavigateToHome(it) } }
    )
}

@Composable
fun SignInScreenContent(
    uiState: SignInState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onDriverClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAppStart: () -> Unit
) {
    LaunchedEffect(Unit) {
        onAppStart()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {

        Spacer(modifier = Modifier.padding(16.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_image),
            contentDescription = null,
            modifier = modifier
                .padding(top = 86.dp)
        )
        Spacer(modifier = Modifier.padding(16.dp))

        EmailField(
            value = uiState.email,
            onNewValue = onEmailChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 64.dp)

        )

        Spacer(modifier = Modifier.padding(16.dp))

        PasswordField(
            value = uiState.password,
            placeholder = R.string.password,
            onNewValue = onPasswordChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(MainColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable(
                    onClick = {
                        onSignUpClick()
                    }
                )
                .align(Alignment.CenterHorizontally),
            text = buildAnnotatedString {
                append(stringResource(R.string.don_t_have_an_account_sign_up))
                withStyle(
                    style = SpanStyle(color = MainColor),
                )
                {
                    append(" Sign up")
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Row(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .padding(top = 46.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                modifier = Modifier.clickable { onDriverClick() },
                text = "Login as a Driver",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MainColor
            )
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInPreview() {
    UniBusTheme {
        SignInScreenContent(
            uiState = SignInState(),
            onEmailChange = {},
            onPasswordChange = {},
            onSignInClick = {},
            onDriverClick = {},
            onSignUpClick = {},
            onAppStart = {}
        )
    }

}