package com.example.unibus.presentation.driver.studentsApproveList


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.unibus.data.models.User
import com.example.unibus.navigation.AppDestination
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.ui.theme.UniBusTheme
import com.example.unibus.ui.theme.itemColorProfile
import com.google.android.gms.maps.model.LatLng


@Composable
fun UserDetailsDialog(
    user: User,
    onDismiss: () -> Unit,
    navController: NavController
) {
    // تقسيم addressMaps إلى خط العرض وخط الطول
    val coordinates = user.addressMaps.split(",")
    val lat = coordinates[0].toDouble()  // استخراج خط العرض
    val lng = coordinates[1].toDouble()  // استخراج خط الطول
    val userLocation = LatLng(lat, lng)  // إنشاء متغير LatLng من الإحداثيات
    Log.d("UserLocationScreen", "User Location: $lat")
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = { onDismiss() },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Image(
                        painter = rememberImagePainter(user.userPhoto),
                        contentDescription = "User Photo",
                        modifier = Modifier
                            .size(100.dp)
                            .fillMaxWidth()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = user.userName,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                ProfileDetailCard(
                    label = "Email",
                    value = user.email
                )

                ProfileDetailCard(
                    label = "UNI ID",
                    value = user.idNumber
                )
                ProfileDetailCard(
                    label = "Phone",
                    value = user.phoneNumber
                )
            }
        },
        confirmButton = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        onClick = {
                            // تمرير الإحداثيات إلى UserLocationScreen عند الضغط
                            navController.navigate(
                                AppDestination.UserLocationDestination.route +
                                        "?lat=${lat}&lng=${lng}"
                            )
                        }),
                colors = CardDefaults.cardColors(MainColor),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LocationText(location = user.addressMaps)
                }
            }
        }
    )
}



@Composable
fun ProfileDetailCard(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .padding(vertical = 4.dp)
                .align(Alignment.Start),
            fontSize = 14.sp,
            color = Color.Gray
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))

        ) {
            Text(
                text = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(itemColorProfile)
                    .padding(8.dp)
                    .padding(vertical = 4.dp)
                    .align(Alignment.Start),
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileDetailCard() {
    ProfileDetailCard(label = "Email", value = "johndoe@example.com")
}

@Composable
fun LocationText(location: String) {
    var locationText by remember { mutableStateOf(location) }
    val context = LocalContext.current
    Text(
        text = "Location",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(8.dp),
        color = Color.White

    )

}

@Preview(showBackground = true)
@Composable
fun PreviewUserDetailsDialog() {
    UniBusTheme {
        UserDetailsDialog(
            user = User(
                userName = "John Doe",
                email = "johndoe@example.com",
                idNumber = "12345",
                phoneNumber = "123-456-7890",
                addressMaps = "37.7749,-122.4194",
                userPhoto = ""
            ),
            onDismiss = {},
            navController = TODO()
        )
    }
}