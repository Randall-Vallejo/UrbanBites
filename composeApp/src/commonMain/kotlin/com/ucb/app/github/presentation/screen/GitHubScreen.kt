package com.ucb.app.github.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ucb.app.github.presentation.viewmodel.GitHubViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GitHubScreen(
    viewModel: GitHubViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = state.username,
            onValueChange = viewModel::onUsernameChange,
            label = { Text("GitHub username") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.searchUser() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }

        state.error?.let { error ->
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        state.user?.let { user ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = "Avatar de ${user.login}",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Login: ${user.login}", style = MaterialTheme.typography.titleMedium)
                    Text("Nombre: ${user.name}")
                    Text("Bio: ${user.bio}")
                    Text("Repos públicos: ${user.publicRepos}")
                    Text("Followers: ${user.followers}")
                    Text("Following: ${user.following}")
                }
            }
        }
    }
}
