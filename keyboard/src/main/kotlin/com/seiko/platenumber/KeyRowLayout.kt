package com.seiko.platenumber

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ParentDataModifierNode
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun KeyRowLayout(
  modifier: Modifier = Modifier,
  rowCount: Int = 10,
  spacing: Dp = 8.dp,
  content: @Composable KeyRowLayoutScope.() -> Unit,
) {
  Layout(
    modifier = modifier,
    content = {
      with(KeyRowLayoutScope) { content() }
    },
    measurePolicy = { measurables, constraints ->
      val spacingPx = spacing.roundToPx()

      // 需要把所有 spacing 都算进去，确保最小的 itemWidth 都是一样大。
      val effectiveWidth = constraints.maxWidth - (rowCount - 1) * spacingPx

      val realRowCount = minOf(rowCount, measurables.size)

      var usedWidth = 0f
      var fillSpacingIndex = -1
      val placeables = List(realRowCount) {
        val measurable = measurables[it]

        val rowWeight = (measurable.rowWeight ?: 1f) * 0.1f
        if (measurable.rowFillSpacing == true) {
          fillSpacingIndex = it
        }

        val itemKeyWidth = (effectiveWidth * rowWeight).toInt()
        usedWidth += itemKeyWidth
        measurable.measure(
          Constraints.fixed(itemKeyWidth, constraints.maxHeight),
        )
      }

      // 计算完以后，就把多余的 spacing 减掉，但是如果有 fillSpacing 就不用减，直接给它消化。
      var moreSpacing = (rowCount - realRowCount) * spacingPx
      if (fillSpacingIndex == -1) {
        usedWidth -= moreSpacing
      }

      // 如果有 fillSpacing ，不需要计算多余的横向间距，给它消化。
      val horizontalPadding = if (fillSpacingIndex != -1) {
        moreSpacing += (effectiveWidth - usedWidth).toInt()
        0f
      } else {
        (effectiveWidth - usedWidth) / 2
      }

      layout(constraints.maxWidth, constraints.maxHeight) {
        var x = horizontalPadding.toInt()
        placeables.forEachIndexed { index, placeable ->
          placeable.placeRelative(x, 0)
          x += placeable.width + spacingPx
          if (index == fillSpacingIndex) {
            x += moreSpacing
          }
        }
      }
    },
  )
}

interface KeyRowLayoutScope {

  @Stable
  fun Modifier.rowWeight(weight: Float, fillSpacing: Boolean = false): Modifier

  companion object : KeyRowLayoutScope {
    @Stable
    override fun Modifier.rowWeight(weight: Float, fillSpacing: Boolean) =
      this then RowWeightElement(weight, fillSpacing)
  }
}

private data class RowWeightElement(
  private val weight: Float,
  private val fillSpacing: Boolean,
) : ModifierNodeElement<RowWeightModifier>() {
  override fun create() = RowWeightModifier(weight, fillSpacing)

  override fun update(node: RowWeightModifier) {
    node.weight = weight
    node.fillSpacing = fillSpacing
  }

  override fun InspectorInfo.inspectableProperties() {
    name = "RowWeight"
    value = mapOf(
      "weight" to weight,
      "fillSpacing" to fillSpacing,
    )
  }
}

private class RowWeightModifier(
  weight: Float,
  fillSpacing: Boolean,
) : ParentDataModifierNode, RowWeightParentData, Modifier.Node() {

  override var weight: Float = weight
    internal set

  override var fillSpacing: Boolean = fillSpacing
    internal set

  override fun Density.modifyParentData(parentData: Any?): Any {
    return this@RowWeightModifier
  }
}

private interface RowWeightParentData {
  val weight: Float
  val fillSpacing: Boolean
}

private val Measurable.rowWeight: Float?
  get() = (parentData as? RowWeightParentData)?.weight

private val Measurable.rowFillSpacing: Boolean?
  get() = (parentData as? RowWeightParentData)?.fillSpacing

@Composable
private fun KeyRowLayoutScope.item(
  char: Char,
  weight: Float = when (char) {
    MORE, BACK, DEL, OK -> 1.5f
    else -> 1f
  },
) {
  KeyButton(
    key = char,
    onClick = {},
    modifier = Modifier.rowWeight(weight, fillSpacing = char == EMPTY),
  )
}

@Preview(showBackground = true, backgroundColor = 0xFFDDDDDD)
@Composable
private fun KeyRowLayoutWithTestWeightPreview() {
  MaterialTheme {
    Column(Modifier.padding(8.dp)) {
      KeyRowLayout(Modifier.height(30.dp)) {
        "AAAAAAAAAA".forEach { char ->
          item(char)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "AAAAAAAA".forEach { char ->
          item(char)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "AAAAAAAAA".forEach { char ->
          item(char)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        listOf(
          'A' to 1f,
          'B' to 2f,
          'C' to 4f,
          'D' to 2f,
          'E' to 1f,
        ).forEach {
          item(it.first, it.second)
        }
      }
    }
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFDDDDDD)
@Composable
private fun CustomKeyRowLayoutPreview() {
  MaterialTheme {
    Column(Modifier.padding(8.dp)) {
      KeyRowLayout(Modifier.height(30.dp)) {
        "1234567890".forEach {
          item(it)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "QWERTYUIOP".forEach {
          item(it)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "ASDFGHJKL".forEach {
          item(it)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "${MORE}ZXCVBNM$OK".forEach {
          item(
            it,
            weight = when (it) {
              MORE, OK -> 1.5f
              else -> 1f
            },
          )
        }
      }
    }
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFDDDDDD)
@Composable
private fun PlateKeyRowLayoutPreview() {
  MaterialTheme {
    Column(Modifier.padding(8.dp)) {
      KeyRowLayout(Modifier.height(30.dp)) {
        "京津晋冀蒙辽吉黑沪苏".forEach { char ->
          item(char)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "浙皖闽赣鲁豫鄂湘粤桂".forEach { char ->
          item(char)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "琼渝川贵云藏陕甘".forEach { char ->
          item(char)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "青宁新台$EMPTY$MORE$DEL$OK".forEach { char ->
          item(char)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "ZXCV$EMPTY$BACK$DEL$OK".forEach { char ->
          item(char)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "ZX民使$EMPTY$BACK$DEL$OK".forEach { char ->
          item(char)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "WXYZ$EMPTY$BACK$DEL$OK".forEach { char ->
          item(char)
        }
      }
      Spacer(Modifier.height(8.dp))
      KeyRowLayout(Modifier.height(30.dp)) {
        "ZXCVBN$EMPTY$DEL$OK".forEach { char ->
          item(char)
        }
      }
    }
  }
}
