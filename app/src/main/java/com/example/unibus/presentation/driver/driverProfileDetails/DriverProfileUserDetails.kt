package com.example.unibus.presentation.driver.driverProfileDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.unibus.data.models.User
import com.example.unibus.navigation.AppDestination
import com.example.unibus.presentation.user.profile.profileDetails.ProfileUserDetailsViewModel
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.ui.theme.colorButtonRed
import com.example.unibus.ui.theme.itemColorProfile

@Composable
fun DriverProfileUserDetails(
    navController: NavController
) {
    val viewModel: ProfileUserDetailsViewModel = hiltViewModel()
    val user by viewModel.user.collectAsState()

    Scaffold(
        topBar = { DriverProfileDetailsTopAppBar(navController, user) },
        content = { paddingValues ->
            Row(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(MainColor),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {}
            Column(
                modifier = Modifier
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                user?.let { user ->
                    ProfileContent(
                        user
                    )
                    LogoutButton(
                        viewModel,
                        navController
                    )
                } ?: CircularProgressIndicator()
            }
        }
    )
}

@Composable
fun ProfileContent(
    user: User,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(4.dp, Color.White, CircleShape)
        ) {
            Image(
                painter = rememberImagePainter(user.userPhoto),
                contentDescription = "User Photo",
                modifier = Modifier
                    .size(120.dp)
                    .fillMaxWidth()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = user.userName,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        ProfileDetailCard("Email", user.email)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileDetailCard("UNI ID", user.idNumber)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileDetailCard("Mobile", user.phoneNumber)

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun ProfileDetailCard(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = label, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .align(Alignment.Start),
            fontSize = 14.sp
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Text(
                text = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(itemColorProfile)
                    .padding(8.dp)
                    .align(Alignment.Start),
                color = MaterialTheme.colorScheme.background
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun LogoutButton(
    userViewModel: ProfileUserDetailsViewModel,
    navController: NavController
) {
    Button(
        onClick = {
            userViewModel.signOutFromAccount()

            navController.popBackStack(AppDestination.SignInDestination.route, inclusive = false)

            navController.navigate(AppDestination.SignInDestination.route) {
                popUpTo(AppDestination.SignInDestination.route) { inclusive = true }
                launchSingleTop = true
            }
        },
        colors = ButtonDefaults.buttonColors(colorButtonRed),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Logout",
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverProfileDetailsTopAppBar(navController: NavController, user: User?) {
    TopAppBar(
        title = {
            Text(
                "Profile",
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack, contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MainColor
        )
    )
}
