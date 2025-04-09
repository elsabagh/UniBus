package com.example.unibus.data.repository

import com.example.unibus.data.api.GraphHopperApiService
import com.example.unibus.data.api.Path
import com.example.unibus.domain.GraphHopperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GraphHopperRepositoryImpl @Inject constructor(
    private val api: GraphHopperApiService
) : GraphHopperRepository {

    override fun getRoute(start: String, end: String, apiKey: String): Flow<Result<Path>> = flow {
        try {
            val response = api.getRoute(apiKey, start, end)
            val path = response.paths.firstOrNull()
            if (path != null) {
                emit(Result.success(path))
            } else {
                emit(Result.failure(Exception("No route found")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

}