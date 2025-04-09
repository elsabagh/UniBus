package com.example.unibus.domain

import com.example.unibus.data.api.Path
import kotlinx.coroutines.flow.Flow

interface GraphHopperRepository {
    fun getRoute(start: String, end: String, apiKey: String): Flow<Result<Path>>

}
