package com.platform45.fx45.extensions

fun String.splitInTwo(): Array<String> {
    val len: Int = this.length
    val a: String = this.substring(0, len / 2)
    val b: String = this.substring(len / 2)
    return arrayOf(a, b)
}