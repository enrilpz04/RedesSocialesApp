package com.example.redessocialesapp.presentation.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.ui.theme.poppinsFontFamily

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel,
) {

    viewModel.loadData()

    val user by viewModel.user.collectAsState()

    val firsName = remember {
        mutableStateOf("")
    }

    val lastName = remember {
        mutableStateOf("")
    }

    val email = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(user) {
        firsName.value = user.firstName
        lastName.value = user.lastName
        email.value = user.email
    }


    if (!isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Box(contentAlignment = Alignment.CenterStart) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "",
                            tint = AppColor,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Editar Perfil",
                        fontSize = 20.sp,
                        color = AppColor,
                        fontFamily = poppinsFontFamily,
                        modifier = Modifier.clickable { }
                    )
                }
                Box(contentAlignment = Alignment.CenterEnd) {
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
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.imageURL)
                    .crossfade(true)
                    .transformations(
                        CircleCropTransformation()
                    )
                    .scale(Scale.FIT)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(130.dp)
            )
            Text(
                text = "Cambiar foto",
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = poppinsFontFamily,
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column(Modifier.padding(horizontal = 16.dp)) {
                Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Nombre",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = poppinsFontFamily,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                EditProfileTextField(
                    value = firsName,
                )
                Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Apellidos",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = poppinsFontFamily,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                EditProfileTextField(
                    value = lastName,
                )
                Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Correo Electrónico",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = poppinsFontFamily,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                EditProfileTextField(
                    value = email,
                )
                Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Password",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = poppinsFontFamily,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                EditProfileTextField(
                    value = password,
                )
                Spacer(modifier = Modifier.height(64.dp))
                TextButton(
                    onClick = {
                        viewModel.updateUser(
                            firsName.value,
                            lastName.value,
                            email.value,
                            password.value
                        )
                    }, colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = AppColor,
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Guardar cambios",
                        color = Color.White,
                        fontFamily = poppinsFontFamily,
                        fontSize = 20.sp
                    )

                }
            }


        }
    }
}

@Composable
fun EditProfileTextField(
    value: MutableState<String>,
) {
    OutlinedTextField(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = Modifier.fillMaxWidth()
    )
}