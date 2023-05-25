package com.example.laba3

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.laba3.R
import com.google.accompanist.pager.*
import kotlin.math.absoluteValue
import com.google.accompanist.pager.rememberPagerState as rememberPagerState1

data class Images(
    val username : String,
    val desc : String,
    val imgUri:Int
)

@SuppressLint("SuspiciousIndentation")
@Preview
@ExperimentalPagerApi
@Composable
fun ViewPagerSlider(){
    val imagesList = listOf(
        Images(
            "User, 18",
            "GosUslugi? Trivagodadsadasddddd",
            R.drawable.image2
        ),
        Images(
            "User, 18",
            "GosUslugi? Trivagodadsadasddddd",
            R.drawable.image3
        ),
        Images(
            "User, 18",
            "GosUslugi? Trivagodadsadasddddd",
            R.drawable.image5
    )
    )

    val pagerState = rememberPagerState1(
        pageCount = imagesList.size,
        initialPage = 0
    )

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalPager(
            state = pagerState
        ) {page ->
            Card(modifier = Modifier
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .fillMaxWidth()
                .padding(25.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                val newImages = imagesList[page]
                Box(modifier = Modifier
                    .fillMaxSize(0.8f)
                    .background(Color.LightGray)
                    .align(Alignment.Center)
                ){
                    Image(painter = painterResource(
                        id = newImages.imgUri
                    ),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .align(Alignment.BottomCenter)
                            .height(90.dp)
                    ) {
                        Column() {
                            Text(
                                text = newImages.username,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = newImages.desc,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth().padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState
        )

}
