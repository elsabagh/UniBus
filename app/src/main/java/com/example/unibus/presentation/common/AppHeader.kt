package com.example.unibus.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.unibus.R
import com.example.unibus.ui.theme.MainColor

@Composable
fun AppHeader() {
    Row(
        modifier = Modifier
            .background(MainColor)
            .padding(16.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_splash),
            contentDescription = "App Logo",
            modifier = Modifier.size(180.dp).padding(top = 16.dp)
        )
    }
}