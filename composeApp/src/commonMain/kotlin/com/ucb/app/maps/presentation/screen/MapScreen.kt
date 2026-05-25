package com.ucb.app.maps.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ucb.app.home.domain.model.FoodTruck

@Composable
expect fun MapScreen(
    modifier: Modifier = Modifier,
    trucks: List<FoodTruck> = emptyList(),
    onTruckClick: (String) -> Unit = {},
    centerLatitude: Double? = null,
    centerLongitude: Double? = null
)
