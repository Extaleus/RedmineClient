package com.example.redmineclient

import retrofit2.http.GET

private const val FETCH_ISSUES_URL = "issues.json"
private const val FETCH_PROJECTS_URL = "projects.json"

interface RedmineApi {
    @GET(FETCH_ISSUES_URL)
    suspend fun getIssues(): IssuesData
    @GET(FETCH_PROJECTS_URL)
    suspend fun getProjects(): ProjectsData
}
