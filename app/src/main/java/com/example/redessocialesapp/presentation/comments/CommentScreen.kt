package com.example.redessocialesapp.presentation.comments

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.redessocialesapp.domain.model.Comment
import com.example.redessocialesapp.presentation.CommentCard
import com.example.redessocialesapp.presentation.ValidateLoginDialog
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.ui.theme.poppinsFontFamily
import com.example.redessocialesapp.util.Screens
import com.google.firebase.Timestamp

@Composable
fun CommentScreen(
    navController: NavController,
    viewModel: CommentViewModel,
    postId: String,
    colecction: String
) {

    val user by viewModel.user.collectAsState()

    val comments by viewModel.comments.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
        viewModel.getComments(colecction, postId)
    }

    val scrollState = rememberScrollState()

    // Variable para mostrar el diálogo de inicio de sesión
    val showValidateDialog = remember { mutableStateOf(false) }

    // Mostrar el diálogo de inicio de sesión
    if (showValidateDialog.value) {
        ValidateLoginDialog(
            showDialog = showValidateDialog.value,
            onDismissRequest = { showValidateDialog.value = false },
            onRegister = { navController.navigate(Screens.SignUpScreen.route) },
            onLogin = { navController.navigate(Screens.LoginScreen.route) {} },
            text = "para comentar una publicación."
        )
    }

    // Variable para mostrar el diálogo de inicio de sesión
    val showCommentDialog = remember { mutableStateOf(false) }

    // Mostrar el diálogo de inicio de sesión
    if (showCommentDialog.value) {
        AddCommentDialog(
            showDialog = showCommentDialog,
            onPost = {
                Log.d("Collection", colecction)
                Log.d("PostId", postId)
                Log.d("User", user.userId)
                viewModel.putComment(
                    colecction,
                    postId,
                    Comment(
                        "",
                        user.userId,
                        user.firstName,
                        user.lastName,
                        user.imageURL,
                        it,
                        Timestamp.now()
                    )
                )
            },
            onCancel = {
                showCommentDialog.value = false
            })
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F1F1)),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (user.userId != "") {
                    showCommentDialog.value = true
                } else {
                    showValidateDialog.value = true

                }
            }, contentColor = Color.White, backgroundColor = AppColor) {
                Icon(Icons.Default.Add, contentDescription = "Add comment")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        content = { innerPadding ->

            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF1F1F1))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(8.dp)) {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "",
                                tint = AppColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)) {
                        Text(
                            text = "Comentarios",
                            fontSize = 20.sp,
                            color = AppColor,
                            fontFamily = poppinsFontFamily,
                            modifier = Modifier.clickable { }
                        )
                    }
                    Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.padding(8.dp)) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                LazyColumn(
                    Modifier
                        .background(Color(0xFFF1F1F1))
                        .padding(horizontal = 16.dp)) {
                    items(comments.size) { it ->
                        CommentCard(
                            comments[it]
                        ) {
                            navController.navigate("profile/${it}") {
                                popUpTo("comment/${postId}") {
                                    inclusive = false
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun AddCommentDialog(
    showDialog: MutableState<Boolean>,
    onPost: (String) -> Unit,
    onCancel: () -> Unit
) {
    val textState = remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Añadir comentario") },
            text = {
                TextField(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    label = { Text("Comentario") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    onPost(textState.value)
                    showDialog.value = false
                }) {
                    Text("Publicar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    onCancel()
                    showDialog.value = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


