package com.seiko.platenumber


import androidx.compose.ui.graphics.Color

enum class PlateNumberType {

    /**
     * 民用
     */
    CIVIL,

    /**
     * 新能源车牌
     */
    NEW_ENERGY,

    /**
     * 新式武警车牌
     */
    WJ2012,

    /**
     * 新式军车车牌
     */
    PLA2012,

    /**
     * 旧式大使馆车牌
     */
    SHI2012,

    /**
     * 新式大使馆车牌
     */
    SHI2017,

    /**
     * 旧式领事馆车牌
     */
    LING2012,

    /**
     * 新式领事馆车牌
     */
    LING2018,

    /**
     * 民航车牌
     */
    AVIATION,

    /**
     * 未知车牌
     */
    UNKNOWN,
}

val PlateNumberType.containerColor: Color
    get() = when (this) {
        PlateNumberType.CIVIL -> Color.Blue
        PlateNumberType.NEW_ENERGY -> Color.Green
        PlateNumberType.WJ2012, PlateNumberType.PLA2012,
        PlateNumberType.AVIATION,
        -> Color.White
        PlateNumberType.SHI2012, PlateNumberType.SHI2017,
        PlateNumberType.LING2012, PlateNumberType.LING2018,
        -> Color.Black
        PlateNumberType.UNKNOWN -> Color.Gray
    }

val PlateNumberType.contentColor: Color
    get() = when (this) {
        PlateNumberType.CIVIL -> Color.White
        PlateNumberType.NEW_ENERGY -> Color.Black
        PlateNumberType.WJ2012, PlateNumberType.PLA2012,
        PlateNumberType.AVIATION,
        -> Color.Black
        PlateNumberType.SHI2012, PlateNumberType.SHI2017,
        PlateNumberType.LING2012, PlateNumberType.LING2018,
        -> Color.White
        PlateNumberType.UNKNOWN -> Color.White
    }

val PlateNumberType.maxLength: Int
    get() = when (this) {
        PlateNumberType.CIVIL -> 7
        PlateNumberType.NEW_ENERGY -> 8
        PlateNumberType.WJ2012 -> 9
        PlateNumberType.PLA2012 -> 7
        PlateNumberType.SHI2012 -> 6
        PlateNumberType.SHI2017 -> 6
        PlateNumberType.LING2012 -> 6
        PlateNumberType.LING2018 -> 6
        PlateNumberType.AVIATION -> 7
        else -> 7
    }
