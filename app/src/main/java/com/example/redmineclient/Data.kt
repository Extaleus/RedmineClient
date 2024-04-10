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
    /*
        "issues": [
    {
        "id": 22929,
        "project": {
        "id": 831,
        "name": "Office Productivity"
    },
        "tracker": {
        "id": 4,
        "name": "Task"
    },
        "status": {
        "id": 13,
        "name": "Completed"
    },
        "priority": {
        "id": 5,
        "name": "High"
    },
        "author": {
        "id": 286,
        "name": "Daniil Chubiy"
    },
        "assigned_to": {
        "id": 286,
        "name": "Daniil Chubiy"
    },
        "subject": "Российский VPN через Hetzner для Игошева",
        "description": "",
        "start_date": "2024-01-29",
        "done_ratio": 0,
        "created_on": "2024-01-29T10:35:09Z",
        "updated_on": "2024-01-29T10:35:09Z"
    },
    */
)

data class IssueObject(
    val id: Int,
    val name: String,
)