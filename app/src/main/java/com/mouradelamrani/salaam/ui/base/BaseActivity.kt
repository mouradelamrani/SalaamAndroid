package com.mouradelamrani.salaam.ui.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mouradelamrani.salaam.LocaleHelper

/** Base activity class with basic functions. */
abstract class BaseActivity : AppCompatActivity() {

    // region lifecycle

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.getInstance(base).wrapContext(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // init activity
        super.onCreate(savedInstanceState)
        layoutId?.let { setContentView(it) }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        initArgs(intent.extras)
        initViewModels()
        observeViewModels()
        layoutId?.let { initViews() }
    }

    // endregion


    // region abstract

    /** Activity's layout id, may be null for activities without layout. */
    protected abstract val layoutId: Int?

    /**
     * This method is called in [onCreate] when savedStateInstance is null.
     * It can be used to initialize Activity's arguments from [Bundle].
     * @param extras arguments supplied to the activity's connect intent.
     */
    protected open fun initArgs(extras: Bundle?) {}

    /**
     * This method is called in [onCreate]. It must be used to initialize view models.
     */
    protected open fun initViewModels() {}

    /**
     * This method is called in [onCreate] after [initViewModels]. It must be used to set up
     * view model observers.
     */
    protected open fun observeViewModels() {}

    /**
     * This method is called in [onCreate]. It may be overridden to initialize views
     * on Activity creation.
     * Arguments from [initArgs] are available here.
     * This method isn't called if [layoutId] is null.
     */
    protected open fun initViews() {}

    // endregion

    protected fun log(msg: String) = Log.d(this::class.java.simpleName, msg)

}