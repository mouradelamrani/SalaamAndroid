package com.mouradelamrani.salaam.ui.surah

import android.animation.*
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.RawRes
import androidx.core.animation.addListener
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mouradelamrani.salaam.BuildConfig
import com.mouradelamrani.salaam.R
import com.mouradelamrani.salaam.data.models.Surah
import com.mouradelamrani.salaam.data.models.Verse
import com.mouradelamrani.salaam.extensions.*
import com.mouradelamrani.salaam.ui.base.BaseActivity
import com.mouradelamrani.salaam.utils.FontSpan
import kotlinx.android.synthetic.main.activity_surah.*
import kotlinx.android.synthetic.main.view_surah_end.*


class SurahActivity : BaseActivity(), MediaPlayer.OnCompletionListener {

    companion object {
        const val ARG_SURAH_NAME = "arg_surah_name"
    }

    override val layoutId: Int? = R.layout.activity_surah
    private lateinit var surahName: String
    private lateinit var viewModel: SurahViewModel
    private var mediaPlayer: MediaPlayer? = null

    private var playerProgressHandler: Handler? = null
    private var progressAnimator: ObjectAnimator? = null

    @RawRes
    private var currentAudioResId: Int? = null

    // player set to pause by user and can be resumed
    private var pausedByUser: Boolean = false
        set(value) {
            field = value
            updateButtonPlayPause()
        }

    private lateinit var fontMedium: Typeface
    private var animateShowVerse = false
    private var animateShowSurahEnd = false

    private lateinit var verseGestureDetector: GestureDetectorCompat
    private lateinit var verseEndGestureDetector: GestureDetectorCompat


    // region activity

    override fun initArgs(extras: Bundle?) {
        surahName = extras!!.getString(ARG_SURAH_NAME)
            ?: throw IllegalArgumentException("arg_surah_name must be provided")
    }

    override fun initViewModels() {
        val viewModelProvider = ViewModelProvider(this, SurahViewModelFactory(this, surahName))
        viewModel = viewModelProvider.get(SurahViewModel::class.java)
    }

    override fun observeViewModels() {
        viewModel.surah.observe(this, this::showSurah)
        viewModel.currentVerse.observe(this) { pair ->
            if (pair != null) {
                val position = pair.first
                val verse = pair.second
                if (animateShowVerse) {
                    showCurrentVerseAnimated(position, verse)
                } else {
                    animateShowVerse = true
                    showCurrentVerse(position, verse)
                }
                releasePlayer()
                playerProgress.progress = 0
                currentAudioResId = verse.audioResId
            } else {
                releasePlayer()
                playerProgress.progress = 0
                currentAudioResId = null
            }
        }
        viewModel.surahEndVisible.observe(this) { visible ->
            if (animateShowSurahEnd) {
                animateSurahEnd(visible)
            } else {
                showSurahEnd(visible)
                animateShowSurahEnd = true
            }
        }
        viewModel.hasPreviousVerse.observe(this) { hasPrevious ->
            buttonPrevious.isEnabled = hasPrevious
        }
//        viewModel.hasNextVerse.observe(this) { hasNext ->
//            buttonNext.isEnabled = hasNext
//        }
        viewModel.playerRepeatMode.observe(this, this::setPlayerRepeatMode)
        viewModel.playCurrentVerse.observe(this) {
            if (mediaPlayer == null) currentAudioResId?.let { startPlayer(it) }
        }
        viewModel.close.observeEvent(this) {
            log("close event from view model")
            finish()
        }
    }

