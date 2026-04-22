package com.ucb.app.collections.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import urbanbites.com.Res
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CollectionView(
    resource: DrawableResource,
    buttonTitle: String,
    buttonAction : () -> Unit
) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(16.dp))
    ) {
        Image(
            painterResource(resource),
            contentDescription = null,
            modifier = Modifier.size(347.dp, 222.dp)

        )
        OutlinedButton(
            modifier = Modifier.align(
                Alignment.BottomCenter
            ),
            onClick = {
                buttonAction()
            }) {
            Text(buttonTitle)
        }
    }

}
