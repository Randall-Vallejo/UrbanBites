package com.ucb.app.onboarding.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ucb.app.onboarding.presentation.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import com.ucb.app.Res
import com.ucb.app.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = koinViewModel(),
    onFinish: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val language = Locale.current.language // Detecta idioma (es, en, fr)
    
    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (uiState.items.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { uiState.items.size })
        
        LaunchedEffect(pagerState.currentPage) {
            viewModel.onPageChanged(pagerState.currentPage)
        }

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pagerState.currentPage > 0) {
                        IconButton(onClick = {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    } else {
                        Spacer(Modifier.size(48.dp))
                    }

                    TextButton(onClick = onFinish) {
                        Text(stringResource(Res.string.onboarding_skip), color = Color.Gray)
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { index ->
                    val item = uiState.items[index]
                    val title = item.title[language] ?: item.title["en"] ?: ""
                    val description = item.description[language] ?: item.description["en"] ?: ""
                    val imageUrl = item.image_url[language] ?: item.image_url["en"] ?: ""

                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier.size(300.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(Modifier.height(32.dp))
                        Text(
                            text = title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = description,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }

                // Indicadores y Botón
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Page Indicator (Simplified)
                    Row(Modifier.padding(bottom = 24.dp)) {
                        repeat(uiState.items.size) { i ->
                            val color = if (pagerState.currentPage == i) MaterialTheme.colorScheme.primary else Color.LightGray
                            Surface(
                                modifier = Modifier.padding(4.dp).size(8.dp),
                                shape = androidx.compose.foundation.shape.CircleShape,
                                color = color
                            ) {}
                        }
                    }

                    if (pagerState.currentPage == uiState.items.size - 1) {
                        Button(
                            onClick = {
                                viewModel.completeOnboarding()
                                onFinish()
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            Text(stringResource(Res.string.onboarding_start))
                        }
                    } else {
                        Button(
                            onClick = {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(stringResource(Res.string.onboarding_next))
                                Spacer(Modifier.width(8.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                            }
                        }
                    }
                }
            }
        }
    } else {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error cargando Onboarding: ${uiState.error}")
        }
    }
}
