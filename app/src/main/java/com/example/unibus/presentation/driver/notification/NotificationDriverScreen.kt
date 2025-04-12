package com.example.unibus.presentation.driver.notification

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unibus.data.models.User
import com.example.unibus.presentation.common.TopAppBar
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.ui.theme.colorCardGreen
import com.example.unibus.ui.theme.colorCardRed

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
                itemsIndexed(notifications.value) { _, userWithDocId ->
                    PassengerNotificationCard(
                        user = userWithDocId.user,
                        onApprove = {
                            notificationViewModel.approveBooking(
                                documentId = userWithDocId.documentId,
                                driverBusId = userWithDocId.user.driverBusId
                            )
                            notificationViewModel.sendCustomNotificationToUser(
                                user = userWithDocId.user,
                                title = "Booking Approved",
                                message = "Driver ${userWithDocId.user.driverBusName} has approved the request to join the trip",
                                type = "approved"
                            )
                        },
                        onReject = {
                            notificationViewModel.rejectBooking(userWithDocId.documentId)
                            notificationViewModel.sendCustomNotificationToUser(
                                user = userWithDocId.user,
                                title = "Booking Rejected",
                                message = "Driver ${userWithDocId.user.driverBusName} has approved the request to join the trip",
                                type = "rejected"
                            )
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

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Filled.NotificationImportant,
                contentDescription = "notification",
                tint = MainColor,
                modifier = Modifier
                    .size(46.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "New Passenger: ${user.userName}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "850m",
                style = MaterialTheme.typography.bodySmall,
                color = MainColor
            )


        }
        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 46.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(shape = CircleShape)
                    .background(colorCardGreen)
            ) {
                IconButton(onClick = onApprove) {
                    Icon(
                        Icons.Default.ArrowDownward,
                        contentDescription = "Approve",
                        tint = Color.White
                    )

                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(shape = CircleShape)
                    .background(colorCardRed)
            ) {
                IconButton(onClick = onReject) {
                    Icon(
                        Icons.Default.ArrowUpward,
                        contentDescription = "Reject",
                        tint = Color.White
                    )

                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${user.bookedDate} ${user.bookedTime}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color.LightGray)
    )
}

@Preview(showBackground = true)
@Composable
fun PassengerNotificationCardPreview() {
    val user = User(
        userName = "Jane Doe",
        bookedDate = "2023-10-01",
        bookedTime = "10:30 AM",
        userId = "12345"
    )
    PassengerNotificationCard(
        user = user,
        onApprove = { /* Handle approve */ },
        onReject = { /* Handle reject */ }
    )
}