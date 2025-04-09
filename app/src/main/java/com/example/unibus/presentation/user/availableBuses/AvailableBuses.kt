package com.example.unibus.presentation.user.availableBuses

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.unibus.ui.theme.colorCardGreen
import com.example.unibus.ui.theme.colorCardRed
import com.example.unibus.utils.calculatePrice
import com.example.unibus.utils.checkIfGpsEnabled
import com.example.unibus.utils.fetchLocation
import com.example.unibus.utils.formatToTwoDecimalPlaces
import com.example.unibus.utils.snackbar.SnackBarManager
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AvailableBuses(
    navController: NavController
) {
    val viewModel: AvailableBusesViewModel = hiltViewModel()
    val driversList = viewModel.drivers.collectAsState().value
    val selectedBus = viewModel.selectedBus.collectAsState().value
    var selectedPrice by remember { mutableStateOf("") }  // لحفظ السعر
    val driversWithDistance = viewModel.driversWithDistance.collectAsState().value

    LaunchedEffect(true) {
        viewModel.getAvailableBuses()
    }

    Scaffold(
        topBar = { TopAppBar("Available Buses", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Select Bus and confirm:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn(modifier = Modifier.weight(1f)) {
                    itemsIndexed(driversWithDistance) { _, pair ->
                        val driver = pair.first
                        val distance = pair.second

                        BusCard(
                            driver = driver,
                            distanceInMeters = distance,
                            isSelected = driver == selectedBus,
                            onBusSelected = { selectedDriver, price ->
                                viewModel.selectBus(selectedDriver)
                                selectedPrice = price
                            }
                        )
                    }
                }

                BookedButton(
                    viewModel = viewModel,
                    user = selectedBus ?: User(),
                    price = selectedPrice

                )
            }
        },
    )
}

@Composable
fun BookedButton(
    viewModel: AvailableBusesViewModel,
    user: User,
    price: String,
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var currentTime by remember { mutableStateOf<String>("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(MainColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        val isGpsEnabled = checkIfGpsEnabled(context)
                        if (!isGpsEnabled) {
                            Log.e("LocationDebug", "GPS is disabled")
                            SnackBarManager.showMessage(R.string.please_enable_gps)
                        } else {
                            fetchLocation(fusedLocationClient, context) { latitude, longitude ->
                                currentLocation = Pair(latitude, longitude)

                                val currentDate = Date()
                                val dateFormat =
                                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                currentTime = dateFormat.format(currentDate)

                                val updatedUser = user.copy(
                                    addressMaps = "${currentLocation?.first},${currentLocation?.second}",
                                    busPrice = price
                                )

                                viewModel.bookBus(updatedUser)
                            }
                        }
                    },
                )
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Join",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
        }
    }
}

@Composable
fun BusCard(
    driver: User,
    distanceInMeters: Double,
    isSelected: Boolean,
    onBusSelected: (User, String) -> Unit
) {

    val formattedDistance = formatToTwoDecimalPlaces(distanceInMeters)
    val price = calculatePrice(distanceInMeters)
    val formattedPrice = formatToTwoDecimalPlaces(price.toDouble())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, if (isSelected) MainColor else Color.Gray, RoundedCornerShape(12.dp))
            .clickable {
                onBusSelected(driver, formattedPrice)  // تمرير الأتوبيس المختار والسعر
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
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

                Text(
                    text = "$formattedDistance m",
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
                    text = "$formattedPrice",
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
                            Spacer(modifier = Modifier.width(4.dp))
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
    BusCard(
        driver = driver,
        onBusSelected = { _, _ -> },
        isSelected = false,
        distanceInMeters = 44.4
    )
}

//@DrawableRes
//fun getFlagByNationality(nationality: String): Int {
//    return when (nationality.lowercase()) {
//        "india" -> R.drawable.india_flag
//        "pakistan" -> R.drawable.pakistan_flag
//        else -> R.drawable.default_flag
//    }
//}
