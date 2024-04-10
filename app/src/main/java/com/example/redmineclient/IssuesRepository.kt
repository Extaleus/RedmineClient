package com.example.redmineclient

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface IssuesRepository {
    suspend fun getIssues(): Result<IssuesData>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun bindsIssuesRepository(repo: IssuesRepositoryImpl): IssuesRepository
}
