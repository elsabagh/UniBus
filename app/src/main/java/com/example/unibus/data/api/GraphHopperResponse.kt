package com.example.unibus.data.api

data class GraphHopperResponse(
    val paths: List<Path>
)

data class Path(
    val distance: Double, // المسافة بالمتر
    val time: Long  // الزمن بالملي ثانية
)
