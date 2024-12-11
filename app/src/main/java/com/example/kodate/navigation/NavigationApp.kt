package com.example.kodate.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import com.example.kodate.viewmodel.SignUpViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kodate.data.factory.LogInViewModelFactory
import com.example.kodate.viewmodel.HomeViewModel
import com.example.kodate.viewmodel.LogInViewModel
import com.example.kodate.viewmodel.MessagesViewModel

@Composable
fun NavigationApp(navController: NavHostController = rememberNavController()){
    val context = LocalContext.current
    val signUpViewModel: SignUpViewModel = viewModel()
    val logInViewModel: LogInViewModel = viewModel(factory = LogInViewModelFactory(context))
    val messagesViewModel: MessagesViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()

    val isLoggedIn by logInViewModel.isLoggedin.collectAsState()

    NavHost(navController = navController, startDestination = if (isLoggedIn) "home" else "logIn"){
        composable("logIn") { LoginScreen(modifier = Modifier, navController = navController, logInViewModel = logInViewModel) }
        composable("signUpScreen") { SignUpScreen(modifier = Modifier, navController = navController, signUpViewModel = signUpViewModel) }
        composable("home") {
            val user by logInViewModel.fetchUserState.collectAsState()
            if (user != null) {
                Home(
                    modifier = Modifier,
                    navController = navController,
                    logInViewModel = logInViewModel,
                    homeViewModel = homeViewModel
                )
            } else {
                CircularProgressIndicator(color = Color.Gray, strokeWidth = 5.dp, modifier = Modifier.size(40.dp))
            }
        }

        composable("messages") { MessagesScreen( navController = navController, logInViewModel = logInViewModel, messagesViewModel = messagesViewModel, homeViewModel = homeViewModel) }
        composable("chatRoom") { ChatRoom(modifier = Modifier, navController = navController, messagesViewModel = messagesViewModel, logInViewModel = logInViewModel) }
        composable("profile") { Profile(modifier = Modifier, navController = navController, logInViewModel = logInViewModel, homeViewModel = homeViewModel) }
        composable("displayImage") { DisplayImage(navController = navController,  signUpViewModel = signUpViewModel) }
    }

}