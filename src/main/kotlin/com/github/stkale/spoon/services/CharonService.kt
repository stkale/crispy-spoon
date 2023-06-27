package com.github.stkale.spoon.services

import com.github.stkale.spoon.domain.Assignment
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CharonService {
    @GET("charons")
    suspend fun getCharons(): Response<List<Assignment>>

    @GET("charons/{charon}")
    suspend fun getCharon(
        @Path("charon") charon: String
    ): Response<Assignment>
}
