package com.example.redessocialesapp.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.util.Screens
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.indendshape.Height

@Composable
fun BottomNavigationMenu(navController: NavController, selectedItem: NavigationBarItems){
    AnimatedNavigationBar(
        modifier = Modifier.height(64.dp),
        selectedIndex = selectedItem.ordinal,
        ballAnimation = Straight(
            tween(300)
        ),
        indentAnimation = Height(
            tween(300)
        ),
        barColor = AppColor,
        ballColor = AppColor,
    ) {
        for (item in NavigationBarItems.values()) {
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = { navController.navigate(item.route.route) },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    imageVector = item.icon,
                    contentDescription = "",
                    tint = if (item==selectedItem) Color.White else Color.LightGray
                )
            }
        }

    }
}

enum class NavigationBarItems(val icon: ImageVector, val route: Screens) {
    Home(icon = Icons.Default.Home, Screens.HomeScreen),
    Notice(icon = Icons.Default.Payment, Screens.NoticeScreen),
    Video(icon = Icons.Default.OndemandVideo, Screens.VideoScreen),
    Profile(icon = Icons.Default.Person, Screens.ProfileScreen)
}
