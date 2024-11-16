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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.kodate.R

@Composable
fun ChatRoom(modifier: Modifier = Modifier, navController: NavHostController) {
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(color = Color(0XFF090e12))
                .padding(PaddingValues(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircleProfile(Modifier, 60, image = R.drawable.meganprofile)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Megan Fox", color = Color.White)
                }

            }
            BackButton(Modifier.align(Alignment.CenterStart))

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
                val scrollState = rememberScrollState()
                Column(
                    Modifier
                        .padding(vertical = 20.dp, horizontal = 10.dp)
                        .fillMaxSize()
                        .verticalScroll(scrollState)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                        ChatBubble(modifier = Modifier, backGroundColor = Color(0XFF5B26DA), textColor = Color.White, message = "morning sweetie")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start){
                        ChatBubble(modifier = Modifier, backGroundColor = Color(0XFFe5e5e7), textColor = Color.Black, message = "Hi")
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start){
                        ChatBubble(modifier = Modifier, backGroundColor = Color(0XFFe5e5e7), textColor = Color.Black, message = "what time will u be here")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                        ChatBubble(modifier = Modifier, backGroundColor = Color(0XFF5B26DA), textColor = Color.White, message = "be there at 9")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        ChatBubble(
                            modifier = Modifier,
                            backGroundColor = Color(0XFFe5e5e7),
                            textColor = Color.Black,
                            message = "ok, sounds fine"
                        )
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
                        modifier = Modifier.size(35.dp)
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
                        //TextFieldChat(width = 270, desc = "type ur message here...")
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
    val text = remember { mutableStateOf(value)
    }

    TextField(value = text.value, onValueChange = {text.value = it
        onValueChange(it)},
        modifier = Modifier
            .width(width.dp)
            .height(70.dp), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
        ), textStyle = TextStyle(color = Color.White, fontSize = 16.sp), placeholder = {
            Text(text = desc, color = Color.White, fontSize = 16.sp)
    }
    )
}

@Composable
fun ChatBubble(modifier: Modifier = Modifier, backGroundColor: Color, textColor: Color, message: String){
    Box(
        modifier
            .widthIn(max = 200.dp)
            .wrapContentHeight()
            .padding(horizontal = 10.dp)
            .background(color = backGroundColor, shape = RoundedCornerShape(10.dp))
            ) {
        Text(text = message, fontSize = 16.sp, color = textColor, modifier = Modifier.padding(10.dp))
    }
}



