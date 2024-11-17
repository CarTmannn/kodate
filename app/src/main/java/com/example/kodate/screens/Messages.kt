package com.example.kodate.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.kodate.viewmodel.MessagesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(navController: NavHostController, logInViewModel: LogInViewModel = viewModel(), messagesViewModel: MessagesViewModel = viewModel()){
    fun ManipulateEmail(email: String): String {
        return email.replace("@", "%40")
    }
    val coroutine = rememberCoroutineScope()
    val listChats by messagesViewModel.listChats.collectAsState()
    
    LaunchedEffect(Unit){
        messagesViewModel.getChat(logInViewModel.fetchUserState.value!!.email)
    }

    Column(modifier = Modifier.fillMaxSize().background(color = Color(0XFF090e12)),) {
        Box (
            Modifier
                .height(85.dp)
                .fillMaxWidth()
                .background(color = Color(0XFF090e12)), contentAlignment = Alignment.CenterStart) {
            Row(Modifier.padding(PaddingValues(start = 20.dp, end = 20.dp, top = 40.dp)), verticalAlignment = Alignment.CenterVertically) {
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
                                    .size(80.dp)
                                    .clickable {
                                        coroutine.launch {
                                            messagesViewModel.initiateChat(
                                                user1 = logInViewModel.fetchUserState.value!!.email,
                                                user2 = item
                                            )
                                            messagesViewModel.setTempEmail(item)
                                            messagesViewModel.setTempChatId(chatId = messagesViewModel.generateChatId(user1 = logInViewModel.fetchUserState.value!!.email, user2 = item) )
                                            navController.navigate("chatRoom")
                                        }

                                    }
                                    .clip(shape = RoundedCornerShape(40.dp))
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(40.dp)
                                    ), contentAlignment = Alignment.Center) {
                                Image(painter = rememberImagePainter(data = "https://firebasestorage.googleapis.com/v0/b/chatapp-8e574.appspot.com/o/profileImage%2F${ManipulateEmail(item)}.jpg?alt=media"), contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                            }
                        }
                    }

                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(640.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color(0XFF5B26DA),
                        RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
                    )
                    .padding(PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp))) {
                Column(
                    Modifier
                        .fillMaxSize()) {
                    LazyColumn(){
                       items(listChats){
                           item ->
                           val otherUserEmail = item.users.firstOrNull { it != logInViewModel.fetchUserState.value!!.email }
                           Box(
                               Modifier
                                   .fillMaxWidth()
                                   .height(70.dp).clickable { messagesViewModel.setTempEmail(otherUserEmail!!)
                                       messagesViewModel.setTempChatId(item.chatId)
                                       println("jam terakhir ${item.lastMessageAt}")
                                   navController.navigate("chatRoom")
                                   }
                           ) {
                               Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                                   Box(
                                       Modifier
                                           .size(70.dp)
                                           .clip(shape = RoundedCornerShape(35.dp))
                                           .background(
                                               color = Color.White,
                                               shape = RoundedCornerShape(35.dp)
                                           )) {
                                       Image(painter = rememberImagePainter(data = "https://firebasestorage.googleapis.com/v0/b/chatapp-8e574.appspot.com/o/profileImage%2F${ManipulateEmail(otherUserEmail!!)}.jpg?alt=media"), contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                                   }
                                   Spacer(Modifier.width(20.dp))
                                   Column() {
                                       val userName = remember(otherUserEmail) {
                                           mutableStateOf("")
                                       }
                                       LaunchedEffect(otherUserEmail) {
                                           userName.value = messagesViewModel.fetchUserNameByEmail(otherUserEmail!!)
                                       }
                                       Text(text = userName.value, color = Color.White, fontWeight = FontWeight.Bold)
                                       Spacer(Modifier.height(10.dp))
                                       Box(
                                           Modifier
                                               .width(200.dp)
                                               .height(25.dp)) {
                                           Text(text = item.lastMessage, color = Color.White)
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
                                       Text(text = if(item.lastMessageAt == null) "" else messagesViewModel.formatTimestamp(item.lastMessageAt!!.toDate()), color = Color(0XFF9EA3AE),)
                                   }
                               }
                           }
                           Spacer(modifier = Modifier.height(10.dp))
                       }
                    } //messagesViewModel.formatTimestamp(item.lastMessageAt.toString())
                    
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