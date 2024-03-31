package com.seiko.platenumber

import androidx.compose.runtime.Immutable

private const val CIVIL_PROVINCES = "京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新台"

sealed interface KeyVisibleRegistry {
  fun isEnable(key: Char): Boolean

  data object AllOpen : KeyVisibleRegistry {
    override fun isEnable(key: Char): Boolean = true
  }

  data object AllClose : KeyVisibleRegistry {
    override fun isEnable(key: Char): Boolean = false
  }

  @Immutable
  class WhiteList(private val chars: CharArray) : KeyVisibleRegistry {
    override fun isEnable(key: Char): Boolean = chars.contains(key)
  }

  @Immutable
  class BlackList(private val chars: CharArray) : KeyVisibleRegistry {
    override fun isEnable(key: Char): Boolean = !chars.contains(key)
  }

  data class PlateNumber(
    private val numberType: PlateNumberType,
    private val selectIndex: Int,
  ) : KeyVisibleRegistry {
    override fun isEnable(key: Char): Boolean {
      return when (key) {
        DEL -> selectIndex > 0
        OK -> true // selectIndex == numberType.maxLength
        MORE, BACK -> true
        else -> {
          if (selectIndex == numberType.maxLength) {
            return false
          }
          when (numberType) {
            PlateNumberType.CIVIL,
            PlateNumberType.NEW_ENERGY,
            -> {
              when (selectIndex) {
                0 -> key in CIVIL_PROVINCES
                1 -> key in 'A'..'Z'
                else -> {
                  when (key) {
                    'I', 'O' -> false
                    else -> true
                  }
                }
              }
            }

            else -> when (key) {
              'I', 'O' -> false
              else -> true
            }
          }
        }
      }
    }
  }
}
