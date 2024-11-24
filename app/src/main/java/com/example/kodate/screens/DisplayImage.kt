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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.kodate.R
import com.example.kodate.viewmodel.TextFieldViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import  coil.compose.AsyncImage
import com.example.kodate.viewmodel.SignUpViewModel
import kotlinx.coroutines.launch

@Composable
fun DisplayImage(navController: NavHostController, viewModel: TextFieldViewModel = viewModel(), signUpViewModel: SignUpViewModel = viewModel()){
    var isChooseDisplayImage: Boolean by remember {
        mutableStateOf(false)
    }
    var scrollState = rememberScrollState()
    val imageUri by viewModel.imageUri.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val signUpState by signUpViewModel.signUpState.collectAsState()
    val tempEmail by signUpViewModel.tempEmail.collectAsState()

    LaunchedEffect(signUpState){
        if(signUpState.isUploadImageSuccess){
            navController.navigate("logIn")
        } else {
            println("Not success yet")
        }
    }

    BackHandler {
        if (isChooseDisplayImage){
            isChooseDisplayImage = !isChooseDisplayImage
        } else {

        }
    }


    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFF090e12))
            .padding(PaddingValues(top = 50.dp, start = 20.dp, end = 20.dp, bottom = 20.dp))
            .verticalScroll(scrollState)) {
        Text(text = "Let's get closer", color = Color(0XFF7764fa), fontSize = 35.sp, fontWeight = FontWeight.Bold)
        if (isChooseDisplayImage){
            ChooseDisplayImage(onFinishClicked = {
                coroutineScope.launch {
                    if (imageUri != null){
                        signUpViewModel.uploadInterests(interests = viewModel.textFieldState.value.interests, email = tempEmail, displayPic = imageUri!!, city = viewModel.textFieldState.value.city, likedUsers = emptyList(), matchedUsers = emptyList())
                } else {
                    println("Please choose picture")
                } }

            })
        } else {
            ChooseInterest({
                if (viewModel.textFieldState.value.interests.size < 3 || viewModel.textFieldState.value.city == ""){
                    println("must be at least 3")
                } else
                {
                    isChooseDisplayImage = !isChooseDisplayImage
                    println(viewModel.textFieldState.value.city)
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
fun ChooseDisplayImage(onFinishClicked: ()-> Unit, viewModel: TextFieldViewModel = viewModel()){
    val context = LocalContext.current
    val imageUri by viewModel.imageUri.collectAsState()


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateImageUri(uri)
    }


    Column {
        Spacer(modifier = Modifier.height(20.dp))
        Box (modifier = Modifier
            .height(670.dp)
            .width(900.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = Color(0XFF090E12))
            .border(width = 2.dp, color = Color(0XFF7764fa), shape = RoundedCornerShape(20.dp))){
            if (imageUri != null){
                AsyncImage(model = imageUri, contentDescription = "", modifier = Modifier
                    .fillMaxSize()
                    .clickable { launcher.launch("image/*") }, contentScale = ContentScale.Crop)
            } else {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0XFF090e12).copy(alpha = 0.96f)), contentAlignment = Alignment.Center){
                    Box(
                        Modifier
                            .size(60.dp)
                            .background(
                                color = Color(0XFF7764fa),
                                shape = RoundedCornerShape(25.dp)
                            )
                            .clickable { launcher.launch("image/*") }, contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Filled.PhotoCamera, contentDescription = "", tint = Color.Black, modifier = Modifier.size(35.dp))
                    }
            }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(15.dp))
                .border(width = 2.dp, color = Color(0XFF7764fa), shape = RoundedCornerShape(15.dp))
                .clickable {
                    onFinishClicked()
                }
                .background(color = Color.White, shape = RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
            Text(text = "Finish", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseInterest(onNextClicked: ()-> Unit, viewModel: TextFieldViewModel = viewModel()){
    val interestsState by viewModel.interests.collectAsState()
    val interestsList = interestsState.interests


    Column {
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Where are you based, if it’s not too personal?", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(
            PaddingValues(end = 20.dp)
        ))
        Spacer(modifier = Modifier.height(20.dp))
        TextFieldBox(
            width = 500,
            desc = "City",
            value = viewModel.textFieldState.value.city,
            onValueChange = { city -> viewModel.setCity(city) }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Pick your top 3", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Box(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp), contentAlignment = Alignment.CenterStart) {
                if (viewModel.textFieldState.value.interests.isEmpty()){
                    Box(modifier = Modifier)
                } else {
                    LazyRow() {
                        items(viewModel.textFieldState.value.interests) { item ->
                            Box(
                                Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(color = Color(0XFF7764fa))
                                    .clickable { viewModel.removeItemFromInterestList(item) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item,
                                    color = Color.White,
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
            Text(text = "${viewModel.textFieldState.value.interests.size}/3", color = Color.White, fontWeight = FontWeight.Medium, modifier = Modifier.align(Alignment.CenterEnd))
        }

        Divider(thickness = 2.dp, color = Color(0XFF7764fa))
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            Modifier
                .width(300.dp)
                .height(290.dp)
                .align(Alignment.CenterHorizontally), contentAlignment = Alignment.TopCenter) {
            FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalArrangement = Arrangement.spacedBy(8.dp), maxItemsInEachRow = 7) {
                val colors = listOf(Color(0XFF5a1b69), Color(0XFFfb928e), Color(0XFF4dd0e1), Color(0XFFe57373), Color(0XFF81c784), Color(0XFF006764), Color(0XFF6650a4))
                interestsList.forEachIndexed {
                    index, item ->
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .height(40.dp)
                            .background(color = colors[index % colors.size])
                            .clickable {
                                viewModel.addItemToInterestList(item)
                            }, contentAlignment = Alignment.Center
                    ) {
                        Text(text = item, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                            .wrapContentWidth(), maxLines = 1)
                    };
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(15.dp))
                .border(width = 2.dp, color = Color(0XFF7764fa), shape = RoundedCornerShape(15.dp))
                .clickable {
                    onNextClicked()
                }
                .background(color = Color.White, shape = RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
            Text(text = "Next", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}