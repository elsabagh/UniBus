package com.example.unibus.presentation.user.availableBuses

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.unibus.R
import com.example.unibus.data.models.User
import com.example.unibus.presentation.common.TopAppBar
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.ui.theme.colorCardAvailableDriver

@Composable
fun AvailableBuses(
    navController: NavController
) {
    val viewModel: AvailableBusesViewModel = hiltViewModel()
    val driversList = viewModel.drivers.collectAsState().value

    LaunchedEffect(true) {
        viewModel.getAvailableBuses()
    }

    Scaffold(
        topBar = { TopAppBar("Available Buses", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(driversList) { _, driver ->
                        BusCard(driver)
                    }
                }
            }
        }
    )
}


@Composable
fun BusCard(driver: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 16.dp),
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
                Text(text = "Trip NO: #${driver.tripNo}")

                Spacer(modifier = Modifier.weight(1f))

                Text(text = "1053m",
                    color = MainColor,
                )

            }

            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Price:",
                    modifier = Modifier,
                )
                Text(
                    text = "10K.D",
                    modifier = Modifier.padding(start = 8.dp),
                    color = MainColor,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Green)
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
                            text = driver.availableSeats,
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
                        .background(Color.Red)
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
                            text = driver.reservedSeats,
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
                        model = driver.userPhoto,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = driver.userName,
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
                            Text(
                                text = driver.phoneNumber,
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

@Preview(showBackground = true)
@Composable
fun BusCardPreview() {
    val driver = User(
        userName = "Jane Doe",
        userPhoto = "https://example.com/photo.jpg",
        tripNo = "123",
        availableSeats = "10",
        reservedSeats = "5",
        phoneNumber = "123-456-7890"
    )
    BusCard(driver = driver)
}

//@DrawableRes
//fun getFlagByNationality(nationality: String): Int {
//    return when (nationality.lowercase()) {
//        "india" -> R.drawable.india_flag
//        "pakistan" -> R.drawable.pakistan_flag
//        else -> R.drawable.default_flag
//    }
//}
