package com.example.redmineclient.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.redmineclient.Issue
import com.example.redmineclient.ui.theme.body
import com.example.redmineclient.ui.theme.textColor
import com.example.redmineclient.viewModels.IssuesViewModel
import de.palm.composestateevents.NavigationEventEffect

@Composable
fun Issues(
    issuesViewModel: IssuesViewModel,
    navController: NavHostController,
    project: String
) {
    val issuesUiState by issuesViewModel.issuesUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        issuesViewModel.setProjectName(project)
    }

    NavigationEventEffect(
        event = issuesUiState.issuesSucceededEvent,
        onConsumed = issuesViewModel::onConsumedIssuesSucceededEvent
    ) {
        navController.navigate("issueinspect/${it}")
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "back",
                    tint = textColor,
                )
            }
            Text(text = "Issues by $project", style = MaterialTheme.typography.bodyMedium)
        }
        if (issuesUiState.isLoading) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        } else {
            issuesUiState.message?.let { Text(text = it) }
            Column {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    issuesUiState.issues?.forEach {
                        issueItemView(it, issuesViewModel, navController)
                    }
                }
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
fun LazyListScope.issueItemView(
    issue: Issue,
    issuesViewModel: IssuesViewModel,
    navController: NavHostController
) {
    item {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .background(body)
                .clickable(onClick = { issuesViewModel.onClickIssue(issue) }),
        ) {
            Row(Modifier.padding(top = 8.dp)) {
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // id
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Id", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = issue.id.toString(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        // tracker
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Tracker", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = issue.tracker.name,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        // status
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Status", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = issue.status.name,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        // priority
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Priority", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = issue.priority.name,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        // done?
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Done?", style = MaterialTheme.typography.bodySmall)
                            Icon(
                                imageVector = if (issue.doneRatio == 0) {
                                    Icons.Rounded.CheckBoxOutlineBlank
                                } else {
                                    Icons.Rounded.CheckBox
                                },
                                contentDescription = "arrow",
                                tint = textColor
                            )
                        }
                    }
                }
            }
            // subject
            Text(
                text = issue.subject,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var image: String? =
                    issue.assignedTo?.name?.lowercase()?.filterNot { it.isWhitespace() }
                image?.let { Log.d("my", it) }
                if (image != "" && image != null) {
                    Image(
                        painterResource(
                            LocalContext.current.resources.getIdentifier(
                                image,
                                "drawable",
                                LocalContext.current.packageName
                            )
                        ),
                        contentDescription = ""
                    )
                } else {
                    image = "vasiliylitvak"
                    Image(
                        painterResource(
                            LocalContext.current.resources.getIdentifier(
                                image,
                                "drawable",
                                LocalContext.current.packageName
                            )
                        ),
                        contentDescription = ""
                    )
                }

                // assignee
                TextButton(onClick = {
                    navController.navigate("profile/${issue.assignedTo?.id}")
                }) {
                    issue.assignedTo?.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                // updated
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Updated: ", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = issue.updatedOn.replace("T", "\n")
                            .replace("Z", " "),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
