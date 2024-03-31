package com.seiko.platenumber

fun Char.fixIsLetterOrDigit(): Boolean {
    return isLetterOrDigit() && code < 128
}
