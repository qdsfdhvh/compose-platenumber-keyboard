package com.seiko.platenumber

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.persistentMapOf

@Composable
fun PlateNumberTextField(
  value: TextFieldValue,
  plateNumberType: PlateNumberType,
  modifier: Modifier = Modifier,
  isFocused: Boolean = false,
) {
  PlateNumberTextField(
    value = value,
    maxLength = plateNumberType.maxLength,
    modifier = modifier,
    isFocused = isFocused,
    showPoint = when (plateNumberType) {
      PlateNumberType.CIVIL,
      PlateNumberType.NEW_ENERGY,
      PlateNumberType.PLA2012,
      PlateNumberType.SHI2012,
      PlateNumberType.SHI2017,
      PlateNumberType.LING2012,
      PlateNumberType.LING2018,
      PlateNumberType.AVIATION,
      -> true
      PlateNumberType.WJ2012 -> false
      PlateNumberType.UNKNOWN -> false
    },
  )
}

@Composable
fun PlateNumberTextField(
  value: TextFieldValue,
  maxLength: Int,
  modifier: Modifier = Modifier,
  isFocused: Boolean = false,
  showPoint: Boolean = true,
  containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
  contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
  borderColor: Color = MaterialTheme.colorScheme.primary,
) {
  BasicTextField(
    value = value,
    onValueChange = {},
    readOnly = true,
    singleLine = true,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Number,
      imeAction = ImeAction.Done,
    ),
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight(),
    decorationBox = {
      Row(
        Modifier
          .fillMaxWidth()
          .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        val selectIndex = value.text.length.coerceAtMost(maxLength - 1).coerceAtLeast(0)
        repeat(maxLength) { index ->
          PlateNumberKey(
            char = value.annotatedString.getOrNull(index),
            isSelected = isFocused && selectIndex == index,
            containerColor = containerColor,
            contentColor = contentColor,
            borderColor = borderColor,
          )
          if (showPoint && index == 1) {
            Spacer(
              Modifier
                .size(4.dp)
                .background(containerColor, CircleShape),
            )
          }
        }
      }
    },
  )
}

@Composable
private fun PlateNumberKey(
  char: Char?,
  isSelected: Boolean,
  containerColor: Color,
  contentColor: Color,
  borderColor: Color,
) {
  Surface(
    shape = MaterialTheme.shapes.small,
    color = containerColor,
    border = if (isSelected) {
      BorderStroke(2.dp, borderColor)
    } else {
      null
    },
    modifier = Modifier.size(40.dp),
  ) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
      if (char != null) {
        Text(
          text = char.toString(),
          fontSize = 20.sp,
          color = contentColor,
        )
      }
    }
  }
}

@Preview
@Composable
private fun PlateNumberTextFieldPreview() {
  MaterialTheme {
    Column {
      remember {
        persistentMapOf(
          PlateNumberType.CIVIL to "京A12345",
          PlateNumberType.NEW_ENERGY to "京A12345Z",
          PlateNumberType.WJ2012 to "WJ01警1234",
          PlateNumberType.PLA2012 to "京V12345",
          PlateNumberType.SHI2012 to "使12345",
          PlateNumberType.SHI2017 to "12345使",
          PlateNumberType.LING2012 to "领12345",
          PlateNumberType.LING2018 to "12345领",
          PlateNumberType.AVIATION to "民航12345",
        )
      }.forEach { (plateNumberType, plateNumber) ->
        PlateNumberTextField(
          value = TextFieldValue(plateNumber),
          isFocused = true,
          plateNumberType = plateNumberType,
          modifier = Modifier,
        )
        Spacer(Modifier.height(8.dp))
      }
      PlateNumberTextField(
        value = TextFieldValue("苏A123456"),
        isFocused = true,
        plateNumberType = PlateNumberType.LING2018,
      )
      Spacer(Modifier.height(8.dp))
      PlateNumberTextField(
        value = TextFieldValue(""),
        maxLength = 7,
        isFocused = true,
      )
      Spacer(Modifier.height(8.dp))
      PlateNumberTextField(
        value = TextFieldValue(""),
        maxLength = 7,
        isFocused = false,
      )
    }
  }
}
