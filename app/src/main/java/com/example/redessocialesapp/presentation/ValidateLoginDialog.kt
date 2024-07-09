package com.example.redessocialesapp.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.ui.theme.poppinsFontFamily
import com.example.redessocialesapp.util.Screens

@Composable
fun ValidateLoginDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onRegister: () -> Unit,
    onLogin: () -> Unit,
    text: String
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = "Debe iniciar sesión $text") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRegister()
                    }, colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        backgroundColor = Color.LightGray),
                    modifier = Modifier
                        .padding(end = 4.dp)
                ) {
                    Text(
                        text = "Registrarse",
                        fontSize = 12.sp,
                        fontFamily = poppinsFontFamily,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onLogin()
                    }, colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = AppColor
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 12.sp,
                        fontFamily = poppinsFontFamily,
                    )
                }
            },
            modifier = Modifier.padding(16.dp),

        )
    }
}