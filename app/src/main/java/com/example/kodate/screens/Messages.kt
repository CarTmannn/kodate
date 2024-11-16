package com.example.kodate.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(navController: NavHostController, logInViewModel: LogInViewModel = viewModel()){
    fun ManipulateEmail(email: String): String {
        return email.replace("@", "%40")
    }
    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color(0XFF090e12)) {
        innerPadding -> Column(modifier = Modifier.padding(innerPadding)) {

        Box (
            Modifier
                .height(85.dp)
                .fillMaxWidth()
                .background(color = Color(0XFF090e12)), contentAlignment = Alignment.CenterStart) {
            Row(Modifier.padding(horizontal = 20.dp, vertical = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                BackButton(Modifier)
                Spacer(Modifier.width(20.dp))
                Text(
                    text = "Messages",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium
                    )
                }

            }
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0XFF090e12))

        ) {

            Box(
                Modifier
                    .size(200.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 65.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.o), contentDescription = "", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }

            Column(Modifier.padding(vertical = 0.dp, horizontal = 20.dp), horizontalAlignment = Alignment.Start) {
                Text(text = "Recent Matches", color = Color.White, fontSize = 20.sp)
                Spacer(Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .width(80.dp)
                            .height(100.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Filled.Favorite, contentDescription = "", tint = Color(0XFFFB0000))
                            Spacer(Modifier.height(10.dp))
                            Text(text = logInViewModel.fetchUserState.value!!.matchedUsers.size.toString(), color = Color(0XFFFB0000))
                        }
                    }
                    LazyRow(){
                        items(logInViewModel.fetchUserState.value!!.matchedUsers){item ->
                            Spacer(modifier = Modifier.width(20.dp))
                            Box(
                                Modifier
                                    .size(80.dp).clip(shape = RoundedCornerShape(40.dp))
                                    .background(color = Color.White, shape = RoundedCornerShape(40.dp))) {
                                Image(painter = rememberImagePainter(data = "https://firebasestorage.googleapis.com/v0/b/chatapp-8e574.appspot.com/o/profileImage%2F${ManipulateEmail(item)}.jpg?alt=media"), contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                            }
                        }
                    }

                }


            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(580.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color(0XFF5B26DA),
                        RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
                    )
                    .padding(vertical = 20.dp, horizontal = 20.dp)) {
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    CircleProfile(Modifier, 70, image = R.drawable.meganprofile)
                    Spacer(Modifier.width(20.dp))
                    Column() {
                        Text(text = "Megan Fox", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(10.dp))
                        Box(
                            Modifier
                                .width(200.dp)
                                .height(25.dp)) {
                            Text(text = "What about u come over", color = Color.White)
                        }


                    }
                    Spacer(Modifier.width(35.dp))
                    Column(
                        modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Box(
                            Modifier
                                .size(15.dp)
                                .background(
                                    color = Color(0XFFDD88CF),
                                    shape = RoundedCornerShape(15.dp)
                                )) {
                        }
                        Spacer(Modifier.height(25.dp))
                        Text(text = "09.09", color = Color(0XFF9EA3AE),)
                    }
                    
                }
            }


        }

        }
    }
}

@Composable
fun BackButton(modifier: Modifier) {
    Box(
        Modifier
            .size(45.dp)
            .background(Color(0XFF090e12), shape = RoundedCornerShape(25.dp))
            .border(
                width = 2.dp,
                color = Color(0XFF1c2125),
                shape = RoundedCornerShape(25.dp)
            ), contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowLeft,
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier.size(35.dp)
        )
    }
}

@Composable
fun CircleProfile(modifier: Modifier, size: Int, image: Int){
    Box(
        Modifier
            .size(size.dp)
            .clip(shape = RoundedCornerShape(35.dp))
            .background(color = Color.White, shape = RoundedCornerShape(35.dp))) {
        Image(painter = painterResource(id = image), contentDescription = "", contentScale = ContentScale.Crop)
    }
}