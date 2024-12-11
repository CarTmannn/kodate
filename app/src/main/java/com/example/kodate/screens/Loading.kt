package com.example.kodate.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.kodate.R

@Composable
fun Loading(){
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFF090e12)), contentAlignment = Alignment.Center) {
        LottieBox(modifier = Modifier, lottie = R.raw.load, width = 500, height = 500)
    }
}