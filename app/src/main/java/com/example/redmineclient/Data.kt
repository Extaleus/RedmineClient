package com.example.redmineclient

data class IssuesData(
    val issues: List<Issue>,
)

data class IssueData(
    val issue: Issue,
)

data class ProfileData(
    val user: User,
)

data class User(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val mail: String,
    val created_on: String,
    val last_login_on: String,
    val memberships: MutableList<Memberships>? = null
)

data class Memberships(
    val id: Int,
    val project: IssueObject,
    val roles: MutableList<IssueObject>? = null
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
    val attachments: List<IssueAttachment>? = null,
    val journals: MutableList<IssueJournals>? = null
)

data class IssueObject(
    val id: Int,
    val name: String,
)

data class IssueAttachment(
    val id: Int,
    val filename: String,
    val filesize: Int,
    val content_type: String,
    val description: String,
    val content_url: String,
    val author: IssueObject,
    val created_on: String
)

//data class IssueJournal(
//    val
//)

data class IssueJournals(
    val id: Int,
    val user: IssueObject,
    val notes: String,
    val created_on: String,
    val details: MutableList<IssueJournalDetails>? = null,
)

data class IssueJournalDetails(
    val property: String,
    val name: String,
    val old_value: String? = null,
    val new_value: String? = null
)

data class ProjectsData(
    val projects: MutableList<Project> = mutableListOf(),
)

data class Project(
    val id: Int,
    val name: String,
    val identifier: String,
    val description: String,
    val parent: ProjectParent? = null,
    var subprojects: ProjectsData = ProjectsData(),
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
    val projects: MutableList<Project>? = null,
    val isLoading: Boolean = false,
    val message: String = "",
)

data class IssuesPageInfo(
    val issues: MutableList<Issue>? = null,
    val isLoading: Boolean = false,
    val message: String = "",
)

data class ProfilePageInfo(
    val profile: User? = null,
    val isLoading: Boolean = false,
    val message: String = "",
    val image: String? = null
)

data class IssueInfo(
    val issue: Issue? = null,
    val isLoading: Boolean = false,
)

// attr
// assigned_to_id
//
// attachment
// 26926
//
// "property": "attr",
// "name": "status_id",
// "old_value": "1",
// "new_value": "12"
//
// "property": "attr",
// "name": "done_ratio",
// "old_value": "0",
// "new_value": "100"