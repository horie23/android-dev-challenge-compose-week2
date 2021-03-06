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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.intellij.lang.annotations.JdkConstants

@Composable
fun CountdownScreen(viewModel: CountdownTimerViewModel = viewModel()) {
    val time: Long by viewModel.countdownTime.observeAsState(initial = 0L)
    val isCountingDown: Boolean by viewModel.isCountingDown.observeAsState(initial = false)

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Countdown(time = time)
        CountdownSetting(isCountingDown = isCountingDown, onClick = { time ->
            if (isCountingDown) viewModel.stopCountdown() else viewModel.startCountdown(time)
        })
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
    Text(
        text = time.toString(),
        fontSize = 48.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(48.dp)
    )
}
