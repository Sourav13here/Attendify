package com.example.attendify

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex


@ExperimentalFoundationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(){
    val content= LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,

                        ) {
                        logo(R.drawable.ic_launcher_background)
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White,
                    titleContentColor = Color.Black
                )



            )
        },
        bottomBar = {
            BottomAppBar(
                contentColor = Color.Black,
                containerColor = Color.White,
                modifier = Modifier.height(80.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {

                    }) {
                        Text(text = "Login/Signup")
                    }
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(5.dp)

            ) {





                Box(modifier = Modifier
                    .align(TopCenter)
                    .zIndex(0.2f)
                    .padding(top = 96.dp)
                    .height(60.dp)
                    .width(150.dp)
                    .background(color= Color.Gray)
                    .border(2.dp, color = Black)) {

                    Text(
                        text = "Events",
                        color = Black,

                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .align(Center)



                    )
                }

                Image(painter = painterResource(id=R.drawable.ic_launcher_foreground),contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .blur(7.dp),
                    Alignment.TopCenter,
                    contentScale = ContentScale.FillBounds
                )

                val itemList= listOf(
                    "Resplandor 6.0 starts from 24th Feb",
                    "When  female goats give birth to babies, called kids",
                    "The ladybug is the official insect of at least five states of the USA",
                    "On average, dogs have better eyesight than humans, although not as colourful",
                    "Dolphins sleep with one eye open",
                    "",
                    "",
                    "","","","","","",""
                )

                LazyColumn(
                    modifier = Modifier
                        .width(380.dp)
                        .padding(top = 200.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)

                ) {

                    items(itemList) { itemText ->
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray),
                        ) {
                            Text(
                                text = itemText,
                                maxLines = 1,
                                overflow = TextOverflow.Visible,

                                modifier = Modifier
                                    .padding(8.dp)
                                    .height(30.dp)
                                    .basicMarquee()


//
                            )

                        }
                    }
                }

            }








        }

    )



}



@Composable
fun logo(@DrawableRes imageRes: Int) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Displayed Image",
        modifier = Modifier
            .height(56.dp)
    )
}







@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun show() {
    Home()

}