package com.mouradelamrani.salaam.extensions

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

@ColorInt
fun Fragment.getColor(@ColorRes id: Int): Int = ContextCompat.getColor(requireContext(), id)

fun Fragment.dpToPxFloat(dp: Float): Float = requireContext().dpToPxFloat(dp)
fun Fragment.dpToPxFloat(dp: Int): Float = requireContext().dpToPxFloat(dp)
fun Fragment.dpToPx(dp: Float): Int = requireContext().dpToPx(dp)
fun Fragment.dpToPx(dp: Int): Int = requireContext().dpToPx(dp)