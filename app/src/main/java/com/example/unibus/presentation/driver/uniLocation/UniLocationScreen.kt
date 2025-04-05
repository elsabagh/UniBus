package com.example.unibus.presentation.driver.uniLocation


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.unibus.presentation.common.TopAppBar
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.utils.fetchLocation
import com.example.unibus.utils.isLocationPermissionGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun UniLocationScreen(navController: NavController) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    var showArrivalButton by remember { mutableStateOf(false) }

    val fixedLatLng = LatLng(30.247764, 31.202864)
    val radius = 150.0f

    Scaffold(
        topBar = { TopAppBar("University Location", navController) },
        content = { paddingValues ->
            Box(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()) {
                MapViewContent(
                    mapView = mapView,
                    fixedLatLng = fixedLatLng,
                    fusedLocationClient = fusedLocationClient,
                    context = context,
                    googleMap = googleMap,
                    radius = radius,
                    showArrivalButton = showArrivalButton,
                    onLocationFetched = { distance ->
                        showArrivalButton = distance <= radius
                    }
                )

                if (showArrivalButton) {
                    ArrivalButton(context,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    )
                }

                LocationFAB(
                    fusedLocationClient,
                    context,
                    googleMap,
                    fixedLatLng,
                    radius,
                    modifier = Modifier
                        .align(Alignment.BottomStart),
                ) { distance ->
                    showArrivalButton = distance <= radius
                }
            }
        }
    )
}

@Composable
fun MapViewContent(
    mapView: MapView,
    fixedLatLng: LatLng,
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    googleMap: GoogleMap?,
    radius: Float,
    showArrivalButton: Boolean,
    onLocationFetched: (distance: Float) -> Unit
) {
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }

    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize(),
        update = { mapView ->
            mapView.getMapAsync { map ->
                googleMap = map
                map.uiSettings.isZoomControlsEnabled = true
                map.addMarker(MarkerOptions().position(fixedLatLng).title("University"))
                if (isLocationPermissionGranted(context)) {
                    fetchLocation(fusedLocationClient, context) { lat, lng ->
                        val currentLatLng = LatLng(lat, lng)
                        map.addCircle(
                            CircleOptions()
                                .center(currentLatLng)
                                .radius(radius.toDouble())
                                .strokeColor(0xFF0000FF.toInt())
                                .fillColor(0x440000FF)
                                .strokeWidth(4f)
                        )
                        val location = android.location.Location("")
                        location.latitude = lat
                        location.longitude = lng
                        val fixedLocation = android.location.Location("")
                        fixedLocation.latitude = fixedLatLng.latitude
                        fixedLocation.longitude = fixedLatLng.longitude
                        val distance = location.distanceTo(fixedLocation)
                        onLocationFetched(distance)
                    }
                }
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(fixedLatLng, 15f))
            }
        }
    )
}

@Composable
fun ArrivalButton(
    context: Context,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp)
                .clickable(onClick = {
                    Toast.makeText(context, "Arrived", Toast.LENGTH_SHORT).show()
                }),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MainColor)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Arrive",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun LocationFAB(
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    googleMap: GoogleMap?,
    fixedLatLng: LatLng,
    radius: Float,
    modifier: Modifier = Modifier,
    onLocationFetched: (distance: Float) -> Unit,
) {
    FloatingActionButton(
        onClick = {
            if (isLocationPermissionGranted(context)) {
                fetchLocation(fusedLocationClient, context) { lat, lng ->
                    val currentLatLng = LatLng(lat, lng)
                    googleMap?.apply {
                        clear()
                        addMarker(MarkerOptions().position(fixedLatLng).title("الجامعة"))
                        addCircle(
                            CircleOptions()
                                .center(currentLatLng)
                                .radius(radius.toDouble())
                                .strokeColor(0xFF0000FF.toInt())
                                .fillColor(0x440000FF)
                                .strokeWidth(4f)
                        )
                        animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                        val location = android.location.Location("")
                        location.latitude = lat
                        location.longitude = lng
                        val fixedLocation = android.location.Location("")
                        fixedLocation.latitude = fixedLatLng.latitude
                        fixedLocation.longitude = fixedLatLng.longitude
                        val distance = location.distanceTo(fixedLocation)
                        onLocationFetched(distance)
                    }
                }
            } else {
                Toast.makeText(context, "اسمح بإذن اللوكيشن الأول", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = modifier
            .padding(16.dp)
            .padding(bottom = 106.dp)
    ) {
        Text("📍")
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        val observer = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) = mapView.onCreate(null)
            override fun onStart(owner: LifecycleOwner) = mapView.onStart()
            override fun onResume(owner: LifecycleOwner) = mapView.onResume()
            override fun onPause(owner: LifecycleOwner) = mapView.onPause()
            override fun onStop(owner: LifecycleOwner) = mapView.onStop()
            override fun onDestroy(owner: LifecycleOwner) = mapView.onDestroy()
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return mapView
}
