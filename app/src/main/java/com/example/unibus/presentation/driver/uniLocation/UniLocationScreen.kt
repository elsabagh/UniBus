package com.example.unibus.presentation.driver.uniLocation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.unibus.presentation.common.TopAppBar
import com.example.unibus.utils.fetchLocation
import com.example.unibus.utils.isLocationPermissionGranted
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun UniLocationScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }

    val fixedLatLng = LatLng(30.0444, 31.2357) // المكان الثابت (مثلاً الجامعة)

    Scaffold(
        topBar = { TopAppBar("الخريطة", navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // الخريطة
                AndroidView(
                    factory = { mapView },
                    modifier = Modifier.fillMaxSize(),
                    update = { mapView ->
                        mapView.getMapAsync { map ->
                            googleMap = map
                            map.uiSettings.isZoomControlsEnabled = true

                            map.addMarker(
                                MarkerOptions()
                                    .position(fixedLatLng)
                                    .title("موقع ثابت")
                            )
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(fixedLatLng, 15f))
                        }
                    }
                )

                // زر عائم في الأسفل الشمال
                FloatingActionButton(
                    onClick = {
                        if (isLocationPermissionGranted(context)) {
                            fetchLocation(fusedLocationClient, context) { lat, lng ->
                                val currentLatLng = LatLng(lat, lng)
                                googleMap?.apply {
                                    clear()

                                    addMarker(
                                        MarkerOptions()
                                            .position(fixedLatLng)
                                            .title("الجامعة")
                                    )

                                    addCircle(
                                        CircleOptions()
                                            .center(currentLatLng)
                                            .radius(20.0)
                                            .strokeColor(0xFF0000FF.toInt())
                                            .fillColor(0x440000FF)
                                            .strokeWidth(4f)
                                    )

                                    animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                                }
                            }
                        } else {
                            Toast.makeText(context, "اسمح بإذن اللوكيشن الأول", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text("📍")
                }
            }
        }
    )
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

