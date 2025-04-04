package com.example.unibus.presentation.driver


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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.unibus.data.models.User
import com.example.unibus.navigation.AppDestination
import com.example.unibus.navigation.AppDestination.SignInDestination
import com.example.unibus.presentation.common.AppHeader
import com.example.unibus.presentation.user.UserCard
import com.example.unibus.ui.theme.ColorCardIcon
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.ui.theme.ProfileColorCard


@Composable
fun DriverScreen(
    navController: NavController
) {

    val viewModel: DriverViewModel = hiltViewModel()
    val driver by viewModel.driver.collectAsState()


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

        driver?.let {user ->
            DriverCard(user)
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