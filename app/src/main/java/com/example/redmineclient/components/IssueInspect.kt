package com.example.redmineclient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.redmineclient.Issue
import com.example.redmineclient.viewModels.IssueInspectViewModel

@Composable
fun IssueInspect(issuesIssuesViewModel: IssueInspectViewModel, issue: Issue) {
    val issueInspectUiState by issuesIssuesViewModel.issueInspectUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        issuesIssuesViewModel.setIssueId(issue.id)
    }

    if (issueInspectUiState.isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = issue.tracker.name + " " + "#" + issue.id.toString(),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
            Column(
                Modifier
                    .weight(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = issue.subject,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                        .background(Color(0xFF649D4C)),
                )
                Text(
                    text = issue.description,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .background(Color(0xFF396B49)),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "Attachments:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
            Column(
                Modifier
                    .weight(0.3f)
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFF396B49))
            ) {
                issueInspectUiState.issue?.attachments?.forEach {
                    TextButton(onClick = {
                        issuesIssuesViewModel.downloadFile(
                            context,
                            it.content_url,
                            it.content_type,
                            it.filename
                        )
                    }, modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp)) {
                        Text(text = "\t" + it.filename + "\n" + it.content_url)
                    }
                }
            }
        }
    }

//    Text(text = issue?.description.toString())

//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(text = "Issues by $project", style = MaterialTheme.typography.bodyMedium)
//        if (issuesUiState.isLoading) {
//            Row(
//                modifier = Modifier.fillMaxSize(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center,
//            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier.width(64.dp),
//                    color = MaterialTheme.colorScheme.secondary,
//                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
//                )
//            }
//        } else {
//            Column {
//                Text(text = issuesUiState.message, style = MaterialTheme.typography.bodyMedium)
//                LazyColumn(
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    issuesUiState.issues?.forEach {
//                        issueItemView(it)
//                    }
//                }
//            }
//        }
//    }
}

//fun LazyListScope.issueItemView(issue: Issue) {
//    item {
//        Column(
//            modifier = Modifier
//                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
//                .background(Color(0xFF396B49)),
//        ) {
//            Row(Modifier.padding(top = 8.dp)) {
//                Column(
//                    Modifier.fillMaxWidth()
//                ) {
//                    Row(
//                        Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceAround
//                    ) {
//                        // id
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text(text = "Id", style = MaterialTheme.typography.bodySmall)
//                            Text(
//                                text = issue.id.toString(),
//                                style = MaterialTheme.typography.bodySmall
//                            )
//                        }
//                        // tracker
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text(text = "Tracker", style = MaterialTheme.typography.bodySmall)
//                            Text(
//                                text = issue.tracker.name,
//                                style = MaterialTheme.typography.bodySmall
//                            )
//                        }
//                        // status
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text(text = "Status", style = MaterialTheme.typography.bodySmall)
//                            Text(
//                                text = issue.status.name,
//                                style = MaterialTheme.typography.bodySmall
//                            )
//                        }
//                        // priority
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text(text = "Priority", style = MaterialTheme.typography.bodySmall)
//                            Text(
//                                text = issue.priority.name,
//                                style = MaterialTheme.typography.bodySmall
//                            )
//                        }
//                        // done?
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text(text = "Done?", style = MaterialTheme.typography.bodySmall)
//                            Icon(
//                                imageVector = if (issue.done_ratio == 0) {
//                                    Icons.Rounded.CheckBoxOutlineBlank
//                                } else {
//                                    Icons.Rounded.CheckBox
//                                },
//                                contentDescription = "arrow",
//                                tint = Color(0xFF7DF9FD)
//                            )
//                        }
//                    }
//                }
//            }
//            // subject
//            Text(
//                text = issue.subject,
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.padding(8.dp)
//            )
//            Row(
//                Modifier
//                    .fillMaxSize()
//                    .padding(bottom = 8.dp),
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // assignee
//                Text(
//                    text = issue.assigned_to.name,
//                    style = MaterialTheme.typography.bodySmall
//                )
//                // updated
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(text = "Updated: ", style = MaterialTheme.typography.bodySmall)
//                    Text(
//                        text = issue.updated_on.replace("T", "\n")
//                            .replace("Z", " "),
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
//            }
//        }
//    }
//}


//@Composable
//fun Issues(issuesUiState: IssuesPageInfo) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(text = "Issues by Office Productivity", style = MaterialTheme.typography.bodyMedium)
//        if (issuesUiState.isLoading) {
//            Row(
//                modifier = Modifier.fillMaxSize(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier.width(64.dp),
//                    color = MaterialTheme.colorScheme.secondary,
//                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
//                )
//            }
//        } else {
//            LazyColumn(modifier = Modifier.fillMaxSize()) {
//                issuesUiState.issues?.forEach {
//                    issueItemView(it)
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun Issues_Preview() {
//    Issues(
//        IssuesPageInfo(
//            mutableListOf(
//                Issue(
//                    22555,
//                    IssueObject(0, ""),
//                    IssueObject(0, "Task"),
//                    IssueObject(0, "Completed"),
//                    IssueObject(0, "High"),
//                    IssueObject(0, ""),
//                    IssueObject(0, "Daniil Chubiy"),
//                    "",
//                    "Dfkadskhgsd fkgsdftky erty sdfgk sdfkg sdfgl",
//                    "29243493934",
//                    0,
//                    "",
//                    ""
//                )
//            ), false
//        )
//    )
//}