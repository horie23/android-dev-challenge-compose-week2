/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.timer

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.theme.purple500

@Composable
fun CountdownScreen(viewModel: CountdownTimerViewModel = viewModel()) {
    val time: Long by viewModel.countdownTime.observeAsState(initial = 0L)
    val isCountingDown: Boolean by viewModel.isCountingDown.observeAsState(initial = false)
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        CountdownSetting(
            isCountingDown = isCountingDown,
            onClick = { time ->
                if (isCountingDown) {
                    viewModel.stopCountdown()
                } else {
                    viewModel.startCountdown(time)
                }
            }
        )
        if (time > 0) {
            Countdown(time = time)
        }
    }
}

@Composable
fun CountdownSetting(isCountingDown: Boolean, onClick: (Long) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        var seconds by remember { mutableStateOf("60") }
        TextField(
            value = seconds,
            onValueChange = { seconds = it },
            label = { Text("seconds") }
        )
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = {
                onClick(seconds.toLongOrNull() ?: 0)
            }
        ) {
            Text(if (isCountingDown) "Stop" else "Start")
        }
    }
}

@Composable
fun Countdown(time: Long) {
    Box(contentAlignment = Alignment.Center) {
        CountdownCircle()
        Text(
            text = time.toString(),
            fontSize = 64.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(48.dp)
        )
    }
}

@Composable
fun CountdownCircle() {
    val infiniteTransition = rememberInfiniteTransition()
    val animationValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val stroke = with(LocalDensity.current) { Stroke(12.dp.toPx()) }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(48.dp),
        onDraw = {
            val innerRadius = (size.minDimension - stroke.width) / 2
            drawArc(
                color = purple500,
                startAngle = -90f,
                sweepAngle = animationValue,
                useCenter = false,
                topLeft = Offset(
                    (size / 2.0f).width - innerRadius,
                    (size / 2.0f).height - innerRadius
                ),
                size = Size(innerRadius * 2, innerRadius * 2),
                style = stroke,
            )
        }
    )
}
