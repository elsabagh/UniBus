package com.example.unibus.presentation.driver


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.unibus.R
import com.example.unibus.data.models.User
import com.example.unibus.navigation.AppDestination
import com.example.unibus.presentation.common.AppHeader
import com.example.unibus.presentation.driver.notification.NotificationDriverViewModel
import com.example.unibus.ui.theme.ColorCardIcon
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.ui.theme.ProfileColorCard
import com.example.unibus.ui.theme.colorCardGreen
import com.example.unibus.ui.theme.colorCardRed
import kotlinx.coroutines.delay

@Composable
fun DriverScreen(
    navController: NavController
) {

    val viewModel: DriverViewModel = hiltViewModel()
    val driver by viewModel.driver.collectAsState()

    val context = LocalContext.current
    val notificationViewModel: NotificationDriverViewModel = hiltViewModel()
    val hasNotifications by notificationViewModel.hasNotifications.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            notificationViewModel.getTripNo { tripNo ->
                if (tripNo.isNotBlank()) {
                    notificationViewModel.loadNotificationsForTrip(tripNo, "processing", context)
                }
            }
            delay(1000)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
    }


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MainColor),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ColorCardIcon)
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(AppDestination.NotificationDriverDestination.route)
                        notificationViewModel.markNotificationsAsRead()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                    )
                }
                if (hasNotifications) {
                    Box(
                        modifier = Modifier.padding(8.dp)
                            .size(10.dp)
                            .align(Alignment.TopEnd)
                            .background(Color.Red, shape = CircleShape)
                    )
                }
            }
            AppHeader()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ColorCardIcon)
            ) {
                IconButton(onClick = {
                    navController.navigate(AppDestination.ProfileDriverDestination.route)
                }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                    )
                }
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {

        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 172.dp)
            .padding(bottom = 16.dp)
    ) {

        driver?.let { user ->
            DriverCard(user)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .verticalScroll(
                        rememberScrollState()
                    )

            ) {
                CurrentTrip(
                    driver = user
                )

                Spacer(modifier = Modifier.height(8.dp))
                UniversityLocation(
                    onClick = {
                        navController.navigate(AppDestination.UniLocationDestinationDriverDestination.route)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                StudentsCard(
                    onClick = {
                        navController.navigate(AppDestination.StudentsListDestination.route)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ButtonEmptyBus(
                    onClick = {
                        viewModel.emptyBus()
                    }
                )
            }
        } ?: run {
            CircularProgressIndicator()
        }
    }

}

@Composable
fun DriverCard(
    user: User,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(ProfileColorCard),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .background(Color.White)
                ) {
                    Image(
                        painter = rememberImagePainter(user.userPhoto),
                        contentDescription = "User Photo",
                        modifier = Modifier
                            .padding(2.dp)
                            .size(50.dp)
                            .fillMaxWidth()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Welcome!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainColor
                    )

                    Text(
                        text = user.userName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentTrip(
    driver: User
) {
    Text(
        text = "Current Trip:",
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Gray,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter(R.drawable.icon_bus),
                    contentDescription = "Trip Image",
                    modifier = Modifier.size(24.dp),
                )
                Text(
                    text = "Trip NO :",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
                Text(
                    text = "#${driver.tripNo}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MainColor
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorCardGreen)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Available Seats",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = driver.availableSeats,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorCardRed)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Reserved Seats",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = driver.reservedSeats,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UniversityLocation(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .clickable(
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(R.drawable.courthouse),
                    contentDescription = "Trip Image",
                    modifier = Modifier.size(28.dp),
                )
                Text(
                    text = "University Location",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Icon(
                painter = rememberImagePainter(R.drawable.direct_right),
                contentDescription = "Location Icon",
                tint = MainColor,
                modifier = Modifier.size(24.dp)
            )

        }
    }
}

@Composable
fun StudentsCard(
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .clickable(
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(R.drawable.user_square),
                    contentDescription = "Trip Image",
                    modifier = Modifier.size(28.dp),
                )
                Text(
                    text = "Students",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Icon(
                painter = rememberImagePainter(R.drawable.direct_right),
                contentDescription = "Location Icon",
                tint = MainColor,
                modifier = Modifier.size(24.dp)
            )

        }
    }
}

@Composable
fun ButtonEmptyBus(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp)
            .border(2.dp, MainColor, RoundedCornerShape(12.dp))
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Empty Bus",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MainColor,
                modifier = Modifier
                    .padding(vertical = 16.dp)

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDriverCard() {
    DriverCard(
        user = User(
            userName = "John Doe",
            userPhoto = "https://example.com/photo.jpg",
            tripNo = "123",
            availableSeats = "10",
            reservedSeats = "5"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCurrentTrip() {
    CurrentTrip(
        driver = User(
            userName = "John Doe",
            userPhoto = "https://example.com/photo.jpg",
            tripNo = "123",
            availableSeats = "10",
            reservedSeats = "5"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewUniversityLocation() {
    UniversityLocation(
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewStudentsCard() {
    StudentsCard(
        onClick = {}
    )
}
@Preview(showBackground = true)
@Composable
fun PreviewButtonEmptyBus() {
    ButtonEmptyBus(
        onClick = {}
    )
}


