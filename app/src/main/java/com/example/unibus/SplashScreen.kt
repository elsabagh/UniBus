package com.example.unibus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unibus.ui.theme.MainColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(isAccountReady: Boolean, userRole: String?, onSplashFinished: () -> Unit) {
    LaunchedEffect(isAccountReady) {
        if (isAccountReady) {
            delay(1000)
            onSplashFinished()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            MainColor
        ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_splash),
            contentDescription = "Splash Screen Logo",
            modifier = Modifier.size(160.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(isAccountReady = true, userRole = "user") {}
}
