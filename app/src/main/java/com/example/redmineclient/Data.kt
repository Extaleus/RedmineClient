package com.example.redmineclient

import com.google.gson.annotations.SerializedName
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import javax.annotation.concurrent.Immutable

@Immutable
data class IssuesData(
    val issues: List<Issue>,
)

@Immutable
data class IssueData(
    val issue: Issue,
)

@Immutable
data class ProfileData(
    val user: User,
)

@Immutable
data class User(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val mail: String,
    @SerializedName(value = "created_on")
    val createdOn: String,
    @SerializedName(value = "last_login_on")
    val lastLoginOn: String,
    val memberships: MutableList<Memberships>? = null
)

@Immutable
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
    @SerializedName(value = "assigned_to")
    val assignedTo: IssueObject? = null,
    val subject: String,
    val description: String,
    @SerializedName(value = "start_date")
    val startDate: String,
    @SerializedName(value = "done_ratio")
    val doneRatio: Int,
    @SerializedName(value = "created_on")
    val createdOn: String,
    @SerializedName(value = "updated_on")
    val updatedOn: String,
    val attachments: List<IssueAttachment>? = null,
    val journals: MutableList<IssueJournals>? = null
)

@Immutable
data class IssueObject(
    val id: Int,
    val name: String,
)

@Immutable
data class IssueAttachment(
    val id: Int,
    val filename: String,
    val filesize: Int,
    @SerializedName(value = "content_type")
    val contentType: String,
    val description: String,
    @SerializedName(value = "content_url")
    val contentUrl: String,
    val author: IssueObject,
    @SerializedName(value = "created_on")
    val createdOn: String
)

@Immutable
data class IssueJournals(
    val id: Int,
    val user: IssueObject,
    val notes: String,
    @SerializedName(value = "created_on")
    val createdOn: String,
    val details: MutableList<IssueJournalDetails>? = null,
)

@Immutable
data class IssueJournalDetails(
    val property: String,
    val name: String,
    @SerializedName(value = "old_value")
    val oldValue: String? = null,
    @SerializedName(value = "new_value")
    val newValue: String? = null
)

data class ProjectsData(
    val projects: MutableList<Project> = mutableListOf(),
)

@Immutable
data class Project(
    val id: Int,
    val name: String,
    val identifier: String,
    val description: String,
    val parent: ProjectParent? = null,
    var subprojects: ProjectsData = ProjectsData(),
    val status: Int,
    @SerializedName(value = "created_on")
    val createdOn: String,
    @SerializedName(value = "updated_on")
    val updatedOn: String,
)

@Immutable
data class ProjectParent(
    val id: Int,
    val name: String,
)

data class AuthViewState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val authSucceededEvent: StateEventWithContent<String> = consumed(),
)

data class ProjectsViewState(
    val projects: MutableList<Project>? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
)

data class IssuesViewState(
    val issues: MutableList<Issue>? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val issuesSucceededEvent: StateEventWithContent<String> = consumed(),
)

data class ProfileViewState(
    val profile: User? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val image: String? = null
)

data class IssueViewState(
    val issue: Issue? = null,
    val isLoading: Boolean = false,
    val nameById: String? = null,
    val message: String? = null
)
