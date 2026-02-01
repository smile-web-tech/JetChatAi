package com.example.jetchatai

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetchatai.ui.theme.LightBackground
import com.example.jetchatai.ui.theme.mainColor
import kotlinx.coroutines.launch
import androidx.navigation.compose.rememberNavController

@Composable
fun IntroScreen(
    navController: NavController
    ) {

    val pagerState = rememberPagerState(pageCount = {pages.size})
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightBackground)
            .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
    ) {
    Spacer(modifier = Modifier.size(24.dp))
        HorizontalPager(state = pagerState) {index ->
            val currentPage = pages[index]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(painter = painterResource(currentPage.imageRes) , contentDescription = "Intro1",
                )

                Text(text = currentPage.title, fontFamily = jakarta_bold, fontWeight = FontWeight.Bold, fontSize = 24.sp,
                    modifier = Modifier.padding(top = 24.dp)
                    )
                Text(text = currentPage.description, fontFamily = jakarta_regular)

            }
        }

        Column(
            modifier = Modifier.fillMaxHeight().padding(bottom = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            PageIndicator(
                pagerState = pagerState,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = {
                    if (pagerState.currentPage < 2) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        navController.navigate("login")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mainColor,
                )
            ) {
                Text(
                    text = if (pagerState.currentPage == 2) "Get started" else "Next",
                    fontSize = 16.sp,
                    fontFamily = jakarta_regular
                )

            }
        }

    }


}

@Composable
@Preview
fun IntroScreenPreview() {
    val context = LocalContext.current
    val fakeNavController = rememberNavController()
    IntroScreen(
        navController = fakeNavController,
    )
}


@Composable
fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { index ->

            val     isSelected = pagerState.currentPage == index

            val width by animateDpAsState(
                targetValue = if (isSelected) 22.dp else 10.dp,
                animationSpec = tween(durationMillis = 300),
                label = "dotWidth"
            )

            val color by animateColorAsState(
                targetValue = if (isSelected) mainColor else Color.LightGray,
                animationSpec = tween(durationMillis = 300),
                label = "dotColor"
            )

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .height(10.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
data class OnBoardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

val pages = listOf(
    OnBoardingPage(
        imageRes = R.drawable.intro3,
        title = "Your One - Stop name solution",
        description = "Powered AI chat bot"
    ),
    OnBoardingPage(
        imageRes = R.drawable.intro3,
        title = "Hana dali",
        description = "Powered AI chat bot by Gemini"
    ),
    OnBoardingPage(
        imageRes = R.drawable.intro1,
        title = "Hana dalihana",
        description = "Powered AIII chat bot by Gemini"
    )
)