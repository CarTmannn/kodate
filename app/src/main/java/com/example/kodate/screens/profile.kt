package com.example.kodate.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter


@Composable
fun Profile(modifier: Modifier, navController: NavHostController, logInViewModel: LogInViewModel = viewModel()){
    val userData by logInViewModel.fetchUserState.collectAsState()
    fun ManipulateEmail(email: String): String {
        return email.replace("@", "%40")
    }

    Column(Modifier.fillMaxSize().background(color = Color(0XFF090E12))) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(110.dp)
                .shadow(
                    elevation = 10.dp,
                    clip = true,
                    ambientColor = Color(0XFF7764fa),
                    shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
                    spotColor = Color(0XFF7764fa)
                )
                .background(
                    color = Color(0XFF090E12),
                    shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
                ), contentAlignment = Alignment.CenterStart
        ) {
            Column(Modifier.padding(PaddingValues(top = 30.dp, start = 20.dp, end = 20.dp, bottom = 10.dp))) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BackButton(modifier = Modifier)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "Profile",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }

        }
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(

                    Modifier
                        .size(150.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(75.dp),
                            clip = true,
                            ambientColor = Color(0XFF6e3859),
                            spotColor = Color(0XFF6e3859)
                        )
                        .background(color = Color(0XFF1c2125), shape = RoundedCornerShape(75.dp))
                        .clip(shape = RoundedCornerShape(75.dp))
                        .clickable {
                        },
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = "https://firebasestorage.googleapis.com/v0/b/chatapp-8e574.appspot.com/o/profileImage%2F${
                                ManipulateEmail(
                                    userData!!.profilePic
                                )
                            }.jpg?alt=media"
                        ),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(40.dp))
                Box(
                    Modifier
                        .height(240.dp)
                        .width(135.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(10.dp),
                            clip = true,
                            ambientColor = Color(0XFF6e3859),
                            spotColor = Color(0XFF6e3859)
                        )
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(color = Color(0XFF1c2125), shape = RoundedCornerShape(10.dp))
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = "https://firebasestorage.googleapis.com/v0/b/chatapp-8e574.appspot.com/o/displayPict%2F${
                                ManipulateEmail(
                                    userData!!.profilePic
                                )
                            }.jpg?alt=media"
                        ),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Name",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0XFF7764fa)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = userData?.name.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "About",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0XFF7764fa)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = userData?.bio.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Gender",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0XFF7764fa)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = userData?.gender.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Interests",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0XFF7764fa)
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(10.dp), contentAlignment = Alignment.CenterStart
            ) {
                if (userData?.interests!!.isEmpty()) {
                    Box(modifier = Modifier)
                } else {
                    LazyRow() {
                        items(userData?.interests!!) { item ->
                            Box(
                                Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(color = Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(
                                        vertical = 10.dp,
                                        horizontal = 15.dp
                                    )
                                )
                            };
                            Spacer(Modifier.width(10.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

    }
}