    override fun initViews() {
        fontMedium = ResourcesCompat.getFont(this, R.font.roboto_medium)!!
        // apply window insets
        layoutSurah.setOnApplyWindowInsetsListener { _, insets ->
            // top
            val newTopMargin = dpToPx(3) + insets.systemWindowInsetTop
            if ((buttonClose.layoutParams as ViewGroup.MarginLayoutParams).topMargin != newTopMargin) {
                buttonClose.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = newTopMargin
                }
            }
            // bottom
            layoutSurah.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
        layoutSurah.requestApplyInsets()

        buttonClose.setOnClickListener { finish() }
        buttonPrevious.setOnClickListener { viewModel.previousClick() }
        buttonNext.setOnClickListener { viewModel.nextClick() }
        buttonModeLoop.setOnClickListener { viewModel.loopModeClicked() }
        buttonModeNext.setOnClickListener { viewModel.nextModeClicked() }
        buttonPlayPause.setOnClickListener { playPauseClicked() }
        buttonRepeatSurah.setOnClickListener { viewModel.repeatSurahClicked() }

        // set up gesture listener
        verseGestureDetector = GestureDetectorCompat(
            this,
            SwipeGestureListener(
                onLeft = { viewModel.nextClick() },
                onRight = { viewModel.previousClick() })
        )
        scrollViewVerse.setOnTouchListener { _, event -> verseGestureDetector.onTouchEvent(event) }
        verseEndGestureDetector = GestureDetectorCompat(
            this,
            SwipeGestureListener(onRight = { viewModel.previousClick() })
        )
        layoutSurahEnd.setOnTouchListener { _, event -> verseEndGestureDetector.onTouchEvent(event) }

        updateButtonPlayPause()
        Glide.with(this).asGif().load(R.raw.repeat_animation_gif).into(ivAnimationRepeat)

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

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
        playerProgress.progress = 0
        log("#progress onStop: set progress to 0")
    }
//
//    // TODO: apply to view only
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        gestureDetector.onTouchEvent(event)
//        return super.onTouchEvent(event)
//    }

    // endregion


    // region view

    private fun showSurah(surah: Surah) {
        ivLogo.setImageResource(surah.logoResId)
        tvSurahEnd.text = SpannableStringBuilder()
            .append(getString(R.string.activity_surah_end_text))
            .append(
                getString(R.string.activity_surah_end_text_surah_s, surah.name),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                FontSpan(fontMedium)
            )
    }

    private fun showCurrentVerse(position: Int, verse: Verse) {
        // todo: remove number from debug build
        tvTranslatedText.text =
            if (BuildConfig.DEBUG) "[$position]. ${getString(verse.translatedTextRestId)}"
            else getString(verse.translatedTextRestId)
        tvText.text = verse.text
        tvTransliteratedText.text = verse.transliteratedText
    }

    private fun showCurrentVerseAnimated(position: Int, verse: Verse) {
        val hideAnimator = ObjectAnimator.ofFloat(
            scrollViewVerse, "alpha", 0f
        ).apply {
            duration = 150L
            interpolator = AccelerateInterpolator()
            addListener(onEnd = { showCurrentVerse(position, verse) })
        }
        val showAnimator = ObjectAnimator.ofFloat(
            scrollViewVerse, "alpha", 1f
        ).apply {
            duration = 150L
            interpolator = DecelerateInterpolator()
        }

        val crossFadeAnim = AnimatorSet().apply {
            playSequentially(hideAnimator, showAnimator)
        }
        crossFadeAnim.start()
    }

    private fun updateButtonPlayPause() {
        val showPause = mediaPlayer?.isPlaying == true && !pausedByUser
        buttonPlayPause.tag = showPause
        buttonPlayPause.setImageResource(if (showPause) R.drawable.selector_ic_pause else R.drawable.selector_ic_play)
        if (showPause) buttonPlayPause.setPadding(
            dpToPx(14.5f), dpToPx(12.5f), dpToPx(14f), dpToPx(12.5f)
        ) else buttonPlayPause.setPadding(
            dpToPx(15f), dpToPx(13.5f), dpToPx(11.5f), dpToPx(13f)
        )
    }

    private fun setPlayerRepeatMode(mode: PlayerRepeatMode) {
        buttonModeLoop.isSelected = mode == PlayerRepeatMode.Loop
        buttonModeNext.isSelected = mode == PlayerRepeatMode.Next
        mediaPlayer?.isLooping = mode == PlayerRepeatMode.Loop
    }

