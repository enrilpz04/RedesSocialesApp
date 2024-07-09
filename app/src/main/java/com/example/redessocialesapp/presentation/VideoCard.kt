package com.example.redessocialesapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.redessocialesapp.domain.model.Video
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.ui.theme.poppinsFontFamily

@Composable
fun VideoCard(
    item: Video,
    isFavorite: Boolean,
    userLoged: Boolean,
    letModify: Boolean,
    onFavoriteClick: (Video) -> Unit,
    onDetailClick: (Video) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onDetailClick(item)
            }
    ) {
        Column {
            Box() {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.imageURL)
                        .crossfade(true)
                        .transformations(
                            listOf(
                                RoundedCornersTransformation(10f)
                            )
                        )
                        .scale(Scale.FILL)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(150.dp)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.7f)) // Establece el color de fondo
                ) {
                    Text(
                        text = item.getTimeAgo(item.date),
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(4.dp),
                        fontSize = 12.sp,
                        fontFamily = poppinsFontFamily
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = "",
                            modifier = Modifier
                                .size(18.dp)
                                .padding(start = 4.dp),
                            tint = Color.White
                        )
                        Text(
                            text = item.category,
                            color = Color.White,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(4.dp),
                            fontSize = 12.sp,
                            fontFamily = poppinsFontFamily
                        )


                    }
                }

            }
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 6.dp, top = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(13.dp),
                                tint = AppColor
                            )
                            Text(
                                text = item.location,
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(start = 2.dp),
                                fontSize = 12.sp,
                                fontFamily = poppinsFontFamily,
                                color = AppColor
                            )
                        }

                    }
                    if (letModify){
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            IconButton(onClick = {
                                onFavoriteClick(item)
                            }) {

                                Icon(
                                    imageVector = if (isFavorite && userLoged) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(20.dp),
                                    tint = if (isFavorite && userLoged) Color.Red else Color.Black
                                )
                            }

                        }
                    }


                }

            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 16.dp
                ),
                fontSize = 16.sp,
                fontFamily = poppinsFontFamily
            )
        }
    }
}