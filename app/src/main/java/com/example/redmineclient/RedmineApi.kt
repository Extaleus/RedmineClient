package com.example.redmineclient

import retrofit2.http.GET

private const val FETCH_LESSONS_URL = "issues.json"

interface RedmineApi {
    @GET("$BASE_URL$FETCH_LESSONS_URL")
    suspend fun getIssues(): IssuesData
}