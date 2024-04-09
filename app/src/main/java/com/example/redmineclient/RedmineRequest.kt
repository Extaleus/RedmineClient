package com.example.redmineclient

import retrofit2.http.GET
import retrofit2.http.Query

interface RedmineRequest {
    @GET("issues")
    suspend fun getIssues(
        @Query("username") username: String,
        @Query("password") password: String
    ): IssuesData
}