package com.example.unibus.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unibus.ui.theme.MainColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(label: String, navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = label,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (!navController.popBackStack()) {
                    navController.navigate("splash")
                }
            }) {
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

@Preview(showBackground = true)
@Composable
fun PreviewTopAppBar() {
    val navController = rememberNavController()
    TopAppBar(label = "Preview", navController = navController)
}