package com.mouradelamrani.salaam.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.mouradelamrani.salaam.R
import com.mouradelamrani.salaam.data.models.Surah
import kotlinx.android.synthetic.main.view_surah_item.view.*

class SurahItemView : RelativeLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) :
            super(context, attrs, defStyle) {
        View.inflate(context, R.layout.view_surah_item, this)
        setBackgroundResource(R.drawable.background_surah_item)
        isClickable = true
        isFocusable = true
    }

    fun showSurah(surah: Surah) {
        tvName.text = surah.name
        tvText.setText(surah.textResId)
        ivIcon.setImageResource(surah.iconResId)
    }

}