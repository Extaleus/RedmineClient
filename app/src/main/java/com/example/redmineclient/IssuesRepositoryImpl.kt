package com.example.redmineclient

import javax.inject.Inject

class IssuesRepositoryImpl @Inject constructor(
    private val redmineApi: RedmineApi
) : IssuesRepository {
    override suspend fun getIssues(): Result<IssuesData> {
        return runCatching {
            redmineApi.getIssues()
        }
    }
}