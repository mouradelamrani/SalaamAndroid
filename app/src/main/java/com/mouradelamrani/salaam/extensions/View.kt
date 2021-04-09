package com.mouradelamrani.salaam.extensions

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import com.mouradelamrani.salaam.utils.AndroidScreen


/*
 * General-purpose extensions for View and it's subclasses.
 */

fun View.showKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as? InputMethodManager ?: return
    inputManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.isKeyboardShown(): Boolean {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as? InputMethodManager ?: return false
    return inputManager.isActive(this)
}

fun View.hideKeyboard() {
    val keyboard = context.getSystemService(Activity.INPUT_METHOD_SERVICE)
            as? InputMethodManager ?: return
    if (keyboard.isActive) keyboard.hideSoftInputFromWindow(this.windowToken, 0)
}

fun TextView.setOnImeActionListener(actionId: Int, onEditorAction: () -> Unit) {
    this.setOnEditorActionListener(OnImeActionListener(actionId, onEditorAction))
}

fun View.setVisible(isVisible: Boolean) {
    if (visibility == View.VISIBLE && isVisible || visibility == View.GONE && !isVisible) return
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.setInvisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

//fun TextView.setOnEnterActionListener(onAction: () -> Unit) =
//        this.setOnEditorActionListener(EnterActionListener(onAction))

fun EditText.requestFocusWithCursor(index: Int = this.length()) {
    this.requestFocus()
    this.setSelection(index)
}

fun EditText.string(): String = this.text.toString()

//fun TextView.setOnImeActionListener(actionId: Int, onEditorAction: () -> Unit) {
//    this.setOnEditorActionListener(OnImeActionListener(actionId, onEditorAction))
//}

fun ImageView.setTintColor(@ColorInt color: Int) =
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))

//fun GenericDraweeHierarchy.setActualImageTintColor(@ColorInt color: Int) =
//    setActualImageColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP))


fun View.setPaddingLeft(padding: Int) =
    this.setPadding(padding, this.paddingTop, this.paddingRight, this.paddingBottom)

fun View.setPaddingTop(padding: Int) =
    this.setPadding(this.paddingLeft, padding, this.paddingRight, this.paddingBottom)

fun View.setPaddingRight(padding: Int) =
    this.setPadding(this.paddingLeft, this.paddingTop, padding, this.paddingBottom)

fun View.setPaddingBottom(padding: Int) =
    this.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, padding)

fun View.dpToPxFloat(dp: Float): Float = context.dpToPxFloat(dp)
fun View.dpToPxFloat(dp: Int): Float = context.dpToPxFloat(dp)
fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun View.dpToPx(dp: Int): Int = context.dpToPx(dp)
fun View.getInset(): Int = AndroidScreen.getViewInset(this)

fun View.setAlphaIfNew(alpha: Float) {
    if (this.alpha != alpha) this.alpha = alpha
}

@ColorInt
fun View.getColor(@ColorRes id: Int): Int = ContextCompat.getColor(context, id)

fun View.getDrawable(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(context, id)

fun View.getFont(@FontRes id: Int): Typeface? = ResourcesCompat.getFont(context, id)


private class OnImeActionListener(
    private val actionId: Int,
    val onEditorAction: () -> Unit
) : TextView.OnEditorActionListener {

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == this.actionId) {
            this.onEditorAction()
            return true
        }
        return false
    }

}