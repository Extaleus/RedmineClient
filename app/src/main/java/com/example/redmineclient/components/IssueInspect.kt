package com.example.redmineclient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.redmineclient.Issue
import com.example.redmineclient.ui.theme.body
import com.example.redmineclient.ui.theme.textColor
import com.example.redmineclient.ui.theme.title
import com.example.redmineclient.viewModels.IssueInspectViewModel

@Composable
fun IssueInspect(
    navController: NavHostController,
    issue: Issue
) {
    val issueInspectViewModel = hiltViewModel<IssueInspectViewModel>()
    val issueInspectUiState by issueInspectViewModel.issueInspectUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        issueInspectViewModel.setIssueId(issue.id)
    }

    if (issueInspectUiState.isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    } else {
        issueInspectUiState.message?.let { Text(text = it) }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically){
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "back",
                        tint = textColor,
                    )
                }
                Text(
                    text = issue.tracker.name + " " + "#" + issue.id.toString(),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }
            Column(
                Modifier
                    .weight(0.5f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = issue.subject,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                        .background(title),
                )
                Text(
                    text = issue.description,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .background(body),
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
                    .weight(0.2f)
                    .verticalScroll(rememberScrollState())
                    .background(body)
                    .fillMaxWidth()
//                    .padding(start = 8.dp, end = 8.dp)
            ) {
                issueInspectUiState.issue?.attachments?.forEach {
                    TextButton(onClick = {
                        issueInspectViewModel.downloadFile(
                            context, it.contentUrl, it.contentType, it.filename
                        )
                    }, modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp)) {
                        Text(text = "\t" + it.filename + "\n" + it.contentUrl)
                    }
                }
            }
            TextButton(
                onClick = {
                    navController.navigate("profile/${issue.assignedTo?.id}")
                },
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .weight(0.1f),
            ) {
                issue.assignedTo?.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Column(
                Modifier
                    .weight(0.2f)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                    .background(body),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Journals", style = MaterialTheme.typography.bodyMedium)
                issueInspectUiState.issue?.journals?.forEach { journal ->
                    Text(
                        text = "Updated by ${journal.user.name} at ${
                            journal.createdOn.replace(
                                "T",
                                " "
                            ).replace("Z", "")
                        }",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    journal.details?.forEach { detail ->
                        when (detail.property) {
                            "attr" -> {
                                when (detail.name) {
                                    "status_id" -> {
                                        Text(
                                            text = "Status changed from ${issueInspectViewModel.issueStatus[detail.oldValue?.toInt()!!]}" +
                                                    " to ${issueInspectViewModel.issueStatus[detail.newValue?.toInt()!!]}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    "assigned_to_id" -> {
                                        if (journal.user.id == detail.newValue?.toInt()) {
                                            Text(
                                                text = "Assignee set to ${journal.user.name}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        } else {
                                            issueInspectViewModel.setUserId(
                                                issue,
                                                detail.newValue?.toInt()!!
                                            )
                                            Text(
                                                text = issueInspectUiState.nameById.toString(),
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }

                                    "done_ratio" -> {
                                        Text(
                                            text = "% Done changed from ${detail.oldValue} to ${detail.newValue}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }

                            "attachment" -> {
                                Text(
                                    text = "File ${detail.newValue} added",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
