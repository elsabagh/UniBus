package com.example.unibus.data.api

import android.content.Context
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

fun drawRouteBetweenTwoPoints(
    context: Context,
    map: GoogleMap,
    origin: LatLng,
    destination: LatLng,
    apiKey: String
) {
    val client = OkHttpClient()
    val url = "https://maps.googleapis.com/maps/api/directions/json?" +
            "origin=${origin.latitude},${origin.longitude}" +
            "&destination=${destination.latitude},${destination.longitude}" +
            "&key=$apiKey"

    CoroutineScope(Dispatchers.IO).launch {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val json = JSONObject(response.body!!.string())
            val routes = json.getJSONArray("routes")

            if (routes.length() > 0) {
                val overviewPolyline = routes
                    .getJSONObject(0)
                    .getJSONObject("overview_polyline")
                    .getString("points")

                val points = PolyUtil.decode(overviewPolyline)

                withContext(Dispatchers.Main) {
                    map.addPolyline(
                        PolylineOptions()
                            .addAll(points)
                            .color(0xFF1E88E5.toInt()) // لون أزرق
                            .width(10f)
                    )
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "لم يتم العثور على مسار", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
