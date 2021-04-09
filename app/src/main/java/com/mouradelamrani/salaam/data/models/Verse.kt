package com.mouradelamrani.salaam.data.models

import androidx.annotation.RawRes
import androidx.annotation.StringRes

data class Verse(
    val position: Int,
    val text: String,
    val transliteratedText: String,
    @StringRes val translatedTextRestId: Int,
    @RawRes val audioResId: Int
)