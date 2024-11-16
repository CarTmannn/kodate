package com.example.kodate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kodate.screens.ChatRoom
import com.example.kodate.screens.DisplayImage
import com.example.kodate.screens.Home
import com.example.kodate.screens.LoginScreen
import com.example.kodate.screens.MessagesScreen
import com.example.kodate.screens.Profile
import com.example.kodate.screens.SignUpScreen
import com.example.kodate.screens.UserCard
import com.example.kodate.viewmodel.SignUpViewModel
import com.example.kodate.viewmodel.TextFieldViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kodate.viewmodel.LogInViewModel

@Composable
fun NavigationApp(navController: NavHostController = rememberNavController()){
    val signUpViewModel: SignUpViewModel = viewModel()
    val logInViewModel: LogInViewModel = viewModel()

    NavHost(navController = navController, startDestination = "logIn"){
        composable("logIn") { LoginScreen(modifier = Modifier, navController = navController, logInViewModel = logInViewModel) }
        composable("signUpScreen") { SignUpScreen(modifier = Modifier, navController = navController, signUpViewModel = signUpViewModel) }
        composable("home") { Home(modifier = Modifier, navController = navController, logInViewModel = logInViewModel) }
        composable("messages") { MessagesScreen( navController = navController, logInViewModel = logInViewModel) }
        composable("chatRoom") { ChatRoom(modifier = Modifier, navController = navController) }
        composable("profile") { Profile(modifier = Modifier, navController = navController, logInViewModel = logInViewModel) }
        composable("displayImage") { DisplayImage(navController = navController,  signUpViewModel = signUpViewModel) }
    }
}