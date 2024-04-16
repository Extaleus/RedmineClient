package com.example.redmineclient.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.redmineclient.viewModels.ProfileViewModel

@SuppressLint("DiscouragedApi")
@Composable
fun Profile(profileViewModel: ProfileViewModel, userId: Int) {
    val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()
    Log.d("my", "From profile: user id = $userId")

    LaunchedEffect(Unit) {
        profileViewModel.setUserId(userId)
    }

    if (profileUiState.isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    } else {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (profileUiState.image != null) {
                    val imageId = LocalContext.current.resources.getIdentifier(
                        profileUiState.image, "drawable", LocalContext.current.packageName
                    )
                    Image(
                        painterResource(imageId),
                        contentDescription = "",
                        Modifier.size(64.dp)
                    )
                } else {
                    Text("NO IMAGE, CHECK IMAGE ID")
                }
                Text(
                    text = "${profileUiState.profile?.firstname} ${profileUiState.profile?.lastname}",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(
                text = "Mail: ${profileUiState.profile?.mail}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Created_on: ${
                    profileUiState.profile?.created_on?.replace("T", " ")?.replace("Z", "")
                }",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            profileUiState.profile?.memberships?.forEach {
                Text(
                    text = "Id: ${it.id}, Project: ${it.project.name}\nRoles:",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
                it.roles?.forEach { roles ->
                    Text(
                        text = roles.name,
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

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