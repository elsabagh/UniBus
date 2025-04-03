package com.example.unibus.presentation.driver


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unibus.navigation.AppDestination.SignInDestination


@Composable
fun DriverScreen(
    navController: NavController
) {

    val viewModel: DriverViewModel = hiltViewModel()
    val driver by viewModel.driver.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Driver Screen",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        LogoutButton(
            userViewModel = viewModel,
            navController = navController
        )

    }
}

@Composable
fun LogoutButton(
    userViewModel: DriverViewModel,
    navController: NavController,
) {
    Button(
        onClick = {
            userViewModel.signOutFromAccount()

            navController.navigate(SignInDestination.route) {
                popUpTo(SignInDestination.route) { inclusive = true }
                launchSingleTop = true
            }

        },
        colors = ButtonDefaults.buttonColors(Color.Red),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            "Logout",
            fontSize = 18.sp,
            modifier = Modifier.padding(4.dp)
        )
    }
}