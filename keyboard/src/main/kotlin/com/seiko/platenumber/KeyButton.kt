package com.seiko.platenumber

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeyButton(
  key: Char,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  shape: Shape = MaterialTheme.shapes.small,
  contentPadding: PaddingValues = KeyButtonDefaults.contentPadding,
  textMapper: (Char) -> String = ::keyTextFor,
  containerColor: Color = containerColorFor(key, enabled),
  contentColor: Color = contentColorFor(key, enabled),
) {
  if (key == EMPTY) {
    Spacer(modifier)
    return
  }
  Surface(
    onClick = onClick,
    enabled = enabled,
    color = containerColor,
    contentColor = contentColor,
    modifier = modifier,
    shape = shape,
  ) {
    Box(Modifier.padding(contentPadding), Alignment.Center) {
      when (key) {
        DEL -> {
          Icon(
            Icons.AutoMirrored.Outlined.Backspace,
            contentDescription = textMapper(key),
          )
        }

        OK -> {
          Text(
            textMapper(key),
            fontSize = KeyButtonDefaults.noLetterTextSize,
          )
        }

        MORE -> {
          Text(
            textMapper(key),
            fontSize = KeyButtonDefaults.noLetterTextSize,
          )
        }

        BACK -> {
          Text(
            textMapper(key),
            fontSize = KeyButtonDefaults.noLetterTextSize,
          )
        }

        else -> {
          Text(
            textMapper(key),
            fontSize = if (key.fixIsLetterOrDigit()) {
              KeyButtonDefaults.letterTextSize
            } else {
              KeyButtonDefaults.noLetterTextSize
            },
          )
        }
      }
    }
  }
}

private fun keyTextFor(char: Char): String {
  return when (char) {
    DEL -> "删除"
    OK -> "确定"
    MORE -> "更多"
    BACK -> "返回"
    else -> char.toString()
  }
}

private fun containerColorFor(char: Char, enabled: Boolean): Color {
  return when (char) {
    OK -> KeyButtonDefaults.primaryContainerColor
    DEL -> KeyButtonDefaults.secondaryContainerColor
    else -> KeyButtonDefaults.containerColor
  }.copy(alpha = if (enabled) 1f else 0.5f)
}

private fun contentColorFor(char: Char, enabled: Boolean): Color {
  return when (char) {
    OK -> KeyButtonDefaults.primaryContentColor
    DEL -> KeyButtonDefaults.secondaryContentColor
    else -> KeyButtonDefaults.contentColor
  }.copy(alpha = if (enabled) 1f else 0.5f)
}

private object KeyButtonDefaults {
  val containerColor = Color.White
  val contentColor = Color.Black

  val primaryContainerColor = Color(0xFF418AF9)
  val primaryContentColor = Color.White

  val secondaryContainerColor = Color(0xFFCECECE)
  val secondaryContentColor = Color.Black

  val contentPadding = PaddingValues(0.dp)

  val letterTextSize = 18.sp
  val noLetterTextSize = 16.sp
}

@Preview(showBackground = true, backgroundColor = 0xFFDDDDDD)
@Composable
private fun KeyButtonPreview() {
  @Composable
  fun item(char: Char, enabled: Boolean) {
    KeyButton(
      char,
      onClick = {},
      enabled = enabled,
      contentPadding = PaddingValues(horizontal = 8.dp),
      modifier = Modifier
        .height(40.dp)
        .width(60.dp),
    )
  }
  MaterialTheme {
    Column(Modifier.padding(16.dp)) {
      Row {
        item('A', true)
        Spacer(Modifier.width(8.dp))
        item('A', false)
      }
      Spacer(Modifier.height(8.dp))
      Row {
        item('京', true)
        Spacer(Modifier.width(8.dp))
        item('京', false)
      }
      Spacer(Modifier.height(8.dp))
      Row {
        item(DEL, true)
        Spacer(Modifier.width(8.dp))
        item(DEL, false)
      }
      Spacer(Modifier.height(8.dp))
      Row {
        item(OK, true)
        Spacer(Modifier.width(8.dp))
        item(OK, false)
      }
      Spacer(Modifier.height(8.dp))
      Row {
        item(MORE, true)
        Spacer(Modifier.width(8.dp))
        item(MORE, false)
      }
      Spacer(Modifier.height(8.dp))
      Row {
        item(BACK, true)
        Spacer(Modifier.width(8.dp))
        item(BACK, false)
      }
      Spacer(Modifier.height(8.dp))
      Row {
        item(EMPTY, true)
        Spacer(Modifier.width(8.dp))
        item(EMPTY, false)
      }
    }
  }
}
