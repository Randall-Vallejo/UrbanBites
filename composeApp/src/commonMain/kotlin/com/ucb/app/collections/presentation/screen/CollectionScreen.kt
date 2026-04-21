package com.ucb.app.collections.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.collections.presentation.composable.CollectionView
import com.ucb.app.Res
import com.ucb.app.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionScreen( modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState).padding(30.dp, 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(Res.string.explore), fontSize = 34.sp)
                Text("Collections", fontSize = 34.sp, fontWeight = FontWeight.Bold)
            }
            Icon(
                painterResource(Res.drawable.icon_cart),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
            )
        }
        Box(
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
        ) {
            Image(
                painterResource(Res.drawable.children),
                contentDescription = null,
                modifier = Modifier.size(347.dp, 222.dp)

            )
            Button(
                modifier = Modifier.align(
                    Alignment.BottomCenter
                ),
                onClick = {

            }) {
                Text("Children")
            }
        }
        CollectionView(
            Res.drawable.children,
            "Children"
        ) {
            print("Click children collection")
        }

        CollectionView(
            Res.drawable.children,
            "Woman"
        ) {
            print("Click woman collection")
        }

        CollectionView(
            Res.drawable.children,
            "Men"
        ) {
            print("Click men collection")
        }

    }
}
