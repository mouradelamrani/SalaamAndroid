package com.mouradelamrani.salaam.ui.main

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import com.mouradelamrani.salaam.R
import com.mouradelamrani.salaam.data.AppPreferences
import com.mouradelamrani.salaam.data.DataRepository
import com.mouradelamrani.salaam.data.models.Surah
import com.mouradelamrani.salaam.extensions.*
import com.mouradelamrani.salaam.ui.base.BaseActivity
import com.mouradelamrani.salaam.ui.surah.SurahActivity
import com.mouradelamrani.salaam.utils.FirstLastLinearMarginDecorator
import com.mouradelamrani.salaam.utils.GridSpaceMarginDecorator
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), SurahAdapter.Listener {

    override val layoutId: Int? = R.layout.activity_main

    private lateinit var viewModel: MainViewModel
    private val surahAdapter = SurahAdapter(this)

    private var animateMenu: Boolean = false


    // region activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inc launch number
        AppPreferences.getInstance(this).launchNumber++
    }

    override fun initViews() {
        // apply window insets
        layoutMain.setOnApplyWindowInsetsListener { _, insets ->
            // top
            val newTopMargin = dpToPx(12.5f) + insets.systemWindowInsetTop
            if ((buttonMenu.layoutParams as ViewGroup.MarginLayoutParams).topMargin != newTopMargin) {
                buttonMenu.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = newTopMargin
                }
            }
            // bottom
            val newBottomMargin = dpToPx(60) + insets.systemWindowInsetBottom
            if ((rvSurahs.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin != newBottomMargin) {
                rvSurahs.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = newBottomMargin
                }
            }
            insets
        }
        layoutMain.requestApplyInsets()

        // recycler view
        rvSurahs.setHasFixedSize(true)
        rvSurahs.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        rvSurahs.itemAnimator = null
        rvSurahs.addItemDecoration(
            GridSpaceMarginDecorator(
                verticalSpace = dpToPxFloat(45.5f),
                horizontalSpace = dpToPxFloat(20)
            )
        )
        rvSurahs.addItemDecoration(FirstLastLinearMarginDecorator(dpToPx(40), 0))
        rvSurahs.adapter = surahAdapter
        surahAdapter.items = DataRepository.surahs

        buttonMenu.setOnToggleListener(viewModel::setMenuOpen)

        // lights animations
        ObjectAnimator.ofFloat(
            ivLampLeft, "translationX",
            dpToPxFloat(3)
        ).apply {
            duration = 1050L
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(
            ivLampRight, "translationX",
            dpToPxFloat(2.5f)
        ).apply {
            startDelay = 350
            duration = 1250L
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    override fun initViewModels() {
        val viewModelProvider = ViewModelProvider(this, MainViewModelFactory(this))
        viewModel = viewModelProvider.get(MainViewModel::class.java)
    }

    override fun observeViewModels() {
        viewModel.menuOpen.observe(this) { isOpen ->
            if (!animateMenu) buttonMenu.setState(isOpen)
            showMenuVisible(isOpen, animateMenu)
            animateMenu = true
        }
        viewModel.menuBackgroundBitmap.observe(this) { bitmap ->
            if (bitmap != null) showMenuBackground(bitmap)
            else ivMenuBlur.setImageDrawable(null)
        }
        viewModel.captureMenuBackground.observeEvent(this) {
            layoutMain.post {
                loadBitmapFromView(layoutMain)?.let { viewModel.saveMenuBackground(it) }
            }
        }
    }

    override fun onBackPressed() {
        if (menuFragmentContainer.isVisible) viewModel.setMenuOpen(false)
        else super.onBackPressed()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        log("onConfigurationChanged")
    }

    // endregion


    // region view

    override fun onSurahClick(surah: Surah) {
        val intent = Intent(this, SurahActivity::class.java).apply {
            putExtra(SurahActivity.ARG_SURAH_NAME, surah.name)
        }
        startActivity(intent)
    }

    private fun showMenuVisible(visible: Boolean, animate: Boolean) {
        // blur background
        ivMenuBlur.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        // fragment
        when {
            visible && animate -> animateFragmentShow()
            else -> menuFragmentContainer.setVisible(visible)
        }
    }

    private fun animateFragmentShow() {
        menuFragmentContainer?.apply {
            scaleX = 0f
            scaleY = 0f
            alpha = 0.0f
            setVisible(true)
            animate()
                .scaleX(1f).scaleY(1f)
                .alpha(1f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator(1f))
                .start()
        }
    }

    private fun showMenuBackground(bitmap: Bitmap) {
        val transforms = MultiTransformation<Bitmap>(
            BlurTransformation(70, 2),
            ColorFilterTransformation(getColor(R.color.menu_overlay))
        )
        Glide.with(this).load(bitmap)
            .apply(RequestOptions.bitmapTransform(transforms))
            .into(ivMenuBlur)
    }

    // endregion


    private fun loadBitmapFromView(v: View): Bitmap? =
        try {
            val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(0, 0, v.width, v.height)
            v.draw(c)
            b
        } catch (ex: Exception) {
            log("failed to get layout bitmap: ${ex.message}")
            ex.printStackTrace()
            null
        }

}
