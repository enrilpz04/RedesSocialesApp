package com.example.redessocialesapp.presentation.authentication

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(navController: NavHostController, viewModel: AuthenticationViewModel) {

    viewModel.checkIfUserIsSignedIn()

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

    val authState by viewModel.firebaseAuthState.collectAsState()

    LaunchedEffect(authState) {
        if (authState) {
            navController.navigate(Screens.HomeScreen.route)
        }
    }



    val token = "916115195283-h8cf2e7uo13giirsc4cfvhh870iq2df9.apps.googleusercontent.com"
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                viewModel.googleSignIn(credential)
                navController.navigate(Screens.HomeScreen.route)
            } catch (ex: Exception) {
                Log.d("RedesSocialesApp", "Error")
            }
        }

    Surface(modifier = Modifier.fillMaxSize()) {
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
                            text = "Iniciar Sesión",
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
                        .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth(),
                    visualTransformation = visualTransformation,
                    trailingIcon = {
                        if (passwordState.value.isNotBlank()) {
                            PasswordVisibleIcon(passwordVisible)
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
                        viewModel.signIn(emailState.value, passwordState.value)
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
                    Text(text = "Iniciar Sesión", fontSize = 18.sp, fontFamily = poppinsFontFamily)

                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        Modifier
                            .background(Color(0xFFF9F9F9))
                            .height(1.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "OR",
                        modifier = Modifier.padding(horizontal = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB5B5B5)
                    )
                    Divider(
                        Modifier
                            .background(Color(0xFFF9F9F9))
                            .height(1.dp)
                            .weight(1f)
                    )
                }

                TextButton(
                    onClick = {
                        val options = GoogleSignInOptions.Builder(
                            GoogleSignInOptions.DEFAULT_SIGN_IN
                        )
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                        val googleSignInClient = GoogleSignIn.getClient(context, options)
                        launcher.launch(googleSignInClient.signInIntent)
                    },
                    shape = RectangleShape,
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .padding(bottom = 8.dp, end = 24.dp, start = 24.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                    ),
                    border = BorderStroke(2.dp, Color.Gray)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.google_icon),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Google", fontSize = 14.sp, fontFamily = poppinsFontFamily)
                    when (val response = viewModel.googleAuthState.value) {
                        true -> {
                            if (response) {
                                navController.navigate(Screens.HomeScreen.route) {
                                    popUpTo(Screens.LoginScreen.route) {
                                        inclusive = false
                                    }
                                }
                            } else {
                                Toast(message = "Sign In Failed")
                            }
                        }

                        false -> {
                            Toast(message = "Error")
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
                    Text(text = "No tienes una cuenta?")
                    Text(
                        text = "Registrate aquí",
                        color = AppColor,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable {
                                navController.navigate(route = Screens.SignUpScreen.route)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image =
        if (passwordVisible.value) {
            Icons.Default.VisibilityOff
        } else {
            Icons.Default.Visibility
        }
    IconButton(onClick = {
        passwordVisible.value = !passwordVisible.value
    }) {
        Icon(
            imageVector = image,
            contentDescription = ""
        )
    }
}
