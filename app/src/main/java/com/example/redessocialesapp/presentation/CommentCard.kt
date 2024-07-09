package com.example.redessocialesapp.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.redessocialesapp.domain.model.Comment
import com.example.redessocialesapp.ui.theme.poppinsFontFamily

@Composable
fun CommentCard(item : Comment, onProfileClick: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)){
            Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.padding(end = 10.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.imageURL)
                        .crossfade(true)
                        .transformations(
                            CircleCropTransformation()
                        )
                        .scale(Scale.FIT)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp).clickable { onProfileClick(item.userId) }
                )
            }
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.userFirstName + " " + item.userLastName,
                        fontSize = 18.sp,
                        fontFamily = poppinsFontFamily,
                        modifier = Modifier.padding(top = 10.dp, end = 10.dp),
                        color = Color.Black
                    )
                    Text(
                        text = item.getTimeAgo(item.date),
                        fontSize = 12.sp,
                        fontFamily = poppinsFontFamily,
                        modifier = Modifier.padding(top = 10.dp),
                        color = Color.Gray
                    )
                }
                Text(
                    text = item.text,
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    modifier = Modifier.padding(top = 10.dp),
                    color = Color.Black
                )
            }
        }
    }
}