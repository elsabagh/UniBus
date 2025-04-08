package com.example.unibus.presentation.driver.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unibus.data.models.User
import com.example.unibus.presentation.common.TopAppBar

@Composable
fun NotificationDriverScreen(
    navController: NavController
) {
    val notificationViewModel: NotificationDriverViewModel = hiltViewModel()
    val notifications = notificationViewModel.notifications.collectAsState()

    Scaffold(
        topBar = { TopAppBar("Notification", navController) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(notifications.value) {_, user ->
                    PassengerNotificationCard(
                        user = user,
                        onApprove = {
                            notificationViewModel.approveBooking(user.userId)
                        },
                        onReject = {
                            notificationViewModel.rejectBooking(user.userId)
                        }
                    )
                }
            }
        }
    )
}


@Composable
fun PassengerNotificationCard(
    user: User,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "New Passenger: ${user.userName}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${user.bookedDate} ${user.bookedTime}", style = MaterialTheme.typography.bodySmall)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = onApprove) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = "Approve", tint = Color.Green)

                    }
                    IconButton(onClick = onReject) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = "Reject", tint = Color.Red)

                    }
                }
                Text(text = "Distance: 850m", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
