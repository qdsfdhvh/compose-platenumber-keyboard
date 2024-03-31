package com.seiko.platenumber

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PlateNumberKeyboard(
  keyboard: ImmutableList<String>,
  onItemKeyClicked: (Char) -> Unit,
  modifier: Modifier = Modifier,
  spacing: Dp = 8.dp,
  rowHeight: Dp = 40.dp,
  contentPadding: PaddingValues = PaddingValues(spacing),
  percentMapper: (Char) -> Float = ::rowWeightFor,
  backgroundColor: Color = PlateNumberKeyboardDefaults.backgroundColor,
  keyVisibleRegistry: KeyVisibleRegistry = KeyVisibleRegistry.AllOpen,
  keyContent: @Composable KeyRowLayoutScope.(Char) -> Unit = { key ->
    KeyButton(
      key = key,
      enabled = keyVisibleRegistry.isEnable(key),
      onClick = { onItemKeyClicked(key) },
      modifier = Modifier.rowWeight(
        weight = percentMapper(key),
        fillSpacing = key == EMPTY,
      ),
    )
  },
) {
  Column(
    modifier = modifier
      .background(backgroundColor)
      .padding(contentPadding),
  ) {
    keyboard.forEachIndexed { y, row ->
      KeyRowLayout(
        spacing = spacing,
        modifier = Modifier
          .fillMaxWidth()
          .height(rowHeight),
      ) {
        row.forEach { key ->
          keyContent(key)
        }
      }
      if (y != keyboard.size - 1) {
        Spacer(Modifier.height(spacing))
      }
    }
  }
}

private fun rowWeightFor(char: Char): Float {
  return when (char) {
    MORE, BACK, DEL, OK -> 1.5f
    else -> 1f
  }
}

@Preview(showBackground = true)
@Composable
private fun PlateNumberKeyboardPreview() {
  MaterialTheme {
    Box(Modifier.fillMaxWidth(), Alignment.Center) {
      PlateNumberKeyboard(
        keyboard = persistentListOf(
          "京津晋冀蒙辽吉黑沪苏",
          "浙皖闽赣鲁豫鄂湘粤桂",
          "琼渝川贵云藏陕甘",
          "青宁新台$EMPTY$MORE$DEL$OK",
        ),
        onItemKeyClicked = {},
        modifier = Modifier.fillMaxWidth(),
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PlateNumberKeyboardMDPreview() {
  MaterialTheme {
    Box(Modifier.fillMaxWidth(), Alignment.Center) {
      PlateNumberKeyboard(
        keyboard = persistentListOf(
          "京津晋冀蒙辽吉黑沪苏",
          "浙皖闽赣鲁豫鄂湘粤桂",
          "琼渝川贵云藏陕甘",
          "青宁新台$EMPTY$MORE$DEL$OK",
        ),
        onItemKeyClicked = {},
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colorScheme.background,
        keyContent = { key ->
          KeyButton(
            key = key,
            enabled = true,
            onClick = {},
            modifier = Modifier.rowWeight(
              weight = when (key) {
                MORE, BACK, DEL, OK -> 1.5f
                else -> 1f
              },
              fillSpacing = key == EMPTY,
            ),
            containerColor = when (key) {
              OK -> MaterialTheme.colorScheme.primary
              else -> MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = when (key) {
              OK -> MaterialTheme.colorScheme.onPrimary
              else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
          )
        },
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PlateNumberKeyboardMoreLayoutPreview() {
  MaterialTheme {
    Box(Modifier.fillMaxWidth(), Alignment.Center) {
      PlateNumberKeyboard(
        keyboard = persistentListOf(
          "1234567890",
          "QWERTYUIOP",
          "ASDFGHJKLM",
          "ZXCVBN$EMPTY$BACK$OK",
        ),
        onItemKeyClicked = {},
        modifier = Modifier.fillMaxWidth(),
        keyVisibleRegistry = KeyVisibleRegistry.BlackList(
          "IO".toCharArray(),
        ),
      )
    }
  }
}


@Preview(showBackground = true)
@Composable
private fun PlateNumberKeyboardMoreLayoutMdPreview() {
  MaterialTheme {
    Box(Modifier.fillMaxWidth(), Alignment.Center) {
      PlateNumberKeyboard(
        keyboard = persistentListOf(
          "1234567890",
          "QWERTYUIOP",
          "ASDFGHJKLM",
          "ZXCVBN$EMPTY$BACK$OK",
        ),
        onItemKeyClicked = {},
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colorScheme.background,
        keyContent = { key ->
          KeyButton(
            key = key,
            enabled = true,
            onClick = {},
            modifier = Modifier.rowWeight(
              weight = when (key) {
                MORE, BACK, DEL, OK -> 1.5f
                else -> 1f
              },
              fillSpacing = key == EMPTY,
            ),
            containerColor = when (key) {
              OK -> MaterialTheme.colorScheme.primary
              else -> MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = when (key) {
              OK -> MaterialTheme.colorScheme.onPrimary
              else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
          )
        },
      )
    }
  }
}

private object PlateNumberKeyboardDefaults {
  val backgroundColor = Color(0xFFDDDDDD)
}
