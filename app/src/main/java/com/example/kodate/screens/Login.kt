package com.example.kodate.screens

import android.app.Activity
import android.app.AlertDialog
import android.text.style.LineHeightSpan
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.kodate.R
import com.airbnb.lottie.compose.*
import com.example.kodate.viewmodel.TextFieldViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kodate.viewmodel.LogInViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(modifier: Modifier, navController: NavHostController, viewModel: TextFieldViewModel = viewModel(), logInViewModel: LogInViewModel = viewModel()){

    val context = LocalContext.current as ComponentActivity
    val isLoggedin by logInViewModel.isLoggedin.collectAsState()
    val isLoading by logInViewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(isLoggedin) {
        if (isLoggedin) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    BackHandler {
        context.finish()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFF090e12))
            .padding(PaddingValues(top = 50.dp, bottom = 20.dp, end = 20.dp, start = 20.dp))) {
        Text(text = "Find the spark, right where you are!", color = Color(0XFF7764fa), fontSize = 30.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(
            PaddingValues(end = 100.dp)
        ))
        Spacer(modifier = Modifier.height(20.dp))
        LottieBox(modifier = Modifier, lottie = R.raw.chat, width = 400, height = 280)
        Spacer(modifier = Modifier.height(15.dp))
        TextFieldLoginBox(width = 500, desc = "Email", value = logInViewModel.loginState.value.email, onValueChange = { logInViewModel.setEmail(it) })
        if(logInViewModel.loginState.value.emailError != ""){
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(25.dp), contentAlignment = Alignment.CenterStart) {
                Text(text = logInViewModel.loginState.value.emailError, color = Color(0XFFf10059), modifier = Modifier.padding(
                    PaddingValues(start = 20.dp)
                ))
            }
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextFieldLogInPasswordBox(width = 300, desc = "Password", value = logInViewModel.loginState.value.password, onValueChange = { logInViewModel.setPassword(it) })
        if(logInViewModel.loginState.value.passwordError != ""){
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(30.dp), contentAlignment = Alignment.CenterStart) {
                Text(text = logInViewModel.loginState.value.passwordError, color = Color(0XFFf10059), modifier = Modifier.padding(
                    PaddingValues(start = 20.dp)
                ))
            }
        } else {
            Spacer(modifier = Modifier.height(15.dp))
        }
        Box(
            Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    coroutineScope.launch {
                        logInViewModel.logIn(
                            email = logInViewModel.loginState.value.email,
                            password = logInViewModel.loginState.value.password
                        )

                    }

                }
                .width(70.dp)
                .height(50.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color(0XFFfdfbff), shape = RoundedCornerShape(30.dp))
                .border(width = 2.dp, shape = RoundedCornerShape(30.dp), color = Color(0XFF7764fa)), contentAlignment = Alignment.Center) {
            Text(text = "Go", color = Color(0XFF7764fa), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        Spacer(Modifier.height(15.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .clickable {

                }
                .height(65.dp)
                .background(color = Color(0XFFdb4a39), shape = RoundedCornerShape(40.dp))) {
            Row(
                Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(50.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(25.dp))
                        .clip(shape = RoundedCornerShape(25.dp)), contentAlignment = Alignment.Center) {
                    Image(painter = painterResource(id = R.drawable.google), contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier.size(35.dp
                    ))
                }
                Spacer(Modifier.width(10.dp))
                Text(text = "Google", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                Text(text = "Don’t have an account?", color = Color(0XFFfdfbff), fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Sign Up", color = Color(0XFFe89cfc), fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.navigate("signUpScreen") })
            }

        }
    }
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}

@Composable
fun LottieBox(modifier: Modifier, lottie: Int, width: Int, height: Int){
    val compositionResult = rememberLottieComposition(LottieCompositionSpec.RawRes(lottie))
    val progress = animateLottieCompositionAsState(composition = compositionResult.value, iterations = LottieConstants.IterateForever)
    Box(
        Modifier
            .width(width.dp)
            .height(height.dp), contentAlignment = Alignment.Center){
        compositionResult.value?.let {
            composition -> LottieAnimation(composition = composition, progress = { progress.progress }, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        }
    }
}

@Composable
fun TextFieldLoginBox(width: Int, desc: String, value: String, onValueChange: (String) -> Unit){
    Box(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(color = Color(0XFF090e12), shape = RoundedCornerShape(35.dp))
            .border(width = 2.dp, color = Color(0XFF7764fa), shape = RoundedCornerShape(35.dp))
            .padding(vertical = 5.dp, horizontal = 10.dp)) {
        TextFieldChat(width = width, desc = desc, value = value, onValueChange = onValueChange)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldLogInPasswordBox(width: Int, desc: String, value: String, onValueChange: (String) -> Unit){


    var isPasswordVisible = remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(color = Color(0XFF090e12), shape = RoundedCornerShape(35.dp))
            .border(width = 2.dp, color = Color(0XFF7764fa), shape = RoundedCornerShape(35.dp))
            .padding(vertical = 5.dp, horizontal = 10.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(value = value, onValueChange = { onValueChange(it)},
                visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .width(width.dp)
                    .height(70.dp), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                ), textStyle = TextStyle(color = Color.White, fontSize = 16.sp), placeholder = {
                    Text(text = desc, color = Color.White, fontSize = 16.sp)

                }
            )
            Spacer(modifier = Modifier.width(5.dp))
            IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                Icon(imageVector = if (isPasswordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = "", tint = Color.White)
            }
        }

    }
}