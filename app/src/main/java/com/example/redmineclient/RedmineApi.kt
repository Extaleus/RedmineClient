package com.example.redmineclient

import retrofit2.http.GET
import retrofit2.http.Path

private const val FETCH_ISSUES_URL = "issues.json"
private const val FETCH_PROJECTS_URL = "projects.json"
private const val FETCH_ATTACHMENTS_URL = "issues/{issueId}.json?include=attachments"

interface RedmineApi {
    @GET(FETCH_ISSUES_URL)
    suspend fun getIssues(): IssuesData
    @GET(FETCH_PROJECTS_URL)
    suspend fun getProjects(): ProjectsData
    @GET(FETCH_ATTACHMENTS_URL)
    suspend fun getIssueAttachments(@Path("issueId") issueId: Int): IssueData
}