    private fun showSurahEnd(visible: Boolean) {
        if (visible) {
            scrollViewVerse.setVisible(false)
            layoutPlayer.setVisible(false)
            layoutSurahEnd.setVisible(true)
        } else {
            layoutSurahEnd.setVisible(false)
            layoutPlayer.setVisible(true)
            scrollViewVerse.setVisible(true)
        }
    }

    private fun animateSurahEnd(visible: Boolean) {
        if (visible) {
            animateShowVerse = false
            animateViewHide(scrollViewVerse)
            animateViewHide(layoutPlayer)
            animateViewShow(layoutSurahEnd)
        } else {
            animateViewShow(scrollViewVerse)
            animateViewShow(layoutPlayer)
            animateViewHide(layoutSurahEnd)
        }
    }

    private fun animateViewShow(view: View) {
        view.alpha = 0f
        view.setVisible(true)
        view.animate().alpha(1f)
            .setDuration(300)
            .setInterpolator(LinearInterpolator())
            .setListener(null)
            .start()
    }

    private fun animateViewHide(view: View) {
        view.animate().alpha(0f)
            .setDuration(300)
            .setInterpolator(LinearInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.setVisible(false)
                }
            })
            .start()
    }

    // endregion


    // region player

    private fun startPlayer(@RawRes audioId: Int) {
        releasePlayer()
        mediaPlayer = MediaPlayer.create(this, audioId).apply {
            isLooping = buttonModeLoop.isSelected
            setOnCompletionListener(this@SurahActivity)
            start()
        }
        updateButtonPlayPause()
        observePlayerProgress()
    }

    private fun releasePlayer() {
        stopObservingPlayerProgress()
        mediaPlayer?.release()
        mediaPlayer = null
        pausedByUser = false
    }

    override fun onCompletion(mp: MediaPlayer) {
        releasePlayer()
        animateProgress(playerProgress.max)
        log("#progress onCompletion: animate progress to max")
        viewModel.onPlayCompleted()
    }

    private fun playPauseClicked() {
        if (buttonPlayPause.tag == true) {
            // pause clicked
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                pausedByUser = true
                stopObservingPlayerProgress()
            }
        } else {
            // play clicked
            when {
                mediaPlayer == null -> currentAudioResId?.let { startPlayer(it) }
                mediaPlayer?.isPlaying == false && pausedByUser -> {
                    observePlayerProgress()
                    mediaPlayer?.start()
                    pausedByUser = false
                }
            }
        }
    }

    private fun observePlayerProgress() {
        if (playerProgressHandler != null) return
        log("#progress observePlayerProgress: max = ${mediaPlayer?.duration ?: 0}")

        playerProgress.max = mediaPlayer?.duration ?: 0
        playerProgressHandler = Handler(Looper.getMainLooper())
        playerProgressHandler?.post { updatePlayerProgress() }
    }

    private fun updatePlayerProgress() {
//        log(
//            "#progress updatePlayerProgress: progress = ${mediaPlayer?.currentPosition ?: 0}, " +
//                    "duration = ${mediaPlayer?.duration}"
//        )
        if (mediaPlayer != null) {
            val progress = mediaPlayer?.currentPosition ?: 0
            animateProgress(progress)
//            log("#progress updatePlayerProgress: animate progress to $progress")
            playerProgressHandler?.postDelayed({ updatePlayerProgress() }, 50)
        } else {
            stopObservingPlayerProgress()
        }
    }

    private fun stopObservingPlayerProgress() {
        log("#progress stopObservingPlayerProgress: playerProgress.max = ${playerProgress.max}")
        playerProgressHandler?.removeCallbacksAndMessages(null)
        playerProgressHandler = null
        progressAnimator?.cancel()
    }

    private fun animateProgress(progress: Int) {
        progressAnimator?.cancel()
        progressAnimator = ObjectAnimator.ofInt(playerProgress, "progress", progress).apply {
            duration = 50
            addListener(onEnd = { progressAnimator = null })
            start()
        }
    }

    // endregion

}