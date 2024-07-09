package com.example.redessocialesapp.presentation.authentication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.redessocialesapp.R
import com.example.redessocialesapp.presentation.Toast
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.ui.theme.poppinsFontFamily
import com.example.redessocialesapp.util.Response
import com.example.redessocialesapp.util.Screens

@Composable
fun SignUpScreen(navController: NavHostController, viewModel: AuthenticationViewModel) {

    val firstNameState = remember {
        mutableStateOf("")
    }
    val lastNameState = remember {
        mutableStateOf("")
    }
    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val buttonEnabled = remember(emailState.value, passwordState.value) {
        emailState.value.trim().isNotEmpty() && passwordState.value.trim().isNotEmpty()
    }

    val loading = rememberSaveable { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (loading.value) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(),
                    color = AppColor
                )
            }
        } else {


            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = 0.45f),
                        painter = painterResource(id = R.drawable.shape),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                    Column(Modifier.fillMaxWidth()) {
                        Row(Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .weight(0.2f)
                                    .padding(top = 8.dp, start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                IconButton(
                                    onClick = {
                                        navController.popBackStack()
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowBack,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Image(
                                    modifier = Modifier
                                        .size(240.dp)
                                        .wrapContentWidth(),
                                    painter = painterResource(id = R.drawable.rs_logo),
                                    contentDescription = ""
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(0.2f)
                                    .padding(top = 8.dp, end = 8.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 60.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = "Registrarse",
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 35.sp,
                                color = Color(0xFF1863c8),
                                fontFamily = poppinsFontFamily
                            )
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    OutlinedTextField(
                        value = firstNameState.value,
                        onValueChange = { firstNameState.value = it },
                        label = {
                            Text(
                                text = "Nombre",
                                fontFamily = poppinsFontFamily
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .padding(bottom = 10.dp, start = 24.dp, end = 24.dp)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedLabelColor = Color(0xFF1863c8),
                            focusedIndicatorColor = Color(0xFF1863c8),
                            backgroundColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = lastNameState.value,
                        onValueChange = { lastNameState.value = it },
                        label = {
                            Text(
                                text = "Apellidos",
                                fontFamily = poppinsFontFamily
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .padding(bottom = 10.dp, start = 24.dp, end = 24.dp)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedLabelColor = Color(0xFF1863c8),
                            focusedIndicatorColor = Color(0xFF1863c8),
                            backgroundColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = { emailState.value = it },
                        label = {
                            Text(
                                text = "Correo Electrónico",
                                fontFamily = poppinsFontFamily
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .padding(bottom = 10.dp, start = 24.dp, end = 24.dp)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedLabelColor = Color(0xFF1863c8),
                            focusedIndicatorColor = Color(0xFF1863c8),
                            backgroundColor = Color.White
                        )
                    )

                    val visualTransformation = if (passwordVisible.value)
                        VisualTransformation.None
                    else PasswordVisualTransformation()

                    OutlinedTextField(
                        value = passwordState.value,
                        onValueChange = { passwordState.value = it },
                        label = {
                            Text(
                                text = "Contraseña",
                                fontFamily = poppinsFontFamily
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier
                            .padding(bottom = 10.dp, start = 24.dp, end = 24.dp)
                            .fillMaxWidth(),
                        visualTransformation = visualTransformation,
                        trailingIcon = {
                            if (passwordState.value.isNotBlank()) {
                                PasswordVisibleIcon(
                                    passwordVisible
                                )
                            } else null
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedLabelColor = Color(0xFF1863c8),
                            focusedIndicatorColor = Color(0xFF1863c8),
                            backgroundColor = Color.White
                        )
                    )
                    TextButton(
                        onClick = {
                            viewModel.signUp(
                                firstName = firstNameState.value,
                                lastName = lastNameState.value,
                                email = emailState.value,
                                password = passwordState.value
                            )
                        },
                        modifier = Modifier
                            .padding(bottom = 8.dp, end = 24.dp, start = 24.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            backgroundColor = AppColor
                        )
                    ) {
                        Text(
                            text = "Registrarse",
                            fontSize = 18.sp,
                            fontFamily = poppinsFontFamily
                        )
                        when (val response = viewModel.signUpState.value) {
                            is Response.Success -> {
                                if (response.data) {
                                    navController.navigate(Screens.HomeScreen.route) {
                                        popUpTo(Screens.SignUpScreen.route) {
                                            inclusive = false
                                        }
                                    }
                                    loading.value = false
                                } else {
                                    Toast(message = "Sign Up Failed")
                                }
                            }

                            is Response.Error -> {
                                Toast(message = response.message)
                            }

                            else -> {}
                        }

                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 24.dp, start = 24.dp, top = 20.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Ya tienes una cuenta?")
                        Text(
                            text = "Inicia sesión",
                            color = AppColor,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .clickable {
                                    navController.navigate(route = Screens.LoginScreen.route) {
                                        popUpTo(Screens.SignUpScreen.route) {
                                            inclusive = false
                                        }
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}
