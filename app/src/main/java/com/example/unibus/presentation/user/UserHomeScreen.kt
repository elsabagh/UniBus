package com.example.unibus.presentation.user

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.unibus.R
import com.example.unibus.data.models.User
import com.example.unibus.navigation.AppDestination
import com.example.unibus.presentation.common.AppHeader
import com.example.unibus.ui.theme.ColorCardIcon
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.ui.theme.ProfileColorCard
import com.example.unibus.ui.theme.colorCardAvailableDriver
import com.example.unibus.ui.theme.colorCardGreen
import com.example.unibus.ui.theme.colorCardRed

@Composable
fun UserHomeScreen(
    navController: NavController,
) {
    val userHomeViewModel: UserHomeViewModel = hiltViewModel()
    val user by userHomeViewModel.user.collectAsState()
    val bookedBus by userHomeViewModel.bookedBus.collectAsState()

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
            IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
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
                navController.navigate(AppDestination.ProfileUserDestination.route)
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 172.dp)
            .padding(bottom = 16.dp)
    ) {

        user?.let { user ->
            UserCard(user)
            Spacer(modifier = Modifier.height(16.dp))

        }
        bookedBus?.let { bus ->
            BooKBusCard(user = bus)
        } ?: run {
            NewTripCard(
                onNewTripClick = {
                    navController.navigate(AppDestination.NewTripDestination.route)
                }
            )
        }
    }
}

@Composable
fun BooKBusCard(
    user: User,
) {
    Text(
        text = "Current Trip",
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
            .padding(horizontal = 8.dp)
            .padding(vertical = 8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter(R.drawable.icon_bus),
                    contentDescription = "Trip Image",
                    modifier = Modifier.size(24.dp),
                )
                Text(text = "Trip NO: #${user.tripNo}")

                Spacer(modifier = Modifier.weight(1f))

            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(colorCardGreen)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Available Seats",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = user.availableSeats,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(colorCardRed)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Reserved Seats",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = user.reservedSeats,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Driver:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.Start),
                color = Color.Gray
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorCardAvailableDriver)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = user.userPhoto,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = user.userName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Image(
                                painter = rememberImagePainter(R.drawable.call),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = user.phoneNumber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = MainColor
                            )
                        }
                    }

//                Spacer(modifier = Modifier.weight(1f))

//                Image(
//                    painter = painterResource(id = getFlagByNationality(driver.nationality)),
//                    contentDescription = null,
//                    modifier = Modifier.size(24.dp)
//                )
                }
            }
        }
    }
}

@Composable
fun UserCard(
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
fun NewTripCard(onNewTripClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable(
                onClick = onNewTripClick
            )
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberImagePainter(R.drawable.icon_bus),
                contentDescription = "Bus",
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
            )
            Text(
                text = "There are no trips",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Text(
                text = "available at the moment",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(MainColor),
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Image(
                            painter = rememberImagePainter(R.drawable.bus_icon_w),
                            contentDescription = "Add",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = "New trip",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Arrow",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun UserCardPreview() {
    val navController = rememberNavController()
    val user = User(
        userName = "John Doe",
        userPhoto = "https://example.com/photo.jpg"
    )
    UserCard(
        user = user
    )
}