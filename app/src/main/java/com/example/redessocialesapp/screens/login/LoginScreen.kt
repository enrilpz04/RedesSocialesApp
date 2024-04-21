package com.example.redessocialesapp.screens.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.InputMode.Companion.Keyboard
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.redessocialesapp.R
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.ui.theme.poppinsFontFamily

@Preview
@Composable
fun LoginScreen(
    viewModel: LoginScreenVM = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (showLoginForm.value) {
                LoginHeader(text = "Iniciar sesión")
                LoginForm(
                    isCreateAccount = true
                ) { email, password ->
                    Log.d("RedesSocialesApp", "Logueando con $email y $password")
                    viewModel.signInWithEmailAndPassword(email, password) {
                        Log.d("RedesSocialesApp", "Login completado con exito")
                    }
                }
            }

        }
    }
}

@Composable
fun LoginForm(
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val buttonEnabled = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 10.dp)
    ) {
        EmailInput(
            emailState = email
        )
        PasswordInput(
            passwordState = password,
            labelId = "Password",
            passwordVisible = passwordVisible
        )
        if (isCreateAccount) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, end = 24.dp, start = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "He olvidado mi contraseña",
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    color = AppColor
                )
            }
        }
        SubmitButton(
            textId = if (isCreateAccount) "Continuar" else "Crear Cuenta",
            enabled = buttonEnabled
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
        if (isCreateAccount) {
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
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { },
                    shape = RectangleShape,
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .width(150.dp)
                        .padding(8.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        ),
                    elevation = ButtonDefaults.elevation(defaultElevation = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp),
                        tint= Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Google")
                }
                TextButton(
                    onClick = { },
                    shape = RectangleShape,
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .width(150.dp)
                        .padding(8.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black,

                        ),
                    elevation = ButtonDefaults.elevation(defaultElevation = 5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.facebook_icon),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp),
                        tint= Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Facebook", fontSize = 14.sp, fontFamily = poppinsFontFamily)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp, start = 24.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "No tienes una cuenta?")
                Text(text = "Registrate aquí", color = AppColor, modifier = Modifier.padding(start = 4.dp))
            }
        }

    }
}

@Composable
fun SubmitButton(textId: String, enabled: Boolean, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppColor,
            contentColor = Color.White
        ),
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth(),
        enabled = enabled

    ) {
        Text(
            text = textId,
            modifier = Modifier
                .padding(vertical = 5.dp)
                .align(Alignment.CenterVertically),
            fontSize = 20.sp,
            fontFamily = poppinsFontFamily
        )
    }
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = {
            Text(
                text = labelId,
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
                PasswordVisibleIcon(passwordVisible)
            } else null
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = Color(0xFF1863c8),
            focusedIndicatorColor = Color(0xFF1863c8),
            backgroundColor = Color.White
        )
    )

}

@Composable
fun PasswordVisibleIcon(passwordVisible: MutableState<Boolean>) {
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

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Correo Electrónico"
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email
    )
}

@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = {
            Text(
                text = labelId,
                fontFamily = poppinsFontFamily
            )
        },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = Color(0xFF1863c8),
            focusedIndicatorColor = Color(0xFF1863c8),
            backgroundColor = Color.White
        )
    )
}

@Composable
fun LoginHeader(text: String) {
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
                        onClick = { },
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
                    text = text,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 35.sp,
                    color = Color(0xFF1863c8),
                    fontFamily = poppinsFontFamily
                )
            }
        }

    }
}