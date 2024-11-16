@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.kodate.screens

import android.widget.Space
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Person4
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.kodate.R
import com.example.kodate.viewmodel.LogInViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.kodate.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun UserCard(modifier: Modifier, name: String, age: String, bio: String, city: String, interests: List<String>, image: String){
    Box (modifier = Modifier
        .height(670.dp)
        .width(900.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(color = Color(0XFF090E12))
        .border(width = 3.dp, color = Color(0XFF7764fa), shape = RoundedCornerShape(20.dp))) {
        Image(
            painter = rememberImagePainter(data = "https://firebasestorage.googleapis.com/v0/b/chatapp-8e574.appspot.com/o/displayPict%2F${image}.jpg?alt=media"),
            contentDescription = "",
            Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(
                PaddingValues(
                    start = 20.dp,
                    top = 490.dp,
                    bottom = 20.dp
                )
            ),
        ) {
            Row {
                Text(
                    text = "${name},",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                );
                Spacer(Modifier.width(10.dp));
                Text(
                    text = "$age",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(10.dp));
            Text(
                text = "${bio}",
                color = Color.White,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(10.dp));
            Text(text = "${city}", color = Color.White, fontSize = 18.sp);
            Spacer(Modifier.height(10.dp));
            InterestList(interests = interests)
        }
    }
}

@Composable
fun Home(modifier: Modifier, navController: NavHostController, logInViewModel: LogInViewModel = viewModel(), homeViewModel: HomeViewModel = viewModel()){
    var scrollState = rememberScrollState()
    val listUsers by homeViewModel.fetchUsers.collectAsState()
    fun ManipulateEmail(email: String): String {
        return email.replace("@", "%40")
    }
    var currentIndex by remember { mutableStateOf(0) }
    val currentChoice = listUsers.getOrNull(currentIndex)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        homeViewModel.GetListUsers(email = logInViewModel.fetchUserState.value!!.email, gender = if(logInViewModel.fetchUserState.value!!.gender == "female") "male" else "female", likedUsers = logInViewModel.fetchUserState.value!!.likedUsers)
    }

    BackHandler {

    }

    Scaffold(containerColor = Color(0XFF090E12)) {
        innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    ambientColor = Color(0xFFDD88CF),
                    spotColor = Color(0xFFDD88CF)
                )
                .height(80.dp)
                .background(color = Color(0XFF090E12)), contentAlignment = Alignment.Center){
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .height(50.dp)
                            .width(160.dp)
                            .background(
                                color = Color(0XFF090E12),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clip(shape = RoundedCornerShape(20.dp))
                            .border(
                                width = 2.dp,
                                color = Color(0XFF7764fa),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { navController.navigate("messages") }, contentAlignment = Alignment.CenterStart) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                  , verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Messages", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(
                                PaddingValues(start = 20.dp, end = 10.dp, bottom = 5.dp)
                            ), fontSize = 20.sp)
                            Box(
                                Modifier
                                    .size(12.dp)
                                    .background(
                                        color = Color(0XFF7764fa),
                                        shape = RoundedCornerShape(7.dp)
                                    ),
                            ) {
                            }
                        }

                    }
                    Icon(imageVector = Icons.Filled.Person, contentDescription = "", tint = Color.White, modifier = Modifier
                        .size(50.dp)
                        .clickable { navController.navigate("profile") }
                        .padding(
                            PaddingValues(end = 15.dp)
                        ))

                }
            }
            Column(Modifier.padding(PaddingValues(start = 20.dp, end = 20.dp, bottom = 10.dp, top = 0.dp))) {
                Spacer(Modifier.padding(vertical = 10.dp))
                Box (
                    Modifier
                        .shadow(
                            elevation = 20.dp,
                            clip = true,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color(0XFF6e3859),
                            spotColor = Color(0XFF6e3859)
                        )
                        .clickable {
                        }){
                    if (currentChoice != null) {
                        UserCard(
                            modifier = Modifier,
                            name = currentChoice.name,
                            age = currentChoice.age.toString(),
                            bio = currentChoice.bio,
                            city = currentChoice.city,
                            interests = currentChoice.interests,
                            image = ManipulateEmail(email = currentChoice.email)
                        )
                    }

                }
                Spacer(Modifier.padding(vertical = 60.dp))
            }

    }
        Column(
            Modifier
                .padding(PaddingValues(bottom = 20.dp))
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
            Box(
                Modifier
                    .height(100.dp)
                    .width(180.dp)
                    .background(color = Color(0XFF22172A), shape = RoundedCornerShape(90.dp)), contentAlignment = Alignment.Center) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 0.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Box(
                        Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(40.dp))
                            .clickable {
                                if (currentIndex < listUsers.size - 1) {
                                    currentIndex++
                                } else {
                                    println("No more user")
                                }
                            }, contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "", tint = Color(0XFF22172A), modifier = Modifier.size(35.dp))
                    }
                    Box(
                        Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .background(
                                color = Color(0xFFDD88CF),
                                shape = RoundedCornerShape(40.dp)
                            )
                            .clickable {
                                coroutineScope.launch {
                                    if (currentIndex < listUsers.size - 1) {
                                        homeViewModel.handleLike(user1Email = logInViewModel.fetchUserState.value!!.email, user2Email = currentChoice!!.email)
                                        currentIndex++
                                    } else {
                                        println("No more user")
                                    }
                                }
                            }, contentAlignment = Alignment.Center, ) {
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = "", tint = Color.White, modifier = Modifier.size(35.dp))
                    }
                }
            }
        }

}
}

@Composable
fun InterestList(interests: List<String>) {
    LazyRow(Modifier.padding(PaddingValues(end = 15.dp))) {
        items(interests) {
            interest ->
            Box(
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color.White), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = interest,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)
                )
            };
            Spacer(Modifier.width(10.dp))
        }
    }
}