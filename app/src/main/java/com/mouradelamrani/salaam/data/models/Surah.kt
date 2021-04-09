package com.mouradelamrani.salaam.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Surah(
    val name: String,
    @StringRes val textResId: Int,
    @DrawableRes val iconResId: Int,
    @DrawableRes val logoResId: Int
)