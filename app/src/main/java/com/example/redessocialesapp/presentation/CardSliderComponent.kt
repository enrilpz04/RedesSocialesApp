package com.example.redessocialesapp.presentation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.redessocialesapp.domain.model.Article
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun CardSliderComponent(
    articlesListFlow: Flow<List<Article>>,
    favArticlesFlow: Flow<List<Article>>,
    userLoged: Boolean,
    onFavoriteClick: (Article, Boolean) -> Unit,
    onDetailClick: (Article) -> Unit
) {

    val articlesList by articlesListFlow.collectAsState(initial = emptyList())
    val favArticlesList by favArticlesFlow.collectAsState(initial = emptyList())


    val scope = rememberCoroutineScope()
    var index = remember {
        mutableStateOf(0)
    }


    var isFavorite = remember {
        mutableStateOf(false)
    }

    if (articlesList.isNotEmpty()) {
        LaunchedEffect(index.value) {
            if (favArticlesList.contains(articlesList.get(index.value))) {
                isFavorite.value = true
            } else {
                isFavorite.value = false
            }
        }

        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            // Ensure the HorizontalPager doesn't take more height than needed
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight() // Ensures that the Box only takes up the height needed by its content
            ) {

                ArticleCard(item = articlesList.get(index.value), isFavorite.value, userLoged, true, {
                    isFavorite.value = !isFavorite.value
                    onFavoriteClick(articlesList.get(index.value), isFavorite.value)
                }, {
                    onDetailClick(articlesList.get(index.value))
                })
            }
        }
    }

    Row(
        Modifier
            .height(50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                if (index.value == 0) {
                    index.value = articlesList.size - 1
                } else {
                    index.value--
                }
            }
        ) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }

        repeat(articlesList.size) { index2 ->
            val color = if (index.value == index2) Color.Blue else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .size(10.dp)
                    .background(color)
            )
        }

        IconButton(
            onClick = {

                if (index.value == articlesList.size - 1) {
                    index.value = 0
                } else {
                    index.value++
                }

            }
        ) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
        }
    }


    LaunchedEffect(Unit) {
        scope.launch {
            while (true) {
                var previousIndex = index.value
                delay(5000) // Wait 3 seconds
                if (previousIndex == index.value) {
                    if (index.value != articlesList.size - 1) {
                        index.value++
                    } else {
                        index.value = 0
                    }
                }


            }
        }
    }
}





