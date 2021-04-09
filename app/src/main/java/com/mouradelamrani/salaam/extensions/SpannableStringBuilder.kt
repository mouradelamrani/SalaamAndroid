package com.mouradelamrani.salaam.extensions

import android.text.SpannableStringBuilder

/** Same as [SpannableStringBuilder.append], but appends text with multiple spans. */
fun SpannableStringBuilder.append(text: CharSequence, flags: Int, vararg spans: Any): SpannableStringBuilder {
    val start = length
    append(text)
    spans.forEach { span -> setSpan(span, start, length, flags) }
    return this
}