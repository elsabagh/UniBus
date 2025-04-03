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
import androidx.navigation.NavController
import com.example.unibus.ui.theme.MainColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(label: String, navController: NavController) {
    TopAppBar(
        title = { Text(label) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack()}) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MainColor // اللون الأصفر
        )
    )
}