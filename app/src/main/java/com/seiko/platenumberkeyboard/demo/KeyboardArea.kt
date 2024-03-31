package com.seiko.platenumberkeyboard.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun KeyboardArea(
  isShown: Boolean,
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier,
  keyboard: @Composable () -> Unit,
) {
  Column(modifier) {
    AnimatedVisibility(isShown, Modifier.weight(1f)) {
      Spacer(
        Modifier
          .clickable(onClick = onDismissRequest)
          .fillMaxSize(),
      )
    }
    AnimatedVisibility(
      isShown,
      enter = fadeIn() + slideInVertically { it },
      exit = fadeOut() + slideOutVertically { it },
    ) {
      keyboard()
    }
  }
}
