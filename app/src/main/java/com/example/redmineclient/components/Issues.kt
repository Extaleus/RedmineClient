package com.example.redmineclient.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.redmineclient.Issue
import com.example.redmineclient.IssuesPageInfo
import com.example.redmineclient.viewModels.IssuesViewModel

@Composable
fun Issues(issuesViewModel: IssuesViewModel, issuesUiState: IssuesPageInfo, project: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Issues by $project", fontSize = 24.sp)
        Row(
            Modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier.weight(0.8f), horizontalArrangement = Arrangement.SpaceAround
            ) {
                // id
                Text(text = "issue_id")
                // tracker
                Text(text = "tracker")
                // status
                Text(text = "status")
                // priority
                Text(text = "priority")
            }
            Row(Modifier.weight(0.2f), horizontalArrangement = Arrangement.Center) {
                // done?
                Text(text = "done?")
            }
        }
        if (issuesUiState.isLoading) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                issuesUiState.issues?.forEach {
                    issueItemView(it)
                }
            }
        }
    }
}

fun LazyListScope.issueItemView(issue: Issue) {
    item {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp)
                .background(Color(0.5f, 0.5f, 0.3f, 0.5f)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // id
                    Text(text = issue.id.toString())
                    // tracker
                    Text(text = issue.tracker.name)
                    // status
                    Text(text = issue.status.name)
                    // priority
                    Text(text = issue.priority.name)
                }
                // subject
                Text(text = issue.subject, Modifier.padding(10.dp))
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // assignee
                    Text(
                        text = issue.assigned_to.name,
                        fontStyle = FontStyle.Italic,
                        fontSize = 18.sp
                    )
                    // updated
                    Column {
                        Text(text = "Updated:")
                        Text(text = issue.updated_on.replace("T", "\n").replace("Z", " "))
                    }
                }
            }
            // done
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = if (issue.done_ratio == 0) {
                    Icons.Rounded.CheckBoxOutlineBlank
                } else {
                    Icons.Rounded.CheckBox
                },
                contentDescription = "arrow",
                tint = Color.White
            )
        }
    }
}

//@Composable
//fun Issues() {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(text = "ISSUES by Office Productivity", fontSize = 24.sp)
//        Row(
//            Modifier.fillMaxWidth(),
//        ) {
//            Row(
//                Modifier.weight(0.8f), horizontalArrangement = Arrangement.SpaceAround
//            ) {
//                // id
//                Text(text = "issue_id")
//                // tracker
//                Text(text = "tracker")
//                // status
//                Text(text = "status")
//                // priority
//                Text(text = "priority")
//            }
//            Row(Modifier.weight(0.2f), horizontalArrangement = Arrangement.Center) {
//                // done?
//                Text(text = "done?")
//            }
//        }
//
//        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            issueItemView()
//        }
//    }
//
//}
//
//@Preview(showBackground = true)
//@Composable
//fun Issues_Preview() {
//    Issues()
//}