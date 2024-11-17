package com.example.kodate.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Boy
import androidx.compose.material.icons.filled.Girl
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.kodate.R
import com.example.kodate.viewmodel.TextFieldViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kodate.data.model.SignUpState
import com.example.kodate.viewmodel.SignUpViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(modifier: Modifier, navController: NavHostController, viewModel: TextFieldViewModel = viewModel(), signUpViewModel: SignUpViewModel = viewModel()){
    var isRegistEmail: Boolean by remember {
        mutableStateOf(true)
    }
    val imageProfileUri by viewModel.imageProfileUri.collectAsState()
        val signUpState by signUpViewModel.signUpState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(signUpState) {
        if (signUpState.isEmailSuccess) {
            isRegistEmail = !isRegistEmail
            if (signUpState.isSuccess){
                navController.navigate("displayImage")
                } else {
                println("Not yet uploading image")
                }
        } else if (signUpState.isEmailExists) {
            println("Email already exists.")
        } else if (signUpState.errorMessage != null) {
            println("Sign-up error: ${signUpState.errorMessage}")
        }
    }

    BackHandler {
        if (isRegistEmail){
            navController.navigate("logIn")
        } else {
            isRegistEmail = !isRegistEmail
        }
    }


    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFF090e12))
            .padding(PaddingValues(top = 50.dp, start = 20.dp, end = 20.dp, bottom = 10.dp))) {
        Text(
            text = "Sign Up",
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0XFF7764fa)
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (isRegistEmail) {
            SignUpEmail(onContinueClicked = {
                coroutineScope.launch {
                if (viewModel.validateAllFields()){
                     signUpViewModel.signUp(email = viewModel.textFieldState.value.email, password = viewModel.textFieldState.value.confirmPassword)
                      } else {
                    println("something wrong with the form")
                }}
            }, viewModel = viewModel, errorEmail = viewModel.textFieldState.value.emailError , errorPassword = viewModel.textFieldState.value.passwordError, errorConfirmPassword = viewModel.textFieldState.value.confirmPasswordError)
        } else {
            SignUpData(onNextClicked = {
                if(viewModel.textFieldState.value.name.isNotEmpty() && viewModel.textFieldState.value.bio.isNotEmpty() && imageProfileUri != null){
                    coroutineScope.launch {
                        signUpViewModel.setTempEmail(viewModel.textFieldState.value.email)
                        signUpViewModel.addAnotherDataToUsersCollection(email = viewModel.textFieldState.value.email,  name = viewModel.textFieldState.value.name, profilePic = viewModel.imageProfileUri.value!!, age = viewModel.textFieldState.value.age, bio = viewModel.textFieldState.value.bio, gender = if (viewModel.textFieldState.value.isMale) "male" else "female", )
                        navController.navigate("displayImage")
                    }
                } else {
                    println("Please insert ur name and bio")
                }
                 })
        }

    }
    if (signUpState.isLoading) {
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
fun SignUpData(onNextClicked: ()-> Unit, viewModel: TextFieldViewModel = viewModel()){
    var isBoy: Boolean by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    val imageProfileUri by viewModel.imageProfileUri.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateImageProfileUri(uri)
    }

    Column {
        Text(text = "Fill this out,", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "and Let the Magic Happen!", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .size(100.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(color = Color.Gray, shape = RoundedCornerShape(50.dp))) {
                if (imageProfileUri != null) {
                    AsyncImage(model = imageProfileUri, contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                } else {
                    Box(modifier = Modifier)
                }
            }
            Icon(imageVector = Icons.Filled.PhotoCamera, contentDescription = "", tint = Color.White, modifier = Modifier
                .offset(x = 30.dp, y = 35.dp)
                .size(30.dp)
                .clickable {
                    launcher.launch("image/*")
                })
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextFieldBox(width = 400, desc = "name", value = viewModel.textFieldState.value.name, onValueChange = { viewModel.setName(it) })
        Spacer(modifier = Modifier.height(20.dp))
        TextFieldBioBox(
            width = 400,
            desc = "Describe urself here n be shine",
            value = viewModel.textFieldState.value.bio,
            maxLength = 40
        )
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            Modifier
                .width(50.dp)
                .height(25.dp)
                .align(Alignment.End)
        ) {
            Text(text = "${viewModel.textFieldState.value.bio.length}/60", color = Color.White)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(Modifier.align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
            Text(text = "Age", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            Modifier
                .width(180.dp)
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
                , contentAlignment = Alignment.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (viewModel.textFieldState.value.age == 18){
                    Box(Modifier.size(50.dp))
                } else {
                    Box(
                        Modifier
                            .size(50.dp)
                            .clickable { viewModel.reduceAge() }, contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Filled.Remove, contentDescription = "", tint = Color.White)
                    }
                }
                Box(
                    Modifier
                        .size(70.dp)
                        .background(color = Color(0XFF22172A), shape = RoundedCornerShape(30.dp)), contentAlignment = Alignment.Center) {
                    Text(text = viewModel.textFieldState.value.age.toString(), color = Color.White, fontWeight = FontWeight.Medium, fontSize = 18.sp)

                }
                Box(
                    Modifier
                        .size(50.dp)
                        .clickable { viewModel.addAge() }, contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(Modifier.align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
            Text(text = "Gender", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            Modifier
                .height(60.dp)
                .width(130.dp)
                .background(color = Color(0XFF22172A), shape = RoundedCornerShape(40.dp))
                .align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
            Row(Modifier.padding(5.dp)) {
                Box(
                    Modifier
                        .size(50.dp)
                        .clickable {
                            isBoy = true
                            viewModel.toMale()
                        }
                        .background(
                            color = if (isBoy) Color(0XFFdd88cf) else Color.White,
                            shape = RoundedCornerShape(25.dp)
                        ), contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Filled.Boy, contentDescription = "", tint = if (isBoy) Color.White else Color(0XFF22172A), modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    Modifier
                        .size(50.dp)
                        .clickable {
                            isBoy = false
                            viewModel.toFemale()
                        }
                        .background(
                            color = if (isBoy) Color.White else Color(0XFFdd88cf),
                            shape = RoundedCornerShape(25.dp)
                        ), contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Filled.Girl, contentDescription = "", tint = if (isBoy) Color(0XFF22172A) else Color.White, modifier = Modifier.size(40.dp))
                }

            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(Modifier.align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
            Row {
                Text(text = "Male", fontWeight = FontWeight.Medium, color = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Female", fontWeight = FontWeight.Medium, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    onNextClicked()
                }
                .background(color = Color.White, shape = RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
            Text(text = "Next", color = Color(0XFF090e12), fontWeight = FontWeight.Bold, fontSize = 20.sp,)
        }
    }
}

@Composable
fun SignUpEmail(onContinueClicked: () -> Unit, viewModel: TextFieldViewModel = viewModel(), signUpViewModel: SignUpViewModel = viewModel(), errorEmail: String, errorPassword: String, errorConfirmPassword: String){
    val signUpState by signUpViewModel.signUpState.collectAsState()
    Column {
        LottieBox(modifier = Modifier, lottie = R.raw.lovee, width = 400, height = 180)
        Spacer(modifier = Modifier.height(10.dp))
        TextFieldBox(width = 350, desc = "email@domain.com", onValueChange = { viewModel.setEmail(it) }, value = viewModel.textFieldState.value.email)
        if(viewModel.textFieldState.value.emailError.isNotEmpty()){
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(horizontal = 10.dp),contentAlignment = Alignment.CenterStart) {
                Text(text = errorEmail, color = Color(0XFFf10059))
            }
        } else { Spacer(modifier = Modifier.height(30.dp))}
        TextFieldPasswordBox(width = 310, desc = "password", value = viewModel.textFieldState.value.password, onValueChange = { viewModel.setPassword(it) })
        if(viewModel.textFieldState.value.passwordError.isNotEmpty()){
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(horizontal = 10.dp),contentAlignment = Alignment.CenterStart) {
                Text(text = errorPassword, color = Color(0XFFf10059))
            }
        } else { Spacer(modifier = Modifier.height(30.dp))}
        TextFieldPasswordBox(width = 310, desc = "confirm password", value = viewModel.textFieldState.value.confirmPassword, onValueChange = { viewModel.setConfirmPassword(it) })
        if(viewModel.textFieldState.value.confirmPasswordError.isNotEmpty()){
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(horizontal = 10.dp),contentAlignment = Alignment.CenterStart) {
                Text(text = errorConfirmPassword, color = Color(0XFFf10059))
            }
        } else { Spacer(modifier = Modifier.height(30.dp))}
        Box(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    onContinueClicked()
                }
                .background(color = Color.White, shape = RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
            Text(text = "Continue", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Divider(thickness = 2.dp, color = Color.White, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Or", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(10.dp))
            Divider(thickness = 2.dp, color = Color.White, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            Modifier
                .size(40.dp)
                .clickable { }
                .background(color = Color.White, shape = RoundedCornerShape(25.dp))
                .clip(shape = RoundedCornerShape(25.dp))
                .align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
            Image(painter = painterResource(id = R.drawable.google), contentDescription = "", contentScale = ContentScale.Crop)
        }
        Column(Modifier.fillMaxSize().padding(bottom = 15.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "By clicking continue, you agree to our Terms of Service and Privacy Policy", color = Color.White, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, fontSize = 12.sp)
        }
    }

}

@Composable
fun TextFieldBox(width: Int, desc: String, value: String, onValueChange: (String) -> Unit){
    Box(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(color = Color(0XFF090e12), shape = RoundedCornerShape(20.dp))
            .border(width = 2.dp, color = Color(0XFF7764fa), shape = RoundedCornerShape(20.dp))
            .padding(vertical = 5.dp, horizontal = 10.dp)) {
        TextFieldChat(width = width, desc = desc, value = value, onValueChange = onValueChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldBioBox(width: Int, desc: String, value: String, maxLength: Int){
    val viewModel: TextFieldViewModel = viewModel()

    Box(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(color = Color(0XFF090e12), shape = RoundedCornerShape(20.dp))
            .border(width = 2.dp, color = Color(0XFF7764fa), shape = RoundedCornerShape(20.dp))
            .padding(vertical = 5.dp, horizontal = 10.dp)) {
        TextField(value = value, onValueChange = { newValue -> if (newValue.length <= maxLength){
            viewModel.setBio(newValue)
                        } },
            modifier = Modifier
                .width(width.dp)
                .height(70.dp), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
            ), textStyle = TextStyle(color = Color.White, fontSize = 16.sp), placeholder = {
                Text(text = desc, color = Color.White, fontSize = 16.sp)
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldPasswordBox(width: Int, desc: String, value: String, onValueChange: (String) -> Unit){


    var isPasswordVisible = remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(color = Color(0XFF090e12), shape = RoundedCornerShape(20.dp))
            .border(width = 2.dp, color = Color(0XFF7764fa), shape = RoundedCornerShape(20.dp))
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