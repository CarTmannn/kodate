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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight



import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.kodate.R
import com.example.kodate.viewmodel.ChatRoomViewModel
import com.example.kodate.viewmodel.LogInViewModel
import com.example.kodate.viewmodel.MessagesViewModel
import com.example.kodate.viewmodel.TextFieldViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatRoom(modifier: Modifier = Modifier, navController: NavHostController, textFieldViewModel: TextFieldViewModel = viewModel(), messagesViewModel: MessagesViewModel = viewModel(), logInViewModel: LogInViewModel = viewModel(), chatRoomViewModel: ChatRoomViewModel = viewModel()) {
   val tempEmail by messagesViewModel.tempEmail.collectAsState()
    val listChats by chatRoomViewModel.listChats.collectAsState()
    val tempChatId by messagesViewModel.tempchatId.collectAsState()
    fun ManipulateEmail(email: String): String {
        return email.replace("@", "%40")
    }
    val userName = remember(tempEmail) {
        mutableStateOf("")
    }
    val coroutine = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        chatRoomViewModel.updateReadStatus(chatId = messagesViewModel.generateChatId(user1 = logInViewModel.fetchUserState.value!!.email, user2 = tempEmail), email = logInViewModel.fetchUserState.value!!.email)
        chatRoomViewModel.getTexts(
            user1 = logInViewModel.fetchUserState.value!!.email,
            user2 = tempEmail
        )
        userName.value = messagesViewModel.fetchUserNameByEmail(tempEmail)
        if (listChats.isNotEmpty()) {
            lazyListState.animateScrollToItem(listChats.size - 1)
        }
    }

    LaunchedEffect(listChats) {
        if (listChats.isNotEmpty()) {
            val currentUserEmail = logInViewModel.fetchUserState.value!!.email
            chatRoomViewModel.updateReadStatus(
                chatId = messagesViewModel.generateChatId(user1 = currentUserEmail, user2 = tempEmail),
                email = currentUserEmail
            )
        }
    }


    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(color = Color(0XFF090e12))
                .padding(PaddingValues(top = 50.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = rememberImagePainter(data = "https://firebasestorage.googleapis.com/v0/b/chatapp-8e574.appspot.com/o/profileImage%2F${ManipulateEmail(tempEmail)}.jpg?alt=media"), contentDescription = "", modifier = Modifier
                        .size(60.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(Color.Gray, shape = RoundedCornerShape(30.dp))
                        .fillMaxSize(), contentScale = ContentScale.Crop)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = userName.value.toString(), color = Color.White)
                }

            }
            BackButton(Modifier.align(Alignment.CenterStart), onClicked = { navController.navigate("messages") })

        }
        Divider(
            color = Color(0XFF5B26DA),
            thickness = 2.dp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(595.dp)
                .background(color = Color(0XFF090e12))) {
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(color = Color(0XFF090e12)), contentAlignment = Alignment.BottomCenter
                        ) {
                    Image(painter = painterResource(id = R.drawable.oo), contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier
                        .height(160.dp)
                        .width(290.dp))
                }
            }
            Box(Modifier.fillMaxSize()) {
                if (listChats.isEmpty()){
                    Box(modifier = Modifier)
                } else {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .padding(vertical = 20.dp, horizontal = 10.dp)
                            .fillMaxSize()) {

                        items(listChats) { item ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = if(item.senderId == logInViewModel.fetchUserState.value!!.email) Arrangement.End else Arrangement.Start) {
                                ChatBubble(
                                    modifier = Modifier,
                                    backGroundColor = if(item.senderId == logInViewModel.fetchUserState.value!!.email) Color(0XFF5B26DA) else Color(0XFFe5e5e7),
                                    textColor =  if(item.senderId == logInViewModel.fetchUserState.value!!.email) Color.White else Color.Black,
                                    message = item.message,
                                    sender = item.senderId
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color(0XFF090e12))
                .align(
                    Alignment.CenterHorizontally
                )) {
            Column {
                Divider(
                    color = Color(0XFF5B26DA),
                    thickness = 2.dp,
                )
                Row(
                    Modifier.padding(vertical = 15.dp, horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .size(35.dp)
                            .clickable { println("ini tempchatid${tempChatId}") }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        Modifier
                            .height(85.dp)
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(
                                color = Color(0XFF090e12),
                                shape = RoundedCornerShape(20.dp),
                            )
                            .border(
                                width = 2.dp,
                                color = Color(0XFF5B26DA),
                                shape = RoundedCornerShape(20.dp)
                            ), contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            Modifier
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                                .height(55.dp)
                                .width(260.dp), contentAlignment = Alignment.CenterStart
                        ) {
                        }
                        TextFieldChat(width = 270, desc = "type ur message here...", value = textFieldViewModel.textFieldState.value.chatMessage, onValueChange = { textFieldViewModel.setChatMessage(it) })
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = "",
                            tint = Color(0XFF5B26DA),
                            modifier = Modifier
                                .size(35.dp)
                                .align(
                                    Alignment.CenterEnd
                                )
                                .padding(PaddingValues(end = 10.dp))
                                .clickable {
                                    coroutine.launch {
                                        messagesViewModel.sendMessage(
                                            chatId = tempChatId,
                                            senderId = logInViewModel.fetchUserState.value!!.email,
                                            message = textFieldViewModel.textFieldState.value.chatMessage
                                        )
                                        textFieldViewModel.clearTextField()
                                        lazyListState.animateScrollToItem(listChats.size - 1)
                                    }

                                }
                        )
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldChat(width: Int, desc: String, value: String, onValueChange: (String)-> Unit){


    TextField(value = value, onValueChange = { onValueChange(it)},
        modifier = Modifier
            .width(width.dp)
            .height(70.dp), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
        ), textStyle = TextStyle(color = Color.White, fontSize = 16.sp), placeholder = {
            Text(text = desc, color = Color.White, fontSize = 16.sp)
    }
    )
}

@Composable
fun ChatBubble(modifier: Modifier = Modifier, backGroundColor: Color, textColor: Color, message: String, sender: String){
    Box(
        modifier
            .widthIn(max = 200.dp)
            .wrapContentHeight()
            .padding(horizontal = 10.dp)
            .background(color = backGroundColor, shape = RoundedCornerShape(10.dp))
            ) {
        Text(text = message, fontSize = 17.sp, color = textColor, modifier = Modifier.padding(10.dp))
    }
}



