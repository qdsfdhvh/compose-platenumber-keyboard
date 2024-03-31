package com.seiko.platenumber

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

object KeyboardEngine {

  /**
   * @param selectIndex 当前选中的车牌字符序号
   * @param showMoreLayout  是否显示对应序号的“更多”状态的键盘布局
   * @param numberType 指定车牌号码类型。要求引擎内部只按此类型来处理。
   */
  fun getLayout(
    selectIndex: Int,
    showMoreLayout: Boolean,
    numberType: PlateNumberType,
  ): ImmutableList<String> {
    if (selectIndex == 0 && numberType in persistentListOf(
        PlateNumberType.CIVIL,
        PlateNumberType.NEW_ENERGY,
        PlateNumberType.PLA2012,
      )
    ) {
      return if (showMoreLayout) {
        persistentListOf(
          "学警港澳航挂试超使领",
          "1234567890",
          "ABCDEFGHJK",
          "WXYZ$EMPTY$BACK$DEL$OK",
        )
      } else {
        persistentListOf(
          "京津晋冀蒙辽吉黑沪苏",
          "浙皖闽赣鲁豫鄂湘粤桂",
          "琼渝川贵云藏陕甘",
          "青宁新台$EMPTY$MORE$DEL$OK",
        )
      }
    }

    return if (showMoreLayout) {
      persistentListOf(
        "学警港澳航挂试超使领",
        "1234567890",
        "ABCDEFGHJK",
        "WXYZ$EMPTY$BACK$DEL$OK",
      )
    } else {
      persistentListOf(
        "1234567890",
        "QWERTYUIOP",
        "ASDFGHJKLM",
        "ZXCVBN$EMPTY$MORE$DEL$OK",
      )
    }
  }
}
