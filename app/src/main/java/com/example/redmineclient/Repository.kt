package com.example.redmineclient

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface Repository {
    suspend fun getIssues(): Result<IssuesData>
    suspend fun getProjects(): Result<ProjectsData>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun bindsRepository(repo: RepositoryImpl): Repository
}
