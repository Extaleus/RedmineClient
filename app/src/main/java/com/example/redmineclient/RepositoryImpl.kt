package com.example.redmineclient

import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val redmineApi: RedmineApi
) : Repository {
    override suspend fun getIssues(): Result<IssuesData> {
        return runCatching {
            redmineApi.getIssues()
        }
    }

    override suspend fun getProjects(): Result<ProjectsData> {
        return runCatching {
            redmineApi.getProjects()
        }
    }
}