package com.example.redmineclient

data class IssuesData(
    val issues: List<Issue>,
)

data class Issue(
    val id: Int,
    val project: IssueObject,
    val tracker: IssueObject,
    val status: IssueObject,
    val priority: IssueObject,
    val author: IssueObject,
    val assigned_to: IssueObject,
    val subject: String,
    val description: String,
    val start_date: String,
    val done_ratio: Int,
    val created_on: String,
    val updated_on: String,
)

data class IssueObject(
    val id: Int,
    val name: String,
)

data class ProjectsData(
    val projects: MutableList<Project>,
)

data class Project(
    val id: Int,
    val name: String,
    val identifier: String,
    val description: String,
    val parent: ProjectParent? = null,
    val subprojects: ProjectsData,
    val status: Int,
    val created_on: String,
    val updated_on: String,
)

data class ProjectParent(
    val id: Int,
    val name: String,
)

data class AuthPageInfo(
    val isLoading: Boolean = false,
    val message: String = "",
)

data class ProjectsPageInfo(
    val projects: ProjectsData? = null,
    val isLoading: Boolean = false,
    val message: String = "",
)