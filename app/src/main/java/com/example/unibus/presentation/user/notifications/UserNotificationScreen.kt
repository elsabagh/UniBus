package com.example.unibus.presentation.user.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unibus.data.models.Notification
import com.example.unibus.navigation.AppDestination.PaymentDestination
import com.example.unibus.presentation.common.TopAppBar
import com.example.unibus.ui.theme.MainColor

@Composable
fun UserNotificationScreen(
    navController: NavController,
) {
    val viewModel: UserNotificationViewModel = hiltViewModel()
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        topBar = { TopAppBar("Notification", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                LazyColumn {
                    itemsIndexed(notifications) { _, notification ->
                        if (notification.notificationType == "payment") {
                            NotificationItemPay(
                                notification = notification,
                                onPaymentClick = {
                                    navController.navigate(PaymentDestination.route)
                                }
                            )
                        } else {
                            NotificationItem(notification)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun NotificationItem(notification: Notification) {
    Column(modifier = Modifier) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
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
                text = notification.message,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .weight(1f)
            )
        }
        Text(
            text = "${notification.date} ${notification.time}",
            modifier = Modifier.align(Alignment.End),
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color.LightGray)
        )
    }
}


@Composable
fun NotificationItemPay(
    notification: Notification,
    onPaymentClick: () -> Unit = {}
) {
    Column(modifier = Modifier) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
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
                text = notification.message,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .weight(1f)
            )
        }

        Card(
            modifier = Modifier
                .padding(8.dp)
                .padding(start = 46.dp)
                .clickable(
                    onClick = {
                        onPaymentClick()
                    }
                )
        ) {
            Row(
                modifier = Modifier
                    .background(MainColor)
                    .padding(8.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pay now",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                )
                Icon(
                    imageVector = Icons.Filled.Paid,
                    contentDescription = "pay",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }

        Text(
            text = "${notification.date} ${notification.time}",
            modifier = Modifier.align(Alignment.End),
            color = Color.Gray,
            fontSize = 12.sp
        )



        Spacer(modifier = Modifier.height(8.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color.LightGray)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationItemPayPreview() {
    val sampleNotification = Notification(
        title = "Trip Approved",
        message = "Your trip request has been approved by the driver.",
        date = "2023-10-01",
        time = "10:30 AM"
    )
    NotificationItemPay(notification = sampleNotification)
}


@Preview(showBackground = true)
@Composable
fun NotificationItemPreview() {
    val sampleNotification = Notification(
        title = "Trip Approved",
        message = "Your trip request has been approved by the driver.",
        date = "2023-10-01",
        time = "10:30 AM"
    )
    NotificationItem(notification = sampleNotification)
}
