package com.sygic.ui.common.extensions

const val EMPTY_STRING = ""

fun String.classPathToUrl() : String = replace(".","/")
